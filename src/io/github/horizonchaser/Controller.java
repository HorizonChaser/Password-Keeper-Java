package io.github.horizonchaser;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;

public class Controller {

    @FXML
    private BorderPane mainBorderPane;

    @FXML
    private MenuItem aboutMenuItem;

    @FXML
    void onAboutMenuItemAction(ActionEvent event) {
        Alert aboutAlert = new Alert(Alert.AlertType.INFORMATION);
        aboutAlert.setTitle("About");
        aboutAlert.setHeaderText("Java14, JavaFX and IDEA integration test");

        aboutAlert.showAndWait();
    }

}

