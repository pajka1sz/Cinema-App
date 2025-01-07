package agh.to.lab.cinema.app;

import agh.to.lab.cinema.model.movies.Movie;
import agh.to.lab.cinema.model.users.CinemaUser;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import lombok.Getter;

import java.io.IOException;

public class CinemaApp extends Application {
    @Getter
    private static Stage primaryStage;
    @Getter
    private static CinemaUser loggedUser;
    @Getter
    static Movie movie;

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

    public static void setMovie(Movie movie) {
        CinemaApp.movie = movie;
    }

    public static void setLoggedUser(CinemaUser loggedUser) {
        CinemaApp.loggedUser = loggedUser;
    }

}