package ninja.donhk.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import ninja.donhk.model.FileRecord;
import ninja.donhk.pojos.DBCredentials;
import ninja.donhk.services.database.DBManager;
import ninja.donhk.services.database.DatabaseServer;
import ninja.donhk.utils.Utils;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class MainWindowController implements Initializable {

    @FXML
    public TableView<FileRecord> tableView;
    @FXML
    public ToggleButton enableFullText;
    @FXML
    public TextField searchBar;
    @FXML
    public TableColumn pathColumn;
    @FXML
    public TableColumn fileColumn;
    @FXML
    public Label filesCounter;
    @FXML
    public ScrollPane scrollPane;

    private DBManager dbManager;

    @FXML
    private void search(KeyEvent keyEvent) {
        boolean isEscape = keyEvent.getCode() == KeyCode.ESCAPE;
        String rawInput = searchBar.getText().trim();
        if (isEscape || rawInput.length() == 0) {
            return;
        }
        System.out.println(rawInput);

        final ObservableList<FileRecord> query = FXCollections.observableArrayList();
        final Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                query.addAll(processQuery(rawInput));
                return null;
            }
        };
        task.setOnSucceeded(e -> tableView.setItems(query));
        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    private ObservableList<FileRecord> processQuery(String rawInput) {
        final List<FileRecord> list = new ArrayList<>();

        try {
            final String query = Utils.prepateExpression(rawInput);
            for (Map.Entry<String, String> e : dbManager.searchWithRegex(query).entrySet()) {
                list.add(new FileRecord(e.getValue(), e.getKey()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return FXCollections.observableArrayList(list);
    }

    public void updateScrollSize(Number newVal) {
        scrollPane.prefViewportHeightProperty().setValue(newVal);
    }

    public void timeToGo(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ESCAPE) {
            System.out.println("escape got called");
        }
    }

    public void openConfigWindow(MouseEvent mouseEvent) throws IOException {
        if (mouseEvent.getButton() != MouseButton.PRIMARY) {
            return;
        }
        final FXMLLoader configLoader = new FXMLLoader(getClass().getResource("/view/configuration_menu.fxml"));
        final Parent parent = configLoader.load();
        final ConfigurationController controller = configLoader.getController();
        controller.setFilesCounter(filesCounter);
        controller.setTableView(tableView);
        final Scene secondScene = new Scene(parent);

        // New window (Stage)
        final Stage newWindow = new Stage();
        newWindow.setTitle("Configuration");
        newWindow.setScene(secondScene);
        newWindow.show();
    }

    public void getRowOptions(MouseEvent mouseEvent) {
        if (tableView.getSelectionModel().getSelectedItem() == null) {
            return;
        }
        final String file = tableView.getSelectionModel().getSelectedItem().pathProperty().getValue();
        if (mouseEvent.getButton() == MouseButton.PRIMARY && mouseEvent.getClickCount() == 2) {
            Utils.openFileWithExplorer(file);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        final ContextMenu contextMenu = new ContextMenu();

        final MenuItem open = new MenuItem("Open");
        open.setOnAction(e -> {
            final String file = tableView.getSelectionModel().getSelectedItem().pathProperty().getValue();
            Utils.openFileWithExplorer(file);
        });
        final MenuItem openPath = new MenuItem("Explore path");
        openPath.setOnAction(e -> {
            final String file = tableView.getSelectionModel().getSelectedItem().pathProperty().getValue();
            Utils.openPath(file);
        });
        final MenuItem copyPath = new MenuItem("Copy path");
        copyPath.setOnAction(e -> {
            final String file = tableView.getSelectionModel().getSelectedItem().pathProperty().getValue();
            Utils.copyPathToClipboard(file);
        });

        contextMenu.getItems().addAll(open, openPath, copyPath);
        tableView.setContextMenu(contextMenu);
        tableView.setEditable(false);

        final DatabaseServer server = new DatabaseServer(
                DBCredentials.USERNAME.val(),
                DBCredentials.PASSWD.val(),
                DBCredentials.DATABASE.val()
        );

        try {
            server.startServer();
            dbManager = DBManager.newInstance(server.getConnection());
            dbManager.loadSchema();
        } catch (Exception e) {
            throw new IllegalStateException("Cannot start");
        }
    }
}
