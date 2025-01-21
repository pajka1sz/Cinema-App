package agh.to.lab.cinema.viewController.user;

import agh.to.lab.cinema.app.CinemaApp;
import agh.to.lab.cinema.model.movies.Movie;
import agh.to.lab.cinema.model.purchases.Purchase;
import agh.to.lab.cinema.model.rates.MovieRate;
import agh.to.lab.cinema.restController.MovieController;
import agh.to.lab.cinema.restController.MovieRateController;
import agh.to.lab.cinema.restController.PurchaseController;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.util.converter.LocalDateTimeStringConverter;

import javafx.scene.image.ImageView;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

public class UserRatesController {
    @FXML
    TableView<MovieRate> userRatesTable;
    @FXML
    TableColumn<MovieRate, String> movieTitleColumn;
    @FXML
    TableColumn<MovieRate, Integer> rateColumn;
    @FXML
    TableColumn<MovieRate, String> rateDescriptionColumn;
    @FXML
    Button deleteRatesButton;

    public void initialize() throws IOException, InterruptedException {
        String baseUrl = MovieRateController.getBaseUrl();
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest ratesRequest = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl))
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(ratesRequest, HttpResponse.BodyHandlers.ofString());
        ObservableList<MovieRate> rates = FXCollections.observableArrayList(new ObjectMapper().readValue(response.body(), MovieRate[].class));
        rates = FXCollections.observableArrayList(
                rates.stream()
                        .filter(rate -> rate.getUser().getId().equals(CinemaApp.getLoggedUser().getId()))
                        .toList()
        );
        System.out.println(rates);
        userRatesTable.setItems(rates);

        userRatesTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        movieTitleColumn.setCellValueFactory(cellData ->
                Bindings.createStringBinding(() -> cellData.getValue().getMovie().getTitle()));


        rateColumn.setCellValueFactory(cellData ->
                Bindings.createIntegerBinding(() -> cellData.getValue().getRate()).asObject());

        rateDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        deleteRatesButton.disableProperty().bind(Bindings.isEmpty(userRatesTable.getSelectionModel().getSelectedItems()));
    }

    public void backToUserInfo() {
        CinemaApp.loadView("views/user/userInfo.fxml");
    }

    public void deleteRates() {
        List<MovieRate> moviesRatesToDelete = userRatesTable.getSelectionModel().getSelectedItems();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete rates");
        alert.setHeaderText("Are you sure you want to delete these rates?");
        ButtonType result = alert.showAndWait().get();
        if (result.equals(ButtonType.OK)) {
            for (MovieRate rate: moviesRatesToDelete) {
                try {
                    String url = MovieRateController.getBaseUrl() + "/delete/" + rate.getId();
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
        }

        try {
            initialize();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
