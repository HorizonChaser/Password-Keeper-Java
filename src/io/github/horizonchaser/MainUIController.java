package io.github.horizonchaser;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;

public class MainUIController {


    @FXML
    private Button resButton;

    @FXML
    private MenuItem deleteEntryMenuItem;

    @FXML
    private MenuItem loadDBMenuItem;

    @FXML
    private Button addEntryButton;

    @FXML
    private MenuItem aboutMenuItem;

    @FXML
    private MenuItem closeMenuItem;

    @FXML
    private MenuItem saveMenuItem;

    @FXML
    private MenuItem addNewEntryMenuItem;

    @FXML
    private Button deleteEntryButton;

    @FXML
    private Button loadDBButton;

    @FXML
    private Button saveButton;

    @FXML
    private Button editEntryButton;

    @FXML
    void onLoadDBAction(ActionEvent event) {

    }

    @FXML
    void onSaveAction(ActionEvent event) {
        FileUtil.saveDBToFile(Main.recordEntryList, Main.currSaveFilePath);
    }

    @FXML
    void onCloseMenuItem(ActionEvent event) {

    }

    @FXML
    void onAddNewEntry(ActionEvent event) {

    }

    @FXML
    void onDeleteEntry(ActionEvent event) {

    }

    @FXML
    void onAboutMenuItem(ActionEvent event) {

    }

    @FXML
    void onAddEntryAction(ActionEvent event) {

    }

    @FXML
    void onEditAction(ActionEvent event) {

    }

    @FXML
    void onDeleteEntryAction(ActionEvent event) {

    }
}
