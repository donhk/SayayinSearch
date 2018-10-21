package ninja.donhk.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
import javafx.stage.WindowEvent;
import ninja.donhk.model.FileRecord;
import ninja.donhk.services.database.DBManager;

import java.io.File;
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

    private DBManager dbManager = DBManager.getInstance();

    @FXML
    private void search(KeyEvent keyEvent) {
        boolean isEscape = keyEvent.getCode() == KeyCode.ESCAPE;
        String rawInput = searchBar.getText().trim();
        if (isEscape || rawInput.length() == 0) {
            return;
        }
        System.out.println(rawInput);
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
        final FXMLLoader configLoader = new FXMLLoader(getClass().getResource("/view/configuration_menu.fxml"));
        final Parent parent = configLoader.load();
        final ConfigurationController controller = configLoader.getController();
        controller.setFilesCounter(filesCounter);
        controller.setTableView(tableView);
        final Scene secondScene = new Scene(parent);

        // New window (Stage)
        final Stage newWindow = new Stage();
        newWindow.setTitle("Second Stage");
        newWindow.setScene(secondScene);
        newWindow.show();
    }


    public void getRowOptions(MouseEvent mouseEvent) {
        final String file = tableView.getSelectionModel().getSelectedItem().pathProperty().getValue();
        if (mouseEvent.getButton() == MouseButton.SECONDARY) {
            showItemOptions(new File(file));
        } else if (mouseEvent.getButton() == MouseButton.PRIMARY && mouseEvent.getClickCount() == 2) {
            openFile(new File(file));
        }
    }

    private void showItemOptions(File target) {
        System.out.println("show menu! for " + target.getAbsolutePath());

    }

    private void openFile(File target) {
        System.out.println("open " + target.getAbsolutePath());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        final ContextMenu contextMenu = new ContextMenu();

        final MenuItem item1 = new MenuItem("Open");
        item1.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                System.out.println("Open");
            }
        });
        final MenuItem item2 = new MenuItem("Open path");
        item2.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                System.out.println("Open path");
            }
        });
        final MenuItem item3 = new MenuItem("Copy path");
        item2.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                System.out.println("copy path");
            }
        });
        contextMenu.getItems().addAll(item1, item2, item3);

        tableView.setContextMenu(contextMenu);
    }
}
