package ninja.donhk.boot;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ninja.donhk.controllers.Search;

public class SearchWindow extends Application {

    public void start(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/search_window.fxml"));
        Parent root = loader.load();

        Search controller = loader.getController();

        Scene scene = new Scene(root, 850, 460);
        scene.heightProperty().addListener((obs, oldVal, newVal) -> {
            controller.updateScrollSize(newVal);
        });

        stage.setTitle("Sayayin Search");
        scene.heightProperty();
        stage.setScene(scene);
        stage.show();
    }
}
