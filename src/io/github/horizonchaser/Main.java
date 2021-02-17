package io.github.horizonchaser;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.Optional;

public class Main extends Application {

    public transient static String currSaveFilePath = "";
    protected transient static byte[] key = new byte[32];
    protected transient static int currEntryCnt = 0;
    private transient static boolean isLoggedIn = false;

    public static void setIsLoggedIn(boolean isLoggedIn) {
        Main.isLoggedIn = isLoggedIn;
    }

    public static void setKey(byte[] key) {
        Main.key = key;
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        File defaultSaveFile = new File(CommonDefinition.defaultSaveName);

        if (!defaultSaveFile.exists()) {
            //TODO ask new or choose manually
            Alert newSaveOrChoose = new Alert(Alert.AlertType.INFORMATION);
            newSaveOrChoose.setTitle("Default save file not found...");
            newSaveOrChoose.setHeaderText("JPK couldn't find default save file " + CommonDefinition.defaultSaveName + " in current dir.");
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
                FileChooser currSaveFileChooser = new FileChooser();
                currSaveFileChooser.setInitialDirectory(new File("."));
                currSaveFileChooser.setTitle("Load an existing database file...");
                currSaveFileChooser.getExtensionFilters().addAll(
                        new FileChooser.ExtensionFilter("JPK Database File", "*.JPK"),
                        new FileChooser.ExtensionFilter("All Files", "*.*")
                );
                File selectedSaveFile = currSaveFileChooser.showOpenDialog(newSaveOrChoose.getOwner());
                currSaveFilePath = selectedSaveFile.getAbsolutePath();
            } else {
                System.exit(0);
            }
        }

        File currSaveFile = new File(currSaveFilePath);
        try {
            FileUtil.loadUserHashFromDB(currSaveFile);
        } catch (JPKFileException j) {
            Alert loadFailedAlert = new Alert(Alert.AlertType.ERROR);
            loadFailedAlert.setTitle("Failed to load selected database file");
            loadFailedAlert.setHeaderText("Due to following reason, JPK failed to load "
                    + currSaveFile.getAbsolutePath() + "as database.");
            loadFailedAlert.setContentText(j.getLocalizedMessage());
            loadFailedAlert.showAndWait();

            System.exit(-2);
        }

        AnchorPane root = FXMLLoader.load(getClass().getResource("loginConsole.fxml"));
        for(Node node : root.getChildren()) {
            if(node instanceof TextArea) {
                ((TextArea) node).setText(currSaveFilePath);
            }
        }

        primaryStage.setTitle("Login");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

    }
}
