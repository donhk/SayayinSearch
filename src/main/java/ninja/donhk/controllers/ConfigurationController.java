package ninja.donhk.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import javafx.scene.text.Text;
import ninja.donhk.model.FileRecord;
import ninja.donhk.services.database.DBManager;
import ninja.donhk.services.indexer.FileIndexer;
import ninja.donhk.services.indexer.TargetProvider;
import ninja.donhk.services.indexer.UnixProvider;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class ConfigurationController implements Initializable {

    @FXML
    public TextField customInput;
    @FXML
    public CheckBox inverseMode;
    @FXML
    public Label progressIndicator;

    private TableView<FileRecord> tableView;
    private Label filesCounter;

    private DBManager dbManager = DBManager.getInstance();

    public ConfigurationController() {
    }


    public void scanFiles(MouseEvent mouseEvent) {
        filesCounter.setText("Scanning");
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


        final Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                final Thread indexerThread = new Thread(indexer::loadFiles);
                indexerThread.start();
                while (indexerThread.isAlive()) {
                    Thread.sleep(1000);
                    final String update = dbManager.getTotalRows() + " Found";
                    updateMessage(update);
                }
                return null;
            }
        };

        progressIndicator.textProperty().bind(task.messageProperty());

        task.setOnSucceeded(e -> {
            progressIndicator.textProperty().unbind();
            // this message will be seen.
            progressIndicator.setText("operation completed successfully");

            filesCounter.setText("Finished");
            tableView.setItems(getInitData());
            tableView.setEditable(false);
            try {
                filesCounter.setText(dbManager.getTotalRows() + " files indexed");
            } catch (SQLException s) {
                //ignored
                filesCounter.setText("Error!");
            }
        });

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }


    private ObservableList<FileRecord> getInitData() {
        List<FileRecord> list = new ArrayList<>();
        try {
            for (Map.Entry<String, String> e : dbManager.getRows(-1).entrySet()) {
                list.add(new FileRecord(e.getValue(), e.getKey()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return FXCollections.observableArrayList(list);
    }

    void setTableView(TableView<FileRecord> tableView) {
        this.tableView = tableView;
    }

    void setFilesCounter(Label filesCounter) {
        this.filesCounter = filesCounter;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        inverseMode.setSelected(true);
        customInput.setText("home; tmp; mnt; etc; opt; srv; bin; var; dev; lib; lib64; run; snap; sbin; lost+found");
        progressIndicator.setText("Nothing yet");
    }
}