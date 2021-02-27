package io.github.horizonchaser;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

/**
 * Login dialog and new user dialog controller class
 * @author Horizon
 */
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
    private Label currUsingField;

    @FXML
    private Button loginBrowseButton;

    private boolean isStrong = false, isRepeatCorrect = false;

    @FXML
    void loginBrowseButtonAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("."));
        fileChooser.setTitle("Save the database at...");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JPK Database File", "*.JPK"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );
        File chosenFile = fileChooser.showOpenDialog(loginBrowseButton.getScene().getWindow());

        if(chosenFile != null) {
            Main.currSaveFilePath = chosenFile.getAbsolutePath();
            currUsingField.setText(chosenFile.getAbsolutePath());
        } else {
            return;
        }

        try {
            FileUtil.loadAndParseDB(chosenFile);
        } catch (JPKFileException j) {
            Main.currSaveFilePath = "";
            currUsingField.setText("");

            Alert loadFailedAlert = new Alert(Alert.AlertType.ERROR);
            loadFailedAlert.setTitle("Failed to load selected database file");
            loadFailedAlert.setHeaderText("Due to following reason, JPK failed to load "
                    + chosenFile.getAbsolutePath() + "as database.");
            loadFailedAlert.setContentText(j.getLocalizedMessage());
            loadFailedAlert.showAndWait();
        }
    }

    @FXML
    void loginButtonAction(ActionEvent event) {
        Alert alert;
        String username = usernameField.getText(), password = passwordField.getText();
        byte[] hash = CryptoUtil.calUserLoginHash(username, password);
        if (Arrays.equals(hash, Main.userHash)) {
            Main.dataKey = CryptoUtil.calDataKey(username, password);

            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Successfully logged in");
            alert.setHeaderText("You have successfully logged in at " + sdf.format(date));
            alert.setContentText("Please take care about your passwords, and remember to save when exit.");
            passwordField.setText("");

            ((Stage) loginButton.getScene().getWindow()).close();

        } else {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Password or Username Incorrect");
            alert.setContentText("Failed to login.");
        }
        alert.showAndWait();
    }

    public void showPasswordComplexity() {
        if (newPasswordField.getText().matches(CommonDefinition.PASSWORD_PATTERN)) {
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
            return;
        }

        if(!isRepeatCorrect) {
            Alert notCorrectAlert = new Alert(Alert.AlertType.WARNING);
            notCorrectAlert.setTitle("Repeat password not matched");
            notCorrectAlert.setHeaderText("Your repeat password doesn't consist with first one. Check it :)");
            notCorrectAlert.showAndWait();
            return;
        }

        Main.setUserHash(CryptoUtil.calUserLoginHash(newUsernameField.getText(), newPasswordField.getText()));
        newPasswordField.setText("");
        repeatField.setText("");

        System.out.println(CryptoUtil.bytesToHex(Main.userHash));

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

        FileUtil.saveDBToFile(Main.recordEntryList, selectedFile.getAbsolutePath());
        Alert createdAlert = new Alert(Alert.AlertType.INFORMATION, "New database created as "
                + selectedFile.getAbsolutePath());
        createdAlert.showAndWait();

        ((Stage) newUserPane.getScene().getWindow()).close();
    }

    public void newUserCancel() {
        Stage currStage = (Stage) newCancelButton.getScene().getWindow();
        currStage.close();
    }
}