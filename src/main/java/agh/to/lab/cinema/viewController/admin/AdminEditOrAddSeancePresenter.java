package agh.to.lab.cinema.viewController.admin;

import agh.to.lab.cinema.app.CinemaApp;
import agh.to.lab.cinema.model.movies.Movie;
import agh.to.lab.cinema.model.rooms.Room;
import agh.to.lab.cinema.model.seances.Seance;
import agh.to.lab.cinema.model.types.MovieType;
import agh.to.lab.cinema.model.types.Type;
import agh.to.lab.cinema.restController.MovieController;
import agh.to.lab.cinema.restController.PurchaseController;
import agh.to.lab.cinema.restController.SeanceController;
import agh.to.lab.cinema.viewController.JsonBodyCreator;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class AdminEditOrAddSeancePresenter {
    private Movie movie;
    private Room room;
    private Stage dialogStage;
    private boolean isApproved;
    private boolean isEdited;
    @FXML
    TextField movieTitleTextField;
    @FXML
    TextField seanceRoomNumber;
    @FXML
    TextField seanceStartDate;
    @FXML
    TextField seancePrice;

    public AdminEditOrAddSeancePresenter(Movie movie, Room room) {
        this.movie = movie;
        this.room = room;
        this.isApproved = false;
    }
    public AdminEditOrAddSeancePresenter() {
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
    public void setData(Movie movie, Room room) {
        this.movie = movie;
        this.room = room;

        movieTitleTextField.textProperty().set(movie.getTitle());
        seanceRoomNumber.textProperty().set(String.valueOf(room.getNumber()));
        movieTitleTextField.setEditable(false);
        seanceRoomNumber.setEditable(false);
    }
    public boolean isApproved() {
        return isApproved;
    }
    @FXML
    private void handleOkAction() {
        HttpClient client = HttpClient.newHttpClient();

        try {

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime startDate = LocalDateTime.parse(seanceStartDate.getText(), formatter);

//            String url = SeanceController.getBaseUrl() + "/add";
//            HttpRequest request = HttpRequest.newBuilder()
//                    .uri(URI.create(url))
//                    .header("Content-Type", "application/json")
//                    .POST(HttpRequest.BodyPublishers.ofString(JsonBodyCreator.createSeanceBody(
//                                movie, room, startDate
//                                , Float.parseFloat(seancePrice.getText()))))
//                    .build();
//            System.out.println(JsonBodyCreator.createSeanceBody(
//                    movie, room, startDate
//                    , Float.parseFloat(seancePrice.getText())));
//            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
//            System.out.println(response.body());
            String baseUrl = SeanceController.getBaseUrl() + "/add";
            HttpClient reserveClient = HttpClient.newHttpClient();
            HttpRequest reserveRequest = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(JsonBodyCreator.createSeanceBody(movie, room, startDate, Float.parseFloat(seancePrice.getText()))))
                    .build();
            try {
                HttpResponse<String> reserveResponse = reserveClient.send(reserveRequest, HttpResponse.BodyHandlers.ofString());
                System.out.println(reserveResponse.body());
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
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

//    private void updateFromTextField(String url_adding, TextField field, HttpClient client) {
//        try {
//            String url = MovieController.getBaseUrl() + url_adding + seance.getId();
//            HttpRequest request = HttpRequest.newBuilder()
//                    .uri(URI.create(url))
//                    .PUT(HttpRequest.BodyPublishers.ofString(field.getText()))
//                    .build();
//            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
//            System.out.println(response.body());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

}
