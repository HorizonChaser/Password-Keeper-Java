package io.github.horizonchaser;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.util.Arrays;

public class Main extends Application {

    private transient static byte[] key;
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
        }


        Parent root = FXMLLoader.load(getClass().getResource("loginConsole.fxml"));
        primaryStage.setTitle("Login");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

        System.out.println(Arrays.toString(Main.key));
    }
}
