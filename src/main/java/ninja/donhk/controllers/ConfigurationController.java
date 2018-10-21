package ninja.donhk.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import ninja.donhk.services.database.DBManager;
import ninja.donhk.services.indexer.FileIndexer;
import ninja.donhk.services.indexer.TargetProvider;
import ninja.donhk.services.indexer.UnixProvider;

import java.util.ArrayList;
import java.util.List;

public class ConfigurationController {

    @FXML
    public TextField customInput;
    @FXML
    public CheckBox inverseMode;

    private DBManager dbManager = DBManager.getInstance();

    public ConfigurationController() {
    }


    public void scanFiles(MouseEvent mouseEvent) {
        final TargetProvider provider;
        if (inverseMode.isSelected()) {
            provider = UnixProvider.newInvertedProvider();
        } else {
            provider = UnixProvider.newProvider();
        }

        final String customDirs = customInput.getText().trim();
        if (customDirs.length() > 0) {
            final List<String> list = new ArrayList<>();
            for (String element : customDirs.split(";")) {
                list.add(element.trim());
            }
            if (inverseMode.isSelected()) {
                provider.includeDirs(list);
            } else {
                provider.excludeDirs(list);
            }
        }

        final FileIndexer indexer = FileIndexer.newInstance(provider, dbManager);
        indexer.loadFiles();
    }
}
