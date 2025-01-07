package agh.to.lab.cinema.viewController.admin;

import agh.to.lab.cinema.app.CinemaApp;
import agh.to.lab.cinema.model.rooms.Room;
import agh.to.lab.cinema.restController.RoomController;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static agh.to.lab.cinema.app.CinemaApp.loadView;

public class AdminRoomsController extends AdminController {

    @FXML
    private TableView<Room> roomsTable;
    @FXML
    private TableColumn<Room, Integer> roomNumber;
    @FXML
    private TableColumn<Room, Integer> roomCapacity;
    @FXML
    private Button editRoomButton, addRoomButton, deleteRoomButton;

    private final HttpClient client = HttpClient.newHttpClient();
    private final String url = RoomController.getBaseUrl();

    @FXML
    private void initialize() throws IOException, InterruptedException {
        // Load room data
        loadRooms();

        // Set up table columns
        roomNumber.setCellValueFactory(new PropertyValueFactory<>("number"));
        roomCapacity.setCellValueFactory(new PropertyValueFactory<>("capacity"));

        // Disable buttons when no room is selected
        editRoomButton.disableProperty().bind(roomsTable.getSelectionModel().selectedItemProperty().isNull());
        deleteRoomButton.disableProperty().bind(roomsTable.getSelectionModel().selectedItemProperty().isNull());
    }

    private void loadRooms() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Accept", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        ObservableList<Room> rooms = FXCollections.observableArrayList(
                new ObjectMapper().readValue(response.body(), Room[].class)
        );
        roomsTable.setItems(rooms);
    }

    @FXML
    private void editRoom() {
        Room room = roomsTable.getSelectionModel().getSelectedItem();
        showRoomEditAndAddDialog(room);
    }

    @FXML
    private void addRoom() {
        Room room = new Room();
        if (showRoomEditAndAddDialog(room)) {
            try {
                initialize(); // Reload the data
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private String deleteRoom(Long roomId) throws Exception {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Room");
        alert.setHeaderText("Are you sure you want to delete this room?");
        ButtonType result = alert.showAndWait().orElse(ButtonType.CANCEL);

        if (result.equals(ButtonType.OK)) {
            String baseUrl = RoomController.getBaseUrl() + "/delete/";
            String deleteUrl = baseUrl.concat(String.valueOf(roomId));

            HttpClient deleteClient = HttpClient.newHttpClient();
            HttpRequest deleteRequest = HttpRequest.newBuilder()
                    .uri(URI.create(deleteUrl))
                    .header("Accept", "application/json")
                    .DELETE()
                    .build();

            HttpResponse<String> deleteResponse = deleteClient.send(deleteRequest, HttpResponse.BodyHandlers.ofString());
            System.out.println("Response from delete operation: " + deleteResponse.body());

            // Reload the room list view to reflect changes
            loadView("views/admin/adminRoomPanel.fxml");
            return deleteResponse.body();
        }
        return null;
    }

    @FXML
    private void deleteRoomHandler() {
        Room selectedRoom = roomsTable.getSelectionModel().getSelectedItem();
        if (selectedRoom != null) {
            try {
                deleteRoom(selectedRoom.getId());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Alert noSelectionAlert = new Alert(Alert.AlertType.WARNING);
            noSelectionAlert.setTitle("No Room Selected");
            noSelectionAlert.setHeaderText("Please select a room to delete.");
            noSelectionAlert.showAndWait();
        }
    }
}
