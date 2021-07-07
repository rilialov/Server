package app;

import connection.Server;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextArea;

public class Controller {
    private Server server;
    private boolean isStopped = true;

    @FXML
    TextArea log;

    @FXML
    private void exit() {
        Platform.exit();
    }

    @FXML
    private void info() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("About");
        dialog.setHeaderText("About this program");
        dialog.setContentText("This is program for practice");
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.show();
    }

    @FXML
    private void start() {
        if (isStopped) {
            isStopped = false;
            server = new Server();
            Thread thread = new Thread(server);
            thread.start();
        }
    }

    @FXML
    private void stop() {
        if (!isStopped) {
            isStopped = true;
            server.close();
            log.appendText("\n" + "Server stopped");
        }
    }

    @FXML
    public void log(String text) {
        log.appendText("\n" + text);
    }
}
