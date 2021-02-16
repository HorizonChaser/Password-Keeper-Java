package io.github.horizonchaser;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.io.File;
import java.util.Arrays;
import java.util.Optional;

public class Main extends Application {

    transient static byte[] key;
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

        if(!defaultSaveFile.exists()) {
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

            if(result.isEmpty()) {
                System.exit(-1);
            }

            if(result.get() == newFileButton) {
                Parent newFile = FXMLLoader.load(getClass().getResource("newUserDialog.fxml"));
                Stage newFileStage = new Stage();
                newFileStage.setTitle("Initialize new user database");

                newFileStage.setScene(new Scene(newFile));
                newFileStage.showAndWait();

            } else if(result.get() == chooseFileButton) {
                System.out.println("CHOOSE MANUALLY");
            } else {
                System.exit(0);
            }

        }


        Parent root = FXMLLoader.load(getClass().getResource("loginConsole.fxml"));
        primaryStage.setTitle("Login");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

        System.out.println(Arrays.toString(Main.key));
    }
}
