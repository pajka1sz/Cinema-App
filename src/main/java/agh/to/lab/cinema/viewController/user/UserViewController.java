package agh.to.lab.cinema.viewController.user;

import agh.to.lab.cinema.app.CinemaApp;
import agh.to.lab.cinema.model.purchases.Purchase;
import agh.to.lab.cinema.restController.PurchaseController;
import agh.to.lab.cinema.restController.UserController;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.util.converter.LocalDateTimeStringConverter;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class UserViewController {
    @FXML
    Label notificationLabel;
    @FXML
    Label usernameLabel;
    @FXML
    Label emailLabel;
    @FXML
    Label welcomeUserLabel;


    @FXML
    private void initialize() {
        usernameLabel.setText(CinemaApp.getLoggedUser().getUsername());
        emailLabel.setText(CinemaApp.getLoggedUser().getEmail());
        welcomeUserLabel.setText("Welcome " + CinemaApp.getLoggedUser().getUsername() + "!");

        try {
            String baseUrl = PurchaseController.getBaseUrl() + "/user_id/" + CinemaApp.getLoggedUser().getId();
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest moviesRequest = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl))
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(moviesRequest, HttpResponse.BodyHandlers.ofString());
            List<Purchase> purchases = Arrays.asList(new ObjectMapper().readValue(response.body(), Purchase[].class));
            purchases = purchases.stream()
                    .filter(purchase -> purchase.getSeance().getStartDate().isAfter(LocalDateTime.now()))
                    .sorted(Comparator.comparing(p -> p.getSeance().getStartDate()))
                    .toList();
            System.out.println(purchases.size());

            if (purchases.isEmpty())
                notificationLabel.setText("You do not have any future reservations in our cinema.");
            else {
                Purchase nearestPurchase = purchases.get(0);

                String pattern = "yyyy-MM-dd HH:mm:ss";
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
                LocalDateTimeStringConverter converter = new LocalDateTimeStringConverter(formatter, formatter);

                notificationLabel.setText("Your next seance is: " + nearestPurchase.getSeance().getMovie().getTitle() +
                        ", on " + converter.toString(nearestPurchase.getSeance().getStartDate()) +
                        " in room number " + nearestPurchase.getSeance().getRoom().getNumber() + ".");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void logOut() {
        CinemaApp.setLoggedUser(null);
        CinemaApp.loadView("views/login.fxml");
    }

    @FXML
    private String deleteAccount() throws Exception {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete account");
        alert.setHeaderText("Are you sure you want to delete this account?");
        ButtonType result = alert.showAndWait().get();
        if (result.equals(ButtonType.OK)) {
            String baseUrl = UserController.getBaseUrl() + "/delete/";
            String deleteUrl = baseUrl.concat(String.valueOf(CinemaApp.getLoggedUser().getId()));
            HttpClient deleteClient = HttpClient.newHttpClient();
            HttpRequest deleteRequest = HttpRequest.newBuilder()
                    .uri(URI.create(deleteUrl))
                    .header("Accept", "application/json")
                    .DELETE()
                    .build();
            HttpResponse<String> deleteResponse = deleteClient.send(deleteRequest, HttpResponse.BodyHandlers.ofString());
            System.out.println(deleteResponse.body());
            CinemaApp.loadView("views/login.fxml");
            CinemaApp.setLoggedUser(null);
            return deleteResponse.body();
        }
        return null;
    }

    public void showMoviesList(ActionEvent actionEvent) {
        CinemaApp.loadView("views/user/userDefault.fxml");
    }

    public void showPurchasesList() {
        CinemaApp.loadView("views/user/userPurchases.fxml");
    }

    public void showMovieRatesList() {
        CinemaApp.loadView("views/user/userRates.fxml");
    }
}
