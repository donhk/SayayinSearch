package ninja.donhk.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import ninja.donhk.model.FileRecord;
import ninja.donhk.services.database.DBManager;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainWindowController {

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

    public void refreshTable(MouseEvent mouseEvent) {
        tableView.setItems(getInitData());
        tableView.setEditable(false);
        try {
            filesCounter.setText(dbManager.getAllFiles().size() + " files indexed");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private ObservableList<FileRecord> getInitData() {
        List<FileRecord> list = new ArrayList<>();
        try {
            for (Map.Entry<String, String> e : dbManager.getAllFiles().entrySet()) {
                list.add(new FileRecord(e.getValue(), e.getKey()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return FXCollections.observableArrayList(list);
    }

    public void openConfigWindow(MouseEvent mouseEvent) throws IOException {
        final FXMLLoader configLoader = new FXMLLoader(getClass().getResource("/view/configuration_menu.fxml"));
        final Parent parent = configLoader.load();

        final Scene secondScene = new Scene(parent);

        // New window (Stage)
        final Stage newWindow = new Stage();
        newWindow.setTitle("Second Stage");
        newWindow.setScene(secondScene);
        newWindow.show();
    }


}
