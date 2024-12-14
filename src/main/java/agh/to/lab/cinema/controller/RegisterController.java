package agh.to.lab.cinema.controller;

import agh.to.lab.cinema.app.CinemaApp;
import agh.to.lab.cinema.model.roles.RoleType;
import agh.to.lab.cinema.model.users.CinemaUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.springframework.http.HttpStatus;

import java.awt.Color;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class RegisterController {
    // Register text fields, labels etc.
    @FXML
    TextField usernameTextFieldRegister;
    @FXML
    TextField emailTextFieldRegister;
    @FXML
    PasswordField passwordTextFieldRegister;
    @FXML
    Label emailLabelRegister;
    @FXML
    Label usernameLabelRegister;
    @FXML
    Label passwordLabelRegister;
    @FXML
    Label successfulRegisterLabel;

    // Colors for textFields;
    Color WHITE = new Color(255, 255, 255);
    Color RED = new Color(255, 0, 0);

    // Register functions
    @FXML
    private String sendRegisterPostRequest() throws Exception {
        if (checkRegisterEmpty())
            return "Empty values";
        System.out.println(JsonBodyCreator.createCinemaUserBody(usernameTextFieldRegister.getText(), passwordTextFieldRegister.getText(), emailTextFieldRegister.getText()));

        String url = "http://localhost:8080/api/register";
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(JsonBodyCreator.createCinemaUserBody(usernameTextFieldRegister.getText(), passwordTextFieldRegister.getText(), emailTextFieldRegister.getText())))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("Response body: " + response.body());
        System.out.println(response.statusCode());

        // Response status:
        // 200 / HttpStatus.OK - user successfully registered
        // 401 - invalid email
        // 402 - username occupied
        // 403 - email occupied
        // 404 - unknown error

        if (response.statusCode() == HttpStatus.OK.value()) {
            CinemaUser registeredUser = new ObjectMapper().readValue(response.body(), CinemaUser.class);

            successfulRegisterLabel.setVisible(true);
            CinemaApp.setLoggedUser(registeredUser);
            CinemaApp.loadView("views/userView.fxml");
        }
        else if (response.statusCode() == 401) {
            emailLabelRegister.setVisible(true);
            emailLabelRegister.setText("Invalid email");
            setTextFieldColor(emailTextFieldRegister, RED);
        }
        else if (response.statusCode() == 402) {
            usernameLabelRegister.setVisible(true);
            usernameLabelRegister.setText("Username not available");
            setTextFieldColor(usernameTextFieldRegister, RED);
        }
        else if (response.statusCode() == 403) {
            emailLabelRegister.setVisible(true);
            emailLabelRegister.setText("Email already has an account");
            setTextFieldColor(emailTextFieldRegister, RED);
        }
        else {
            emailLabelRegister.setVisible(true);
            emailLabelRegister.setText("UNKNOWN ERROR");
            usernameLabelRegister.setVisible(true);
            usernameLabelRegister.setText("UNKNOWN ERROR");
        }

        return response.body();
    }

    @FXML
    private void goToLogin() {
        CinemaApp.loadView("views/login.fxml");
    }

    @FXML
    private void resetEmailLabelAndTextFieldRegister() {
        emailLabelRegister.setVisible(false);
        setTextFieldColor(emailTextFieldRegister, WHITE);
    }

    @FXML
    private void resetUsernameLabelAndTextFieldRegister() {
        usernameLabelRegister.setVisible(false);
        setTextFieldColor(usernameTextFieldRegister, WHITE);
    }

    @FXML
    private void resetPasswordLabelAndTextFieldRegister() {
        passwordLabelRegister.setVisible(false);
        setTextFieldColor(passwordTextFieldRegister, WHITE);
    }

    private boolean checkRegisterEmpty() {
        boolean isEmpty = false;
        if (emailTextFieldRegister.getText().isBlank()) {
            emailLabelRegister.setVisible(true);
            emailLabelRegister.setText("Email must not be empty!");
            setTextFieldColor(emailTextFieldRegister, RED);
            isEmpty = true;
        }
        if (usernameTextFieldRegister.getText().isBlank()) {
            usernameLabelRegister.setVisible(true);
            usernameLabelRegister.setText("Username must not be empty!");
            setTextFieldColor(usernameTextFieldRegister, RED);
            isEmpty = true;
        }
        if (passwordTextFieldRegister.getText().isBlank()) {
            passwordLabelRegister.setVisible(true);
            passwordLabelRegister.setText("Password must not be empty!");
            setTextFieldColor(passwordTextFieldRegister, RED);
            isEmpty = true;
        }
        return isEmpty;
    }

    private void setTextFieldColor(TextField textField, Color color) {
        String hexColor = Integer.toHexString(color.getRGB()).substring(2);
        if (!color.equals(WHITE))
            hexColor += "; -fx-opacity: 0.5";
        textField.setStyle("-fx-control-inner-background: " + "#" + hexColor);
    }
}
