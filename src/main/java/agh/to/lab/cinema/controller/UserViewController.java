package agh.to.lab.cinema.controller;

import agh.to.lab.cinema.app.CinemaApp;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class UserViewController {
    @FXML
    Label usernameLabel;
    @FXML
    Label emailLabel;


    @FXML
    private void initialize() {
        usernameLabel.setText(CinemaApp.getCinemaUser().getUsername());
        emailLabel.setText(CinemaApp.getCinemaUser().getEmail());
    }

    @FXML
    private void logOut() {
        CinemaApp.setCinemaUser(null);
        CinemaApp.loadView("views/login.fxml");
    }
}
