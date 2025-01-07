package agh.to.lab.cinema.viewController.admin;

import agh.to.lab.cinema.model.movies.Movie;
import agh.to.lab.cinema.restController.MovieController;
import agh.to.lab.cinema.viewController.JsonBodyCreator;
import javafx.fxml.FXML;
import javafx.stage.Stage;

import javafx.scene.control.TextField;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashSet;

public class AdminEditOrAddMoviePresenter {
    private Movie movie;
    private Stage dialogStage;
    private boolean isApproved;
    private boolean isEdited;
    @FXML
    TextField movieTitleTextField;
    @FXML
    TextField movieDescriptionTextField;
    @FXML
    TextField movieLengthTextField;
    @FXML
    TextField movieThumbnailTextField;

    public AdminEditOrAddMoviePresenter(Movie movie) {
        this.movie = movie;
        this.isApproved = false;
    }
    public AdminEditOrAddMoviePresenter() {

    }
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
    public void setData(Movie movie) {
        this.movie = movie;
        isEdited = movie.getTitle() != null;

        movieTitleTextField.textProperty().set(movie.getTitle());
        movieDescriptionTextField.textProperty().set(movie.getDescription());
        movieLengthTextField.textProperty().set(movie.getLength() != null ? String.valueOf(movie.getLength()) : "");
        movieThumbnailTextField.textProperty().set(movie.getThumbnail());
    }
    public boolean isApproved() {
        return isApproved;
    }
    @FXML
    private void handleOkAction() {
        movie.setTitle(movieTitleTextField.getText());
        movie.setDescription(movieDescriptionTextField.getText());
        movie.setLength(Integer.valueOf(movieLengthTextField.getText()));
        movie.setThumbnail(movieThumbnailTextField.getText());

        try {
            if (!isEdited) {
                String url = MovieController.getBaseUrl() + "/add";
                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(url))
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(JsonBodyCreator.createMovieBody(
                                movie.getTitle(), movie.getDescription(), movie.getLength(), movie.getThumbnail(), new HashSet<>())))
                        .build();
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                System.out.println(response.body());
            }
            isApproved = true;

        } catch (Exception e) {
            e.printStackTrace();
        }
        dialogStage.close();
    }
    @FXML
    private void handleCancelAction() {
        dialogStage.close();
    }
}
