package agh.to.lab.cinema.controller;

import agh.to.lab.cinema.app.CinemaApp;
import agh.to.lab.cinema.model.roles.RoleType;
import agh.to.lab.cinema.model.users.CinemaUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.List;

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


    // Register functions
    @FXML
    private String sendRegisterPostRequest() throws Exception {
        if (checkRegisterEmpty())
            return "Empty values";
        System.out.println(passwordTextFieldRegister.getText() + " " + usernameTextFieldRegister.textProperty().get());
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

        if (response.body().equals("User added")) {
            successfulRegisterLabel.setVisible(true);

            String getUsersUrl = "http://localhost:8080/api";
            HttpRequest getUsersRequest = HttpRequest.newBuilder()
                    .uri(URI.create(getUsersUrl))
                    .header("Accept", "application/json")
                    .GET()
                    .build();
            HttpResponse<String> getUsersResponse = client.send(getUsersRequest, HttpResponse.BodyHandlers.ofString());
            List<CinemaUser> users = Arrays.asList(new ObjectMapper().readValue(getUsersResponse.body(), CinemaUser[].class));
            for (CinemaUser user: users) {
                if (user.getUsername().equals(usernameTextFieldRegister.getText())
                        && user.getEmail().equals(emailTextFieldRegister.getText())) {
                    System.out.println("FOUND!!!");
                    CinemaApp.setLoggedUser(user);
                    String loadedView = user.getRoleType().equals(RoleType.ADMIN) ? "views/adminPanel.fxml" : "views/userView.fxml";
                    CinemaApp.loadView(loadedView);
                }
            }
        }
        else if (response.body().equals("Invalid email")) {
            emailLabelRegister.setVisible(true);
            emailLabelRegister.setText("Invalid email");
        }
        else if (response.body().equals("Email occupied")) {
            emailLabelRegister.setVisible(true);
            emailLabelRegister.setText("Email already has an account");
        }
        else if (response.body().equals("Username not available")) {
            usernameLabelRegister.setVisible(true);
            usernameLabelRegister.setText("Username not available");
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
    private void resetEmailLabelRegister() {
        emailLabelRegister.setVisible(false);
    }

    @FXML
    private void resetUsernameLabelRegister() {
        usernameLabelRegister.setVisible(false);
    }

    @FXML
    private void resetPasswordLabelRegister() {
        passwordLabelRegister.setVisible(false);
    }

    private boolean checkRegisterEmpty() {
        System.out.println(emailTextFieldRegister.getText() + " " + usernameTextFieldRegister.getText() + " " + passwordTextFieldRegister.getText());
        System.out.println(emailTextFieldRegister.getText().equals("") + " " + usernameTextFieldRegister.getText().equals("") + " " + passwordTextFieldRegister.getText().equals(""));
        boolean isEmpty = false;
        if (emailTextFieldRegister.getText().equals("")) {
            emailLabelRegister.setVisible(true);
            emailLabelRegister.setText("Email must not be empty!");
            isEmpty = true;
        }
        if (usernameTextFieldRegister.getText().equals("")) {
            usernameLabelRegister.setVisible(true);
            usernameLabelRegister.setText("Username must not be empty!");
            isEmpty = true;
        }
        if (passwordTextFieldRegister.getText().equals("")) {
            passwordLabelRegister.setVisible(true);
            passwordLabelRegister.setText("Password must not be empty!");
            isEmpty = true;
        }
        return isEmpty;
    }
}
