package agh.to.lab.cinema.viewController.user;

import agh.to.lab.cinema.app.CinemaApp;
import agh.to.lab.cinema.model.movies.Movie;
import agh.to.lab.cinema.model.movies.MovieRepository;
import agh.to.lab.cinema.model.movies.MovieService;
import agh.to.lab.cinema.restController.MovieController;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


public class UserDefaultController {
    @FXML
    private TableView<Movie> movieTable;
    @FXML
    private TableColumn<Movie, String> thumbnailColumn;
    @FXML
    private TableColumn<Movie, String> titleColumn;
    @FXML
    private TableColumn<Movie, Integer> lengthColumn;

    public void initialize() throws IOException, InterruptedException {
        String baseUrl = MovieController.getBaseUrl();
        HttpClient moviesClient = HttpClient.newHttpClient();
        HttpRequest moviesRequest = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl))
                .GET()
                .build();
        HttpResponse<String> response = moviesClient.send(moviesRequest, HttpResponse.BodyHandlers.ofString());
        ObservableList<Movie> movies = FXCollections.observableArrayList(new ObjectMapper().readValue(response.body(), Movie[].class));
        movieTable.setItems(movies);
        movieTable.setRowFactory(row -> {
            TableRow<Movie> tableRow = new TableRow<>();
            tableRow.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !tableRow.isEmpty()) {
                    Movie movie = tableRow.getItem();
                    CinemaApp.setMovie(movie);
                    CinemaApp.loadView("views/user/movieDetails.fxml");
                }
            });
            return tableRow;
        });
        thumbnailColumn.setCellValueFactory(new PropertyValueFactory<>("thumbnail"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        lengthColumn.setCellValueFactory(new PropertyValueFactory<>("length"));
        thumbnailColumn.setCellFactory(column -> new TableCell<>() {
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

        titleColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String title, boolean empty) {
                super.updateItem(title, empty);
                if (empty || title == null) {
                    setText(null);
                } else {
                    setText(title);
                    setAlignment(javafx.geometry.Pos.CENTER); // Center the text
                }
            }
        });

        lengthColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Integer length, boolean empty) {
                super.updateItem(length, empty);
                if (empty || length == null) {
                    setText(null);
                } else {
                    setText(length + " minutes");
                    setAlignment(javafx.geometry.Pos.CENTER); // Center the text
                }
            }
        });
    }

    public void showAccountDetails(ActionEvent actionEvent) {
        CinemaApp.loadView("views/user/userInfo.fxml");
    }
}
