package ninja.donhk.boot;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ninja.donhk.controllers.Configuration;
import ninja.donhk.controllers.Search;
import ninja.donhk.pojos.DBCredentials;
import ninja.donhk.services.database.DBManager;
import ninja.donhk.services.database.DatabaseServer;
import sun.plugin.dom.exception.InvalidStateException;

public class SearchWindow extends Application {

    private final DBManager dbManager;

    public SearchWindow() {
        final DatabaseServer server = new DatabaseServer(
                DBCredentials.USERNAME.val(),
                DBCredentials.PASSWD.val(),
                DBCredentials.DATABASE.val()
        );

        try {
            server.startServer();
            dbManager = DBManager.newInstance(server.getConnection());
        } catch (Exception e) {
            throw new InvalidStateException("Cannot start");
        }

    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        dbManager.loadSchema();
        Configuration configuration = new Configuration();
        configuration.setDbManager(dbManager);

        final FXMLLoader searchLoader = new FXMLLoader(getClass().getResource("/view/search_window.fxml"));
        final Parent root = searchLoader.load();

        final Search controller = searchLoader.getController();
        final Scene scene = new Scene(root, 850, 460);

        scene.heightProperty().addListener((obs, oldVal, newVal) -> controller.updateScrollSize(newVal));

        primaryStage.setTitle("Sayayin Search");
        scene.heightProperty();
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
