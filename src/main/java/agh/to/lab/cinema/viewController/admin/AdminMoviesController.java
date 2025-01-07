package agh.to.lab.cinema.viewController.admin;

import agh.to.lab.cinema.model.movies.Movie;
import agh.to.lab.cinema.model.types.Type;
import agh.to.lab.cinema.restController.MovieController;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;

import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
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

    //@FXML
    //private HBox editingTable;

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
        System.out.println(movies.get(0));
        System.out.println(movies.size());
        System.out.println(response.body());

        movieTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        movieDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        movieLength.setCellValueFactory(new PropertyValueFactory<>("length"));
        movieThumbnail.setCellValueFactory(new PropertyValueFactory<>("thumbnail"));
        movieTypes.setCellValueFactory(new PropertyValueFactory<>("types"));
        adminMoviesTable.setItems(movies);

        editMovieButton.disableProperty().bind(Bindings.isEmpty(adminMoviesTable.getSelectionModel().getSelectedItems()));
    }

    @FXML
    private void editMovie() {
        //editingTable.setVisible(true);
    }

    @FXML
    private void addMovie() {
        //editingTable.setVisible(true);
    }
}
