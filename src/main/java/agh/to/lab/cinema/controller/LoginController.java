package agh.to.lab.cinema.controller;

import agh.to.lab.cinema.app.CinemaApp;
import agh.to.lab.cinema.model.roles.RoleType;
import agh.to.lab.cinema.model.users.CinemaUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import org.springframework.http.HttpStatus;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class LoginController {
    // Login view labels, textFields etc.
    @FXML
    TextField usernameTextFieldLogin;
    @FXML
    PasswordField passwordTextFieldLogin;
    @FXML
    Label loginResultLabel;


    // Login view functions
    @FXML
    private String sendLoginPostRequest() throws Exception {

        System.out.println(JsonBodyCreator.createCinemaUserBody(usernameTextFieldLogin.getText(), passwordTextFieldLogin.getText(), null));
        String url = "http://localhost:8080/api/login";

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(JsonBodyCreator.createCinemaUserBody(usernameTextFieldLogin.getText(), passwordTextFieldLogin.getText(), null)))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("Response body: " + response.body() + "\nResponse status: " + response.statusCode());

        // Response status:
        // 200 / HttpStatus.OK - user successfully logged in
        // 404 / HttpStatus.NOT_FOUND - user not found

        if (response.statusCode() == HttpStatus.OK.value()) {
            CinemaUser user = new ObjectMapper().readValue(response.body(), CinemaUser.class);
            System.out.println(user);

            System.out.println("User found");

            loginResultLabel.setVisible(true);
            loginResultLabel.setText("You have successfully logged in!");
            loginResultLabel.setTextFill(Color.color(0, 1.0, 0));

            CinemaApp.setLoggedUser(user);
            String loadedView = user.getRole().getRole().equals(RoleType.ADMIN) ? "views/adminPanel.fxml" : "views/userView.fxml";
            CinemaApp.loadView(loadedView);
        }
        else {
            System.out.println("User not found");

            loginResultLabel.setVisible(true);
            loginResultLabel.setText("Username or password is incorrect");
            loginResultLabel.setTextFill(Color.color(1.0, 0, 0));
        }

        return response.body();
    }
    @FXML
    private void goToRegister() {
        CinemaApp.loadView("views/register.fxml");
    }

    @FXML
    private void resetLoginResultLabel() {
        loginResultLabel.setVisible(false);
    }
}
