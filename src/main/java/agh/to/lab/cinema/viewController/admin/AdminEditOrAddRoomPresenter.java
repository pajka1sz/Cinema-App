package agh.to.lab.cinema.viewController.admin;

import agh.to.lab.cinema.model.rooms.Room;
import agh.to.lab.cinema.restController.RoomController;
import agh.to.lab.cinema.viewController.JsonBodyCreator;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static agh.to.lab.cinema.app.CinemaApp.loadView;

public class AdminEditOrAddRoomPresenter {
    private Room room;
    private Stage dialogStage;
    private boolean isApproved;
    private boolean isEdited;
    @FXML
    private TextField roomNumberTextField;
    @FXML
    private TextField roomCapacityTextField;

    public AdminEditOrAddRoomPresenter(Room room) {
        this.room = room;
        this.isApproved = false;
    }

    public AdminEditOrAddRoomPresenter() {}

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setData(Room room) {
        this.room = room;
        isEdited = room.getNumber() != null;

        roomNumberTextField.textProperty().set(room.getNumber() != null ? String.valueOf(room.getNumber()) : "");
        roomCapacityTextField.textProperty().set(room.getCapacity() != null ? String.valueOf(room.getCapacity()) : "");
    }

    public boolean isApproved() {
        return isApproved;
    }

    @FXML
    private void handleOkAction() {
        room.setNumber(Integer.valueOf(roomNumberTextField.getText()));
        room.setCapacity(Integer.valueOf(roomCapacityTextField.getText()));

        try {
            HttpClient client = HttpClient.newHttpClient();
            String url;
            HttpRequest request;

            if (!isEdited) {
                // Add Room
                url = RoomController.getBaseUrl() + "/add";
                request = HttpRequest.newBuilder()
                        .uri(URI.create(url))
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(JsonBodyCreator.createRoomBody(
                                room.getNumber(), room.getCapacity())))
                        .build();
                client.send(request, HttpResponse.BodyHandlers.ofString());
            } else {
                // Update Room
                url = RoomController.getBaseUrl() + "/update_capacity/" + room.getId();
                request = HttpRequest.newBuilder()
                        .uri(URI.create(url))
                        .header("Content-Type", "application/json")
                        .PUT(HttpRequest.BodyPublishers.ofString(String.valueOf(room.getCapacity())))
                        .build();
                client.send(request, HttpResponse.BodyHandlers.ofString());

                url = RoomController.getBaseUrl() + "/update_number/" + room.getId();
                request = HttpRequest.newBuilder()
                        .uri(URI.create(url))
                        .header("Content-Type", "application/json")
                        .PUT(HttpRequest.BodyPublishers.ofString(String.valueOf(room.getNumber())))
                        .build();
                client.send(request, HttpResponse.BodyHandlers.ofString());
            }

            loadView("views/admin/adminRoomPanel.fxml");
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
