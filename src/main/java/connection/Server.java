package connection;

import app.App;
import app.Controller;
import db.DBConnector;
import db.Fabric;
import model.Form;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Server extends Thread {
    private static final int port = 5555;
    private static DBConnector dbConnector;
    private static boolean isRunning;
    private static final Controller controller = App.getController();

    public void run() {
        while (!isInterrupted()) {
            dbConnector = Fabric.getConnector();
            dbConnector.setConnection();
            dbConnector.setStatement();

            try(ServerSocket socket = new ServerSocket(port)) {
                controller.log("Server started");
                isRunning = true;
                while(isRunning) {
                    Socket client = socket.accept();
                    Handler handler = new Handler(client);
                    handler.start();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static class Handler extends Thread {
        private final Socket socket;

        public Handler (Socket socket) throws IOException {
            this.socket = socket;
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
            while (isRunning) {
                Message message = connection.receive();
                if (message.getType() == MessageType.FORM_REQUEST) {
                    controller.log("Form requested");
                    String login = message.getLogin();
                    DBConnector connector = Fabric.getConnector();
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

        public void run() {
            try {
                Comm connection = new Comm(socket);
                serverHandshake(connection);
                serverMainLoop(connection);
                connection.close();
            } catch (IOException | ClassNotFoundException ioException) {
                ioException.printStackTrace();
            }
        }
    }
}