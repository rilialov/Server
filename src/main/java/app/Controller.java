package app;

import connection.Server;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;

public class Controller {

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
        Server server = new Server();
        Thread thread = new Thread(server);
        thread.start();
    }

    @FXML
    private void stop() {

    }
}
