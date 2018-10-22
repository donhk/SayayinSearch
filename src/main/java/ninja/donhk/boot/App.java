package ninja.donhk.boot;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import ninja.donhk.controllers.MainWindowController;

public class App extends Application {

    public void startApp(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        final FXMLLoader searchLoader = new FXMLLoader(getClass().getResource("/view/search_window.fxml"));
        final Parent root = searchLoader.load();

        final MainWindowController controller = searchLoader.getController();
        final Scene scene = new Scene(root, 850, 460);
        controller.setPrimaryStage(primaryStage);

        scene.heightProperty().addListener((obs, oldVal, newVal) -> controller.updateScrollSize(newVal));
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                primaryStage.close();
            }
        });
        primaryStage.setTitle("Sayayin Search");
        scene.heightProperty();
        primaryStage.setScene(scene);
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/icons/Dragon_Ball.png")));
        primaryStage.show();
    }
}
