package agh.to.lab.cinema.controller;

import agh.to.lab.cinema.app.CinemaApp;
import agh.to.lab.cinema.model.roles.RoleType;
import agh.to.lab.cinema.model.users.CinemaUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class AdminController {
    @FXML
    TextField changeRoleTextField;

    ObservableList<String> roleOptions;

    @FXML
    ComboBox changeRoleComboBox = new ComboBox(roleOptions);

    @FXML
    TextField deleteUserTextField;

    @FXML
    Label deleteUserLabel;

    @FXML
    Label changeRoleLabel;

    @FXML
    private void initialize() {
        changeRoleComboBox.setItems(FXCollections.observableArrayList(
                Arrays.stream(RoleType.values())
                        .map(Enum::toString)
                        .collect(Collectors.toList())
        ));
    }

    @FXML
    private String deleteUser() throws Exception {
        String url = "http://localhost:8080/user";
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Accept", "application/json")
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        List<CinemaUser> users = Arrays.asList(new ObjectMapper().readValue(response.body(), CinemaUser[].class));
        CinemaUser foundUser = null;
        for (CinemaUser user: users) {
            if (user.getUsername().equals(deleteUserTextField.getText())) {
                foundUser = user;
            }
        }
        if (foundUser != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete account");
            alert.setHeaderText("Are you sure you want to delete account of " + foundUser.getUsername() + "?");
            ButtonType result = alert.showAndWait().get();
            if (result.equals(ButtonType.OK)) {
                String baseUrl = "http://localhost:8080/user/delete/";
                String deleteUrl = baseUrl.concat(String.valueOf(foundUser.getId()));
                HttpClient deleteClient = HttpClient.newHttpClient();
                HttpRequest deleteRequest = HttpRequest.newBuilder()
                        .uri(URI.create(deleteUrl))
                        .header("Accept", "application/json")
                        .DELETE()
                        .build();
                HttpResponse<String> deleteResponse = deleteClient.send(deleteRequest, HttpResponse.BodyHandlers.ofString());
                System.out.println(deleteResponse.body());
                deleteUserLabel.setText("Deleted user: " + foundUser.getUsername());
                deleteUserLabel.setVisible(true);
                if (foundUser.getUsername().equals("admin")) {
                    CinemaApp.setLoggedUser(null);
                    CinemaApp.loadView("views/login.fxml");
                }
            }
        } else {
            deleteUserLabel.setText("This user does not exist!");
            deleteUserLabel.setVisible(true);
        }
        System.out.println(response.body());
        return response.body();
    }


    @FXML
    private void logOut() {
        CinemaApp.setLoggedUser(null);
        CinemaApp.loadView("views/login.fxml");
    }

}
