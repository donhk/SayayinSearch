package ninja.donhk.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class Search {

    @FXML
    public TableView tableView;
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
}
