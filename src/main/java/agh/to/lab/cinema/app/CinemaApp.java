package agh.to.lab.cinema.app;

import agh.to.lab.cinema.model.users.CinemaUser;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class CinemaApp extends Application {
    private static Stage primaryStage;
    private static CinemaUser cinemaUser;

    @Override
    public void start(Stage primaryStage) {
        CinemaApp.primaryStage = primaryStage;
        loadView("views/login.fxml");
    }

    public static void loadView(String fxmlPath) {
        try {
            // load layout from FXML file
            var loader = new FXMLLoader();
            loader.setLocation(CinemaApp.class.getClassLoader().getResource(fxmlPath));
            BorderPane view = loader.load();

            // add layout to a scene and show them all
            var scene = new Scene(view);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Cinema app");
            primaryStage.minWidthProperty().bind(view.minWidthProperty());
            primaryStage.minHeightProperty().bind(view.minHeightProperty());
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setCinemaUser(CinemaUser cinemaUser) {
        CinemaApp.cinemaUser = cinemaUser;
    }

    public static CinemaUser getCinemaUser() {
        return cinemaUser;
    }
}