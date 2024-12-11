package agh.to.lab.cinema.controller;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonBodyCreator {
    public static String createCinemaUserBody(String username, String password, String email) {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.createObjectNode()
                .put("username", username)
                .put("email", email)
                .put("password", password)
                .toString();
    }
}
