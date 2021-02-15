package io.github.horizonchaser;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class LoginConsoleController {

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
    void loginButtonAction(ActionEvent event) {
        Alert alert;
        String username = passwordField.getText(), password = usernameField.getText();
        if(passwordField.getText().equals("TEST") && usernameField.getText().equals("TEST")) {
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
        if(newPasswordField.getText().matches(CommonDefinition.passwordPattern)) {
            passwordStrengthIndicator.setText("Strong");
            passwordStrengthIndicator.setTextFill(Color.GREEN);
        } else {
            passwordStrengthIndicator.setText("Weak");
            passwordStrengthIndicator.setTextFill(Color.RED);
        }
    }

    public void checkRepeat() {
        if(newPasswordField.getText().equals(repeatField.getText())) {
            repeatPasswordIndicator.setText("Matched");
            repeatPasswordIndicator.setTextFill(Color.GREEN);
        } else {
            repeatPasswordIndicator.setText("Mismatch");
            repeatPasswordIndicator.setTextFill(Color.RED);
        }
    }
}