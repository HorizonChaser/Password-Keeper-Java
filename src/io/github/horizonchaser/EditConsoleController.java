package io.github.horizonchaser;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class EditConsoleController extends Main {

    @FXML
    private TextField passwordTextField;

    @FXML
    private CheckBox showPasswordCheckBox;

    @FXML
    private Button cancelButton;

    @FXML
    private TextField domainTextField;

    @FXML
    private TextField noteTextField;

    @FXML
    private Button saveButton;

    @FXML
    private TextField usernameTextField;
    private RecordEntry currEditEntry;

    @FXML
    void onPasswordCheckBoxAction(ActionEvent event) {
        if (showPasswordCheckBox.isSelected()) {
            passwordTextField.setText(currEditEntry.getPassword());
            showPasswordCheckBox.setDisable(true);
        } else {
            passwordTextField.setText("********");
        }
    }

    @FXML
    void onSaveAction(ActionEvent event) {
        /*
        if (Main.recordEntryList.contains(currEditEntry)) {
            Alert duplicateAlert = new Alert(Alert.AlertType.ERROR);
            duplicateAlert.setTitle("Duplicate Account Detected");
            duplicateAlert.setHeaderText("JPK has found an entry with same username under same domain, which is not allowed...");
            duplicateAlert.setContentText("To solve this, please check the previous account and determine which to save.");
            duplicateAlert.showAndWait();
            return;
        }
        */
        currEditEntry = new RecordEntry(domainTextField.getText(), usernameTextField.getText(),
                passwordTextField.getText(), noteTextField.getText());
        if(!showPasswordCheckBox.isSelected()) {
            currEditEntry.setPassword(MainUIController.currSelect.getPassword());
        }

        //FIXME modification on MainUIController.currSelect WON'T affect Main.recordEntryList
        //PRIORITY 0
        MainUIController.currSelect = currEditEntry;
        currEditEntry = null;
        ((Stage)passwordTextField.getScene().getWindow()).close();
    }

    @FXML
    void onCancelAction(ActionEvent event) {

    }


    @Override
    public void start(Stage primaryStage) throws Exception {

        AnchorPane editConsolePane = FXMLLoader.load(getClass().getResource("editConsole.fxml"));
        Scene scene = new Scene(editConsolePane);
        primaryStage.setTitle("Edit Selected Entry");
        primaryStage.setScene(scene);

        primaryStage.show();
    }

    @FXML
    public void initialize() {
        currEditEntry = MainUIController.currSelect.getCopy();

        domainTextField.setText(currEditEntry.getDomain());
        domainTextField.editableProperty().setValue(false);

        usernameTextField.setText(currEditEntry.getUsername());
        usernameTextField.editableProperty().setValue(false);

        passwordTextField.setText("********");
        noteTextField.setText(currEditEntry.getNote());
    }
}
