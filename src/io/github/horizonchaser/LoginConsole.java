package io.github.horizonchaser;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class LoginConsole {

    @FXML
    private TextField usernameField;

    @FXML
    private Label UserLabel;

    @FXML
    private Button loginButton;

    @FXML
    private PasswordField passwordField;

    @FXML
    void loginButtonAction(ActionEvent event) {
        Alert alert;
        if(passwordField.getText().equals("TEST") && usernameField.getText().equals("TEST")) {
            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Successfully Logged in");
            alert.setContentText("Logged in.");
            passwordField.setText("");

        } else {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Password or Username Incorrect");
            alert.setContentText("Failed to login.");
        }
        alert.showAndWait();

    }

}