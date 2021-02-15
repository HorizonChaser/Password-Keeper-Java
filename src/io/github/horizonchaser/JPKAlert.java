package io.github.horizonchaser;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class JPKAlert extends Alert {

    public JPKAlert(AlertType alertType, String contentText, ButtonType... buttons) {
        super(alertType, contentText, buttons);
    }
}
