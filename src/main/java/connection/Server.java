package connection;

import app.App;
import app.Controller;
import db.DBConnector;
import db.DB;
import model.Form;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Server extends Thread {
    private static final int port = 5555;
    private static DBConnector dbConnector;
    private static Handler handler;
    private static ServerSocket socket;
    private static boolean isRunning;
    private static final Controller controller = App.getController();

    public Server() {
        isRunning = true;
    }

    public void run() {
        dbConnector = DB.getConnector();
        dbConnector.setConnection();
        dbConnector.setStatement();

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
        dbConnector.close();
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
            int id = 0;
            Form form = null;

            while (isActive) {
                Message message = connection.receive();
                if (message.getType() == MessageType.FORM_REQUEST) {
                    controller.log("Form requested");
                    String login = message.getLogin();
                    DBConnector connector = DB.getConnector();
                    ResultSet resultSet = connector.getQuery("SELECT * FROM users WHERE login = '" + login + "';");
                    if (resultSet != null) {
                        try {
                            id = resultSet.getInt(4);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }

                    if (id != 0) {
                        form = new Form(id);
                        connection.send(new Message(MessageType.ACCEPTED, form.getData()));
                    }

                } if (message.getType() == MessageType.FORM_SAVE) {
                    String[] array = message.getArray();
                    assert form != null;
                    dbConnector.execute("UPDATE students SET first_name_ed = '" + array[0] +
                            "', last_name_ed = '" + array[1] +
                            "', phone_ed = '" + array[2] +
                            "', email_ed = '" + array[3] +
                            "' WHERE student_id = " + form.getStudent_id() + ";");
                    controller.log("Form updated");
                    return;
                }
            }
        }
    }
}