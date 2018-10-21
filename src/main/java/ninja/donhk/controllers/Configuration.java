package ninja.donhk.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import ninja.donhk.services.database.DBManager;
import ninja.donhk.services.indexer.FileIndexer;
import ninja.donhk.services.indexer.TargetProvider;
import ninja.donhk.services.indexer.UnixProvider;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Configuration {

    @FXML
    public Button configBtn;
    @FXML
    public TextField customInput;
    @FXML
    public CheckBox inverseMode;

    private DBManager dbManager = DBManager.getInstance();

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
    }


    public void scanFiles(MouseEvent mouseEvent) {
        TargetProvider provider;
        if (inverseMode.isSelected()) {
            provider = UnixProvider.newInvertedProvider();
        } else {
            provider = UnixProvider.newProvider();
        }

        String customDirs = customInput.getText().trim();
        if (customDirs.length() > 0) {
            List<String> list = new ArrayList<>();
            for (String element : customDirs.split(";")) {
                list.add(element.trim());
            }
            if (inverseMode.isSelected()) {
                provider.includeDirs(list);
            } else {
                provider.excludeDirs(list);
            }
        }

        FileIndexer indexer = FileIndexer.newInstance(provider, dbManager);
        indexer.loadFiles();
        System.out.println("done");
    }
}
