package io.github.horizonchaser;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class NewEntryConsoleController extends Main {

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

    private transient String password;
    private transient boolean isPasswordSet = false;

    @FXML
    void onPasswordCheckBoxAction(ActionEvent event) {
        if (!showPasswordCheckBox.isSelected()) {
            passwordTextField.setText("********");
            if (isPasswordSet && passwordTextField.getText() != null) {
                password = passwordTextField.getText();
                isPasswordSet = true;
            }
        } else {
            passwordTextField.setText(password);
        }
    }

    @FXML
    void onSaveAction(ActionEvent event) {
        if (domainTextField.getText() == null || usernameTextField.getText() == null || !isPasswordSet || noteTextField.getText() == null){
            Alert notFinishedAlert = new Alert(Alert.AlertType.ERROR);
            notFinishedAlert.setTitle("Necessary field(s) are missing");
            notFinishedAlert.setHeaderText("At least one necessary field is missing...");
            notFinishedAlert.setContentText("Please check and fill the content before save the new entry.");
            notFinishedAlert.showAndWait();

            return;
        }

        RecordEntry newEntry = new RecordEntry(domainTextField.getText(), usernameTextField.getText(), passwordTextField.getText(), noteTextField.getText());
        for(RecordEntry curr : Main.recordEntryList) {
            if (curr.hashCode() == newEntry.hashCode()) {
                //TODO Duplicate Alert

                return;
            }
        }

        Main.recordEntryList.add(newEntry);
    }

    @FXML
    void onCancelAction(ActionEvent event) {
        Stage currStage = (Stage) cancelButton.getScene().getWindow();
        currStage.close();
    }


    @Override
    public void start(Stage primaryStage) throws Exception {

        AnchorPane editConsolePane = FXMLLoader.load(getClass().getResource("newEntryConsole.fxml"));
        Scene scene = new Scene(editConsolePane);
        primaryStage.setTitle("Add New Entry");
        primaryStage.setScene(scene);

        primaryStage.show();
    }

    @FXML
    public void initialize() {
        showPasswordCheckBox.setSelected(true);
    }
}
