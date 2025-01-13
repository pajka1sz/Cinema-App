package agh.to.lab.cinema.viewController.admin;

import agh.to.lab.cinema.model.movies.Movie;
import agh.to.lab.cinema.model.purchases.Purchase;
import agh.to.lab.cinema.model.roles.Role;
import agh.to.lab.cinema.model.rooms.Room;
import agh.to.lab.cinema.model.seances.Seance;
import agh.to.lab.cinema.model.users.CinemaUser;
import agh.to.lab.cinema.restController.MovieController;
import agh.to.lab.cinema.restController.RoomController;
import agh.to.lab.cinema.restController.SeanceController;
import agh.to.lab.cinema.restController.UserController;
import agh.to.lab.cinema.viewController.JsonBodyCreator;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.converter.LocalDateTimeStringConverter;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AdminSeancesController extends AdminController {

    // Movies table
    @FXML
    private TableView<Movie> adminMoviesTable;
    @FXML
    private TableColumn<Movie, String> movieTitle;
    @FXML
    private TableColumn<Movie, Integer> movieLength;
    @FXML
    private TableColumn<Movie, String> movieThumbnail;
    @FXML
    private TableColumn<Movie, String> movieDescription;

    // Rooms table
    @FXML
    private TableView<Room> adminRoomsTable;
    @FXML
    private TableColumn<Room, Integer> roomNumber;
    @FXML
    private TableColumn<Room, Integer> roomCapacity;

    // Seances table
    @FXML
    TableView<Seance> adminSeancesTable;
    @FXML
    TableColumn<Seance, Integer> seanceId;
    @FXML
    TableColumn<Seance, String> seanceMovie ;
    @FXML
    TableColumn<Seance, Integer> seanceRoom;
    @FXML
    TableColumn<Seance, String> seanceStartDate;
    @FXML
    TableColumn<Seance, Float> seancePrice;

    @FXML
    private void initialize() throws Exception {

        String urlMovie = MovieController.getBaseUrl();
        HttpClient clientMovie = HttpClient.newHttpClient();
        HttpRequest requestMovie = HttpRequest.newBuilder()
                .uri(URI.create(urlMovie))
                .header("Accept", "application/json")
                .GET()
                .build();
        HttpResponse<String> responseMovie = clientMovie.send(requestMovie, HttpResponse.BodyHandlers.ofString());
        ObservableList<Movie> movies = FXCollections.observableArrayList(new ObjectMapper().readValue(responseMovie.body(), Movie[].class));
        System.out.println(responseMovie.body());

        movieTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        movieLength.setCellValueFactory(new PropertyValueFactory<>("length"));
        movieDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
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
        adminMoviesTable.setItems(movies);

        String urlRoom = RoomController.getBaseUrl();
        HttpClient clientRoom = HttpClient.newHttpClient();
        HttpRequest requestRoom = HttpRequest.newBuilder()
                .uri(URI.create(urlRoom))
                .header("Accept", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = clientRoom.send(requestRoom, HttpResponse.BodyHandlers.ofString());
        ObservableList<Room> rooms = FXCollections.observableArrayList(
                new ObjectMapper().readValue(response.body(), Room[].class)
        );
        roomNumber.setCellValueFactory(new PropertyValueFactory<>("number"));
        roomCapacity.setCellValueFactory(new PropertyValueFactory<>("capacity"));
        adminRoomsTable.setItems(rooms);

        String urlSeance = SeanceController.getBaseUrl();
        HttpClient clientSeance = HttpClient.newHttpClient();
        HttpRequest requestSeance = HttpRequest.newBuilder()
                .uri(URI.create(urlSeance))
                .header("Accept", "application/json")
                .GET().
                build();
        HttpResponse<String> responseSeance = clientSeance.send(requestSeance, HttpResponse.BodyHandlers.ofString());
        ObservableList<Seance> seances = FXCollections.observableArrayList(new ObjectMapper().readValue(responseSeance.body(), Seance[].class));

        seanceId.setCellValueFactory(new PropertyValueFactory<>("id"));
        seanceMovie.setCellValueFactory(cellData -> Bindings.createStringBinding(() -> cellData.getValue().getMovie().getTitle()));
        seanceRoom.setCellValueFactory(cellData -> Bindings.createIntegerBinding(() -> cellData.getValue().getRoom().getNumber()).asObject());

        String pattern = "yyyy-MM-dd HH:mm:ss";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        LocalDateTimeStringConverter converter = new LocalDateTimeStringConverter(formatter, formatter);
        seanceStartDate.setCellValueFactory(cellData -> Bindings.createStringBinding(() -> converter.toString(cellData.getValue().getStartDate())));

        seancePrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        adminSeancesTable.setItems(seances);
    }

    @FXML
    private void editSeance() {
        Seance seance = adminSeancesTable.getSelectionModel().getSelectedItem();
        if (showSeanceEditAndAddDialog(seance.getMovie(), seance.getRoom())) {
            try {
                initialize();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void addSeance() {
        Movie movie = adminMoviesTable.getSelectionModel().getSelectedItem();
        Room room = adminRoomsTable.getSelectionModel().getSelectedItem();

        if (movie != null && room != null) {
            showSeanceEditAndAddDialog(movie, room);
        }
        try {
            initialize();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void deleteSeances() {
        List<Seance> seancesToDelete = adminSeancesTable.getSelectionModel().getSelectedItems();

        for (Seance seance: seancesToDelete) {
            try {
                String url = MovieController.getBaseUrl() + "/delete/" + seance.getId();
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
