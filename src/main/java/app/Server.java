package app;

import connection.Comm;
import connection.Message;
import connection.MessageType;
import db.DBController;
import db.FormBuilder;
import model.Form;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Thread {
    private static final int port = 5555;
    private static Handler handler;
    private static ServerSocket socket;
    private static boolean isRunning;
    private static DBController db;
    private static final Controller controller = App.getController();

    public Server() {
        isRunning = true;
    }

    public void run() {
        db = new DBController();
        try {
            socket = new ServerSocket(port);
            controller.log("Server started");
            while(isRunning) {
                Socket client = socket.accept();
                handler = new Handler(client);
                handler.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        if (handler != null) {
            handler.disable();
        }
        isRunning = false;
        try {
            socket.close();
        } catch (IOException e) {
            controller.log("Socket interrupted");
        }
        db.close();
    }

    private static class Handler extends Thread {
        private final Socket socket;
        private boolean isActive;

        public Handler (Socket socket) throws IOException {
            this.socket = socket;
            isActive = true;
        }

        public void run() {
            try {
                Comm connection = new Comm(socket);
                serverHandshake(connection);
                serverMainLoop(connection);
                connection.close();
                controller.log("Client disconnected");
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        void disable() {
            isActive = false;
        }

        private void serverHandshake(Comm connection) throws IOException, ClassNotFoundException {
            if (connection != null) {
                connection.send(new Message(MessageType.CONNECTED));
                controller.log("Client connected");
            }
        }

        private void serverMainLoop(Comm connection) throws IOException, ClassNotFoundException {
            FormBuilder fb = null;
            while (isActive) {
                Message message = connection.receive();
                if (message.getType() == MessageType.FORM_REQUEST) {
                    controller.log("Form requested");
                    String login = message.getLogin();
                    int id = db.getFormID(login);
                    fb = new FormBuilder(id, db);
                    connection.send(new Message(MessageType.ACCEPTED, fb.getData()));
                } if (message.getType() == MessageType.FORM_SAVE) {
                    String[] array = message.getArray();
                    Form form = fb.getForm();
                    db.updateForm(array, form);
                    controller.log("Form updated");
                    return;
                }
            }
        }
    }
}