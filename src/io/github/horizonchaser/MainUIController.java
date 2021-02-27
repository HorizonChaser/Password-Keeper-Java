package io.github.horizonchaser;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

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
    private Label entryCntLabel;

    @FXML
    private Label saveIndicateLabel;

    private boolean hasSaved = false;

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

    public void refreshEntryCntLabel(int val) {
        entryCntLabel.setText(val + "entry(s)");
    }

    public void refreshSaveIndicatorLabel(boolean save) {
        hasSaved = save;
        if(hasSaved) {
            saveIndicateLabel.setText("Saved");
            saveIndicateLabel.setTextFill(Color.GREEN);
        }else {
            saveIndicateLabel.setText("Unsaved");
            saveIndicateLabel.setTextFill(Color.RED);
        }
    }
}
