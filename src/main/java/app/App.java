package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {
    private Stage stage;
    private static Controller controller;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        this.stage = stage;
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(App.class.getResource("app.fxml"));
        AnchorPane pane = null;
        try {
            pane = loader.load();
        } catch (IOException e) {
            System.out.println("Failed to read fxml file.");
        }

        controller = loader.getController();

        if (pane != null) {
            stage.setScene(new Scene(pane));
        }

        setParameters();
        stage.show();
    }

    private void setParameters() {
        stage.setTitle("Server");
        stage.setWidth(400);
        stage.setHeight(400);
        stage.setResizable(false);
    }

    public static Controller getController() {
        return controller;
    }
}
