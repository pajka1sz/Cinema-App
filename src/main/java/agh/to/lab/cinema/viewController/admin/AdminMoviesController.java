package agh.to.lab.cinema.viewController.admin;

import agh.to.lab.cinema.model.movies.Movie;
import agh.to.lab.cinema.model.types.Type;
import agh.to.lab.cinema.restController.MovieController;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;

import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Set;

public class AdminMoviesController extends AdminController {
    // Table
    @FXML
    private TableView<Movie> adminMoviesTable;
    @FXML
    private TableColumn<Movie, String> movieTitle;
    @FXML
    private TableColumn<Movie, String> movieDescription;
    @FXML
    private TableColumn<Movie, Integer> movieLength;
    @FXML
    private TableColumn<Movie, String> movieThumbnail;
    @FXML
    private TableColumn<Movie, Set<Type>> movieTypes;

    // Buttons
    @FXML
    private Button editMovieButton;
    @FXML
    private Button deleteMovieButton;


    @FXML
    private void initialize() throws IOException, InterruptedException {
        String url = MovieController.getBaseUrl();
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Accept", "application/json")
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        ObservableList<Movie> movies = FXCollections.observableArrayList(new ObjectMapper().readValue(response.body(), Movie[].class));
        System.out.println(response.body());

        adminMoviesTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        movieTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        movieDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        movieLength.setCellValueFactory(new PropertyValueFactory<>("length"));
        movieThumbnail.setCellValueFactory(new PropertyValueFactory<>("thumbnail"));
        movieThumbnail.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String imageUrl, boolean empty) {
                super.updateItem(imageUrl, empty);
                if (empty || imageUrl == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    ImageView imageView = new ImageView(new Image(imageUrl, 100, 100, true, true));
                    imageView.setPreserveRatio(true);
                    setGraphic(imageView);
                    setAlignment(javafx.geometry.Pos.CENTER);
                }
            }
        });
        movieTypes.setCellValueFactory(new PropertyValueFactory<>("types"));
        adminMoviesTable.setItems(movies);

        editMovieButton.disableProperty().bind(Bindings.size(
                adminMoviesTable.getSelectionModel().getSelectedItems()).isNotEqualTo(1));
        deleteMovieButton.disableProperty().bind(Bindings.isEmpty(adminMoviesTable.getSelectionModel().getSelectedItems()));
    }

    @FXML
    private void editMovie() {
        Movie movie = adminMoviesTable.getSelectionModel().getSelectedItem();
        if (showMovieEditAndAddDialog(movie)) {
            try {
                initialize();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void addMovie() {
        Movie movie = new Movie();
        if (showMovieEditAndAddDialog(movie)) {
            try {
                initialize();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void deleteMovies() {
        List<Movie> moviesToDelete = adminMoviesTable.getSelectionModel().getSelectedItems();

        for (Movie movie: moviesToDelete) {
            try {
                String url = MovieController.getBaseUrl() + "/delete/" + movie.getId();
                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(url))
                        .header("Accept", "application/json")
                        .DELETE()
                        .build();
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                System.out.println(response.body());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        try {
            initialize();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
