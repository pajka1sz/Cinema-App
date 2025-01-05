package agh.to.lab.cinema.viewController;

import agh.to.lab.cinema.app.CinemaApp;
import agh.to.lab.cinema.model.users.UserController;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class UserViewController {
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
}
