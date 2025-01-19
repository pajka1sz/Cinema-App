package agh.to.lab.cinema.viewController.user;

import agh.to.lab.cinema.app.CinemaApp;
import agh.to.lab.cinema.model.movies.Movie;
import agh.to.lab.cinema.model.types.MovieType;
import agh.to.lab.cinema.model.types.Type;
import agh.to.lab.cinema.model.users.CinemaUser;
import agh.to.lab.cinema.restController.MovieController;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashSet;
import java.util.Set;


public class UserDefaultController {
    @FXML
    private TableView<Movie> movieTable;
    @FXML
    private TableColumn<Movie, String> thumbnailColumn;
    @FXML
    private TableColumn<Movie, String> titleColumn;
    @FXML
    private TableColumn<Movie, Integer> lengthColumn;
    @FXML
    private TextField searchField;
    @FXML
    private FlowPane typeCheckboxPane;
    @FXML
    private Button rateButton;

    private ObservableList<Movie> filteredMovies;
    private ObservableList<Movie> movies;

    private Set<MovieType> selectedTypes = new HashSet<>();

    public void initialize() throws IOException, InterruptedException {
        String baseUrl = MovieController.getBaseUrl();
        HttpClient moviesClient = HttpClient.newHttpClient();
        HttpRequest moviesRequest = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl))
                .GET()
                .build();
        HttpResponse<String> response = moviesClient.send(moviesRequest, HttpResponse.BodyHandlers.ofString());
        movies = FXCollections.observableArrayList(new ObjectMapper().readValue(response.body(), Movie[].class));
        filteredMovies = FXCollections.observableArrayList(movies);
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

        for (MovieType movieType : MovieType.values()) {
            CheckBox checkBox = new CheckBox(movieType.toString());
            checkBox.setOnAction(event -> {
                if (checkBox.isSelected()) {
                    selectedTypes.add(movieType);
                } else {
                    selectedTypes.remove(movieType);
                }
                applyFilters();
            });
            typeCheckboxPane.getChildren().add(checkBox);
        }

        rateButton.disableProperty().bind(Bindings.size(movieTable.getSelectionModel().getSelectedItems()).isNotEqualTo(1));
    }

    public void showAccountDetails() {
        CinemaApp.loadView("views/user/userInfo.fxml");
    }

    private void showRateWindow(Movie movie, CinemaUser user) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(UserDefaultController.class.getClassLoader().getResource("views/user/userRateAddDialog.fxml"));
            BorderPane page = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Add rate");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(CinemaApp.getPrimaryStage());
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            UserAddRatePresenter presenter = loader.getController();
            presenter.setDialogStage(dialogStage);
            presenter.setData(movie, user);

            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void rateMovie() {
        showRateWindow(movieTable.getSelectionModel().getSelectedItem(), CinemaApp.getLoggedUser());
    }

    @FXML
    private void applyFilters() {
        String searchText = searchField.getText().toLowerCase();

        filteredMovies.setAll(movies.filtered(movie -> {
            boolean matchesSearch = movie.getTitle().toLowerCase().contains(searchText);
            boolean matchesType = selectedTypes.isEmpty() || movie.getTypes().stream()
                    .map(Type::getMovieType)
                    .anyMatch(selectedTypes::contains);
            return matchesSearch && matchesType;
        }));

        movieTable.setItems(filteredMovies);
    }
}
