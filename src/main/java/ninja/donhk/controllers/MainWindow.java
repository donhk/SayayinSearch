package ninja.donhk.controllers;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainWindow extends Application {

    public void start(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/view/mainwindow.fxml"));
        Scene scene = new Scene(root, 850, 460);
        stage.setTitle("Sayayin Search");
        stage.setScene(scene);
        stage.show();
    }
}
