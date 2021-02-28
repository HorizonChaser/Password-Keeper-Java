package io.github.horizonchaser;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import static io.github.horizonchaser.Main.recordEntryList;


public class MainUIController extends Main {

    public  ObservableList<RecordEntry> entryObservableList;

    @FXML
    public TableView<RecordEntry> mainTable;

    private TableColumn domainColumn;

    private TableColumn usernameColumn;

    private TableColumn noteColumn;

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
    public Label entryCntLabel = new Label();

    @FXML
    public Label saveIndicateLabel;

    private boolean hasSaved = false;

    @FXML
    void onLoadDBAction(ActionEvent event) {
    }

    @FXML
    void onSaveAction(ActionEvent event) {
        FileUtil.saveDBToFile(recordEntryList, Main.currSaveFilePath);
        entryCntLabel.setText(recordEntryList.size() + " entry(s)!");
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
        if (hasSaved) {
            saveIndicateLabel.setText("Saved");
            saveIndicateLabel.setTextFill(Color.GREEN);
        } else {
            saveIndicateLabel.setText("Unsaved");
            saveIndicateLabel.setTextFill(Color.RED);
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        AnchorPane mainUIPane = FXMLLoader.load(getClass().getResource("mainUI.fxml"));
        Scene scene = new Scene(mainUIPane);
        primaryStage.setTitle("Java Password Keeper");
        primaryStage.setScene(scene);

        primaryStage.show();
    }

    @FXML
    public void initialize(){
        entryObservableList = FXCollections.observableArrayList();

        domainColumn = new TableColumn("Domain");
        domainColumn.setMinWidth(200);
        domainColumn.setCellValueFactory(
                new PropertyValueFactory<>("domain")
        );

        usernameColumn = new TableColumn("Username");
        usernameColumn.setMinWidth(200);
        usernameColumn.setCellValueFactory(
                new PropertyValueFactory<>("username")
        );
        noteColumn = new TableColumn("Note");
        noteColumn.setMinWidth(200);
        noteColumn.setCellValueFactory(
                new PropertyValueFactory<>("note")
        );

        entryObservableList.addAll(recordEntryList);
        mainTable.setItems(entryObservableList);
        mainTable.getColumns().addAll(domainColumn, usernameColumn, noteColumn);

        entryCntLabel.setText(Main.recordEntryList.size() + " entry(s)");
        refreshSaveIndicatorLabel(true);
    }
}
