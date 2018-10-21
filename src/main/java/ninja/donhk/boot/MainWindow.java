package ninja.donhk.boot;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ninja.donhk.controllers.MainWindowController;

public class MainWindow extends Application {

    public void startApp(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        final FXMLLoader searchLoader = new FXMLLoader(getClass().getResource("/view/search_window.fxml"));
        final Parent root = searchLoader.load();

        final MainWindowController controller = searchLoader.getController();
        final Scene scene = new Scene(root, 850, 460);

        scene.heightProperty().addListener((obs, oldVal, newVal) -> controller.updateScrollSize(newVal));

        primaryStage.setTitle("Sayayin Search");
        scene.heightProperty();
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
