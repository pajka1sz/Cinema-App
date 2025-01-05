package agh.to.lab.cinema.viewController;

import agh.to.lab.cinema.app.CinemaApp;
import agh.to.lab.cinema.model.roles.RoleType;
import agh.to.lab.cinema.model.types.MovieType;
import agh.to.lab.cinema.model.types.Type;
import agh.to.lab.cinema.model.users.CinemaUser;
import agh.to.lab.cinema.restController.UserController;
import agh.to.lab.cinema.restController.MovieController;
import agh.to.lab.cinema.restController.RoomController;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
        String url = UserController.getBaseUrl();
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
                String baseUrl = url + "/delete/";
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

    @FXML
    private String addTestRecords() throws Exception{
        String url = MovieController.getBaseUrl() + "/add";
        HttpClient client = HttpClient.newHttpClient();
        Set<Type> movieTypes = new HashSet<>();
        movieTypes.add(new Type(MovieType.COMEDY));
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(JsonBodyCreator.createMovieBody("Nietykalni",
                        "Sparaliżowany milioner zatrudnia do opieki młodego chłopaka z przedmieścia, który właśnie wyszedł z więzienia.", 112,
                        "https://www.monolith.pl/wp-content/uploads/2021/07/nietykalni-nietykalni.jpg", movieTypes)))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());

        String url2 = RoomController.getBaseUrl() + "/add";
        HttpClient client2 = HttpClient.newHttpClient();
        HttpRequest request2 = HttpRequest.newBuilder()
                .uri(URI.create(url2))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(JsonBodyCreator.createRoomBody(4, 100)))
                .build();
        HttpResponse<String> response2 = client2.send(request2, HttpResponse.BodyHandlers.ofString());
        System.out.println(response2.body());

        return response.body();
    }

}
