package io.github.horizonchaser;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Horizon
 */
public class Main extends Application {

    public transient static String currSaveFilePath = "";
    public transient static List<RecordEntry> recordEntryList = new ArrayList<>();
    protected transient static byte[] userHash = new byte[32];
    protected transient static byte[] dataKey = new byte[32];
    protected transient static int currEntryCnt = 0;

    public static void setUserHash(byte[] userHash) {
        Main.userHash = userHash;
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static File existingDBFileChooseDialog(Window window) {
        FileChooser currSaveFileChooser = new FileChooser();
        currSaveFileChooser.setInitialDirectory(new File("."));
        currSaveFileChooser.setTitle("Load an existing database file...");
        currSaveFileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JPK Database File", "*.JPK"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );
        return currSaveFileChooser.showOpenDialog(window);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        File defaultSaveFile = new File(CommonDefinition.DEFAULT_SAVE_NAME);

        //TEST
        recordEntryList.add(new RecordEntry("dom1", "test01", "123456", "note01"));
        recordEntryList.add(new RecordEntry("dom2", "test02", "6asdqdG", "note02"));

        if (defaultSaveFile.exists()) {
            Alert loadDefaultOrChoose = new Alert(Alert.AlertType.INFORMATION);
            loadDefaultOrChoose.setTitle("Default Database File Detected");
            loadDefaultOrChoose.setHeaderText("JPK has detected default database " + CommonDefinition.DEFAULT_SAVE_NAME + "in current dir.");
            loadDefaultOrChoose.setContentText("So would you choose your JPK save file manually or load this default one?\n");

            ButtonType loadDefaultButton = new ButtonType("Load Default Button");
            ButtonType chooseFileButton = new ButtonType("Choose Manually");
            ButtonType exitButton = new ButtonType("Exit", ButtonBar.ButtonData.CANCEL_CLOSE);
            loadDefaultOrChoose.getButtonTypes().addAll(loadDefaultButton, chooseFileButton, exitButton);
            Optional<ButtonType> result = loadDefaultOrChoose.showAndWait();

            if (result.isEmpty()) {
                System.exit(-1);
            }

            if (result.get() == loadDefaultButton) {
                currSaveFilePath = defaultSaveFile.getAbsolutePath();
            } else if (result.get() == chooseFileButton) {
                currSaveFilePath = existingDBFileChooseDialog(loadDefaultOrChoose.getOwner()).getAbsolutePath();
            } else {
                System.exit(0);
            }
        } else {
            Alert newSaveOrChoose = new Alert(Alert.AlertType.INFORMATION);
            newSaveOrChoose.setTitle("Default Database File Not Found");
            newSaveOrChoose.setHeaderText("JPK couldn't find default save file " + CommonDefinition.DEFAULT_SAVE_NAME + " in current dir.");
            newSaveOrChoose.setContentText("So would you choose your JPK save file manually or initialize a new one?\n");

            ButtonType newFileButton = new ButtonType("New Save File");
            ButtonType chooseFileButton = new ButtonType("Choose Manually");
            ButtonType exitButton = new ButtonType("Exit", ButtonBar.ButtonData.CANCEL_CLOSE);
            newSaveOrChoose.getButtonTypes().setAll(newFileButton, chooseFileButton, exitButton);
            Optional<ButtonType> result = newSaveOrChoose.showAndWait();

            if (result.isEmpty()) {
                System.exit(-1);
            }

            if (result.get() == newFileButton) {
                Parent newFile = FXMLLoader.load(getClass().getResource("newUserDialog.fxml"));
                Stage newFileStage = new Stage();
                newFileStage.setTitle("Initialize new user database");
                newFileStage.setScene(new Scene(newFile));
                newFileStage.showAndWait();
            } else if (result.get() == chooseFileButton) {
                System.out.println("CHOOSE MANUALLY");
                File selectedSaveFile = existingDBFileChooseDialog(newSaveOrChoose.getOwner());
                currSaveFilePath = selectedSaveFile.getAbsolutePath();
            } else {
                System.exit(0);
            }
        }

        File currSaveFile = new File(currSaveFilePath);
        try {
            FileUtil.loadAndParseDB(currSaveFile);
        } catch (JPKFileException j) {
            Alert loadFailedAlert = new Alert(Alert.AlertType.ERROR);
            loadFailedAlert.setTitle("Failed to load selected database file");
            loadFailedAlert.setHeaderText("Due to following reason, JPK failed to load "
                    + currSaveFile.getAbsolutePath() + "as database.");
            loadFailedAlert.setContentText(j.getLocalizedMessage());
            loadFailedAlert.showAndWait();

            //XXX Maybe unsafe - mem leak
            primaryStage.close();
            Platform.runLater(() -> {
                try {
                    new Main().start(new Stage());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            return;
        }

        AnchorPane loginConsolePane = FXMLLoader.load(getClass().getResource("loginConsole.fxml"));
        for (Node node : loginConsolePane.getChildren()) {
            if (node instanceof Label) {
                Label label = (Label) node;
                String id = label.getId();
                if (id != null && id.equals("currUsingField")) {
                    label.setText(currSaveFile.getAbsolutePath());
                    break;
                }
            }
        }
        Stage loginStage = new Stage();
        primaryStage.hide();
        loginStage.setTitle("Login");
        loginStage.setScene(new Scene(loginConsolePane));
        loginStage.showAndWait();

        try {
            MainUIController mainUIController = new MainUIController();
            mainUIController.start(primaryStage);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        /*
        AnchorPane mainUIPane = FXMLLoader.load(getClass().getResource("mainUI.fxml"));
        Stage mainUIStage = new Stage();
        mainUIStage.setTitle("Java Password Keeper");
        mainUIStage.setScene(new Scene(mainUIPane));

        for (Node node : mainUIPane.getChildren()) {
            if (node instanceof Label) {
                Label label = (Label)node;
                String id = label.getId();
                if(id!= null && id.equals("entryCntLabel")) {
                    label.setText(recordEntryList.size() + " entry(s)");
                    break;
                }
            }
        }
        mainUIStage.show();
        */
    }
}
