package io.github.horizonchaser;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class LoginConsoleController extends Main {

    @FXML
    private Button newCancelButton;

    @FXML
    private TextField usernameField;

    @FXML
    private Label UserLabel;

    @FXML
    private Button loginButton;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField newPasswordField;

    @FXML
    private PasswordField repeatField;

    @FXML
    private TextField newUsernameField;

    @FXML
    private Label passwordStrengthIndicator;

    @FXML
    private Label repeatPasswordIndicator;

    @FXML
    private Button newConfirmButton;

    @FXML
    private Pane newUserPane;

    @FXML
    private TextField currUsingField;

    @FXML
    private Button loginBrowseButton;

    private boolean isStrong = false, isRepeatCorrect = false;

    @FXML
    void loginButtonAction(ActionEvent event) {
        Alert alert;
        String username = passwordField.getText(), password = usernameField.getText();
        if (passwordField.getText().equals("TEST") && usernameField.getText().equals("TEST")) {
            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Successfully Logged in");
            alert.setContentText("Logged in.");
            passwordField.setText("");

            Main.setKey("SUCC".getBytes());

        } else {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Password or Username Incorrect");
            alert.setContentText("Failed to login.");
        }
        alert.showAndWait();
    }

    public void showPasswordComplexity() {
        if (newPasswordField.getText().matches(CommonDefinition.passwordPattern)) {
            passwordStrengthIndicator.setText("Strong");
            passwordStrengthIndicator.setTextFill(Color.GREEN);
            isStrong = true;
        } else {
            passwordStrengthIndicator.setText("Weak");
            passwordStrengthIndicator.setTextFill(Color.RED);
            isStrong = false;
        }
        checkRepeat();
    }

    public void checkRepeat() {
        if (newPasswordField.getText().equals(repeatField.getText())) {
            repeatPasswordIndicator.setText("Matched");
            repeatPasswordIndicator.setTextFill(Color.GREEN);
            isRepeatCorrect = true;
        } else {
            repeatPasswordIndicator.setText("Mismatch");
            repeatPasswordIndicator.setTextFill(Color.RED);
            isRepeatCorrect = false;
        }
    }

    public void newUserConfirm() {
        showPasswordComplexity();
        checkRepeat();

        if(!isStrong) {
            Alert notStrongAlert = new Alert(Alert.AlertType.WARNING);
            notStrongAlert.setTitle("Weak password detected");
            notStrongAlert.setHeaderText("This password is used to protect your other passwords, so it needs to be especially strong.");
            notStrongAlert.setContentText("In short, it should meet following requirements:"
                    + "\n    1. At least 8 characters long"
                    + "\n    2. At least contains one uppercase letter, one lowercase letter, one digit and one symbol each");
            notStrongAlert.showAndWait();
        }

        if(!isRepeatCorrect) {
            Alert notCorrectAlert = new Alert(Alert.AlertType.WARNING);
            notCorrectAlert.setTitle("Repeat password not matched");
            notCorrectAlert.setHeaderText("Your repeat password doesn't consist with first one. Check it :)");
            notCorrectAlert.showAndWait();
        }

        Main.setKey(CryptoUtil.calUserLoginHash(newUsernameField.getText(), newPasswordField.getText()));
        newPasswordField.setText("");
        repeatField.setText("");

        System.out.println(CryptoUtil.bytes2Hex(Main.key));

        FileChooser saveFileChooser = new FileChooser();
        saveFileChooser.setInitialDirectory(new File("."));
        saveFileChooser.setTitle("Save the database at...");
        saveFileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JPK Database File", "*.JPK"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );
        File selectedFile = saveFileChooser.showSaveDialog(newUserPane.getScene().getWindow());

        if(selectedFile == null) {
            return;
        }

        FileUtil.initializeDB(selectedFile.getAbsolutePath(), Main.key);
        new Alert(Alert.AlertType.INFORMATION, "New database created as "
                + selectedFile.getAbsolutePath()).showAndWait();
    }

    public void newUserCancel() {
        Stage currStage = (Stage) newCancelButton.getScene().getWindow();
        currStage.close();
    }
}