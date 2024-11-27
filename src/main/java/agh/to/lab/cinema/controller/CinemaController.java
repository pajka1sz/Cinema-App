package agh.to.lab.cinema.controller;

import javafx.fxml.FXML;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class CinemaController {

    @FXML
    private String sendRegisterPostRequest() throws Exception {
        System.out.println(JsonBodyCreator.createCinemaUserBody("test3", "dupadupadupa", "test3"));
        String url = "http://localhost:8080/api/register";
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(JsonBodyCreator.createCinemaUserBody("test3", "dupadupadupa", "test3@gmail.com")))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
        return response.body();
    }
}
