package gha.gha.FrontEnd;


import gha.gha.BackEnd.GameLogic;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Main Window for the application
 */
public class Game extends Application {
    Scene menuScene;
    Stage stage;
    @FXML
    Button startButton;

    private ScheduledExecutorService scheduledExecutorService;

    /**
     * @param primaryStage the primary stage for this application, onto which
     * the application scene can be set.
     * Applications may create other stages, if needed, but they will not be
     * primary stages.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;

        FXMLLoader MenuLoader = new FXMLLoader(Game.class.getResource("/MainMenu.fxml"));
        menuScene = new Scene(MenuLoader.load(), 640, 640);
        stage.setScene(menuScene);
        stage.setTitle("Task Tycoon!");
        stage.show();

        stage.setOnCloseRequest(event -> {
            Platform.exit();
            System.exit(0);
        });

    }

    /**
     *
     * @param actionEvent
     * The action listener listens for a click event on the start button
     * When stage is closed there is an autosave foing on, that saves the current state of loaded files.
     */
    @FXML
    public void startGame(ActionEvent actionEvent) throws IOException {
        FXMLLoader GameLoader = new FXMLLoader(Game.class.getResource("/GameWindow.fxml"));
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        Parent root = GameLoader.load();
        stage = (Stage) startButton.getScene().getWindow();
        stage.setScene(new Scene(root, 640, 640));
        stage.show();


        stage.setOnCloseRequest(t -> {
            GameLogic.getInstance().CloseApplication();
            Platform.exit();
            System.exit(0);
        });
    }


    /**
     * Stops the Java FX application
     */
    @Override
    public void stop() throws Exception {
        super.stop();
        scheduledExecutorService.shutdownNow();
    }

    public static void main(String[] args) {
        launch();
    }

}
