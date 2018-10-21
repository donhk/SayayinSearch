package ninja.donhk.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ninja.donhk.services.database.DBManager;

public class Configuration {

    @FXML
    public Button configBtn;
    @FXML
    public TextField customInput;

    private DBManager dbManager;

    public Configuration() {
    }

    public void openConfigWindow() throws Exception {
        final FXMLLoader configLoader = new FXMLLoader(getClass().getResource("/view/configuration_menu.fxml"));
        final Parent parent = configLoader.load();

        final Scene secondScene = new Scene(parent);

        // New window (Stage)
        final Stage newWindow = new Stage();
        newWindow.setTitle("Second Stage");
        newWindow.setScene(secondScene);
        newWindow.show();

        if (DBManager.getInstance() == null) {
            System.out.println("dbmanager is null!!!");
        } else {
            System.out.println("dbmanager is NOT null yeah ");
        }
    }

    public void setDbManager(DBManager dbManager) {
        this.dbManager = dbManager;
    }
}
