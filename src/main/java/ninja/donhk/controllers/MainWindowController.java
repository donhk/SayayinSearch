package ninja.donhk.controllers;

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
import ninja.donhk.utils.Utils;

import java.io.IOException;
import java.net.URL;
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
            System.out.println(file);
            Utils.openFileWithExplorer(file);
        });
        final MenuItem openPath = new MenuItem("Explore path");
        openPath.setOnAction(e -> {
            final String file = tableView.getSelectionModel().getSelectedItem().pathProperty().getValue();
            System.out.println(file);
            Utils.openPath(file);
        });
        final MenuItem copyPath = new MenuItem("Copy path");
        copyPath.setOnAction(e -> {
            final String file = tableView.getSelectionModel().getSelectedItem().pathProperty().getValue();
            System.out.println(file);
            Utils.copyPathToClipboard(file);
        });

        contextMenu.getItems().addAll(open, openPath, copyPath);
        tableView.setContextMenu(contextMenu);
    }
}
