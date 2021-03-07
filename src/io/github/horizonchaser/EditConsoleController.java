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
        currEditEntry = new RecordEntry(domainTextField.getText(), usernameTextField.getText(),
                passwordTextField.getText(), noteTextField.getText());
        if (!showPasswordCheckBox.isSelected()) {
            currEditEntry.setPassword(MainUIController.currSelect.getPassword());
        }

        Main.recordEntryList.set(Main.recordEntryList.indexOf(MainUIController.currSelect), currEditEntry);
        currEditEntry = null;

        MainUIController.entryObservableList.clear();
        MainUIController.entryObservableList.addAll(Main.recordEntryList);
        ((Stage) passwordTextField.getScene().getWindow()).close();
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
        currEditEntry = MainUIController.currSelect;

        domainTextField.setText(currEditEntry.getDomain());
        domainTextField.editableProperty().setValue(false);

        usernameTextField.setText(currEditEntry.getUsername());
        usernameTextField.editableProperty().setValue(false);

        passwordTextField.setText("********");
        noteTextField.setText(currEditEntry.getNote());
    }
}
