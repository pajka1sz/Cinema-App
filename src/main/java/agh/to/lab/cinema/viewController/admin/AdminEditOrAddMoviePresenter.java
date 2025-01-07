package agh.to.lab.cinema.viewController.admin;

import agh.to.lab.cinema.model.movies.Movie;
import agh.to.lab.cinema.model.types.MovieType;
import agh.to.lab.cinema.model.types.Type;
import agh.to.lab.cinema.restController.MovieController;
import agh.to.lab.cinema.viewController.JsonBodyCreator;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import javafx.scene.control.TextField;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

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
    @FXML
    ListView<CheckBox> movieTypesList;

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

        movieTitleTextField.setEditable(!isEdited);
        movieLengthTextField.setEditable(!isEdited);

        List<MovieType> currentMovieTypes = movie.getTypes().stream()
                .map(Type::getMovieType)
                .toList();
        for (MovieType movieType: MovieType.values()) {
            CheckBox checkBox = new CheckBox(movieType.toString());
            movieTypesList.getItems().add(checkBox);

            if (currentMovieTypes.contains(movieType))
                checkBox.setSelected(true);
        }
    }
    public boolean isApproved() {
        return isApproved;
    }
    @FXML
    private void handleOkAction() {
        HttpClient client = HttpClient.newHttpClient();

        try {
            if (!isEdited) {
                String url = MovieController.getBaseUrl() + "/add";
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(url))
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(JsonBodyCreator.createMovieBody(
                                movieTitleTextField.getText(), movieDescriptionTextField.getText(),
                                Integer.valueOf(movieLengthTextField.getText()), movieThumbnailTextField.getText(),
                                getSelectedMovieTypes())))
                        .build();
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                System.out.println(response.body());
                isApproved = true;
            }
            else {
                if (!movieDescriptionTextField.getText().equals(movie.getDescription()))
                    updateFromTextField("/update_description/", movieDescriptionTextField, client);
                if (!movieThumbnailTextField.getText().equals(movie.getThumbnail()))
                    updateFromTextField("/update_thumbnail/", movieThumbnailTextField, client);
                if (!getSelectedMovieTypes().equals(movie.getTypes()))
                    updateTypes(client);
                isApproved = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        dialogStage.close();
    }
    @FXML
    private void handleCancelAction() {
        dialogStage.close();
    }

    private void updateFromTextField(String url_adding, TextField field, HttpClient client) {
        try {
            String url = MovieController.getBaseUrl() + url_adding + movie.getId();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .PUT(HttpRequest.BodyPublishers.ofString(field.getText()))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response.body());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateTypes(HttpClient client) {
        try {
            String url = MovieController.getBaseUrl() + "/update_types/" + movie.getId();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(JsonBodyCreator.createMovieTypesBody(getSelectedMovieTypes())))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response.body());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private HashSet<Type> getSelectedMovieTypes() {
        return movieTypesList.getItems()
                .stream()
                .filter(CheckBox::isSelected)
                .map(CheckBox::getText)
                .map(String::toUpperCase)
                .map(MovieType::valueOf)
                .map(Type::new)
                .collect(Collectors.toCollection(HashSet::new));
    }
}
