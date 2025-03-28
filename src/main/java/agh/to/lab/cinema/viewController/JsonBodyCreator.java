package agh.to.lab.cinema.viewController;
import agh.to.lab.cinema.model.movies.Movie;
import agh.to.lab.cinema.model.rooms.Room;
import agh.to.lab.cinema.model.seances.Seance;
import agh.to.lab.cinema.model.types.Type;
import agh.to.lab.cinema.model.users.CinemaUser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public class JsonBodyCreator {
    public static String createCinemaUserBody(String username, String password, String email) {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.createObjectNode()
                .put("username", username)
                .put("email", email)
                .put("password", password)
                .toString();
    }

    public static String createMovieBody(String title, String description, Integer length, String thumbnail, Set<Type> movieTypes) {
        ObjectMapper objectMapper = new ObjectMapper();
        List<String> types = movieTypes.stream()
                .map(Type::toString)
                .toList();
        return objectMapper.createObjectNode()
                .put("title", title)
                .put("description", description)
                .put("length", length)
                .put("thumbnail", thumbnail)
                .putPOJO("types", types)
                .toString();
    }

    public static String createRoomBody(Integer number, Integer capacity) {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.createObjectNode()
                .put("number", number)
                .put("capacity", capacity)
                .toString();
    }

    public static String createSeanceBody(Movie movie, Room room, LocalDateTime startDate, float price) {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.createObjectNode()
                .put("movie", movie.getId())
                .put("room", room.getId())
                .put("startDate", startDate.toString())
                .put("price", price)
                .toString();
    }

    public static String createPurchaseBody(CinemaUser user, Seance seance, int numberOfTickets) {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.createObjectNode()
                .put("user", user.getId())
                .put("seance", seance.getId())
                .put("numberOfTickets", numberOfTickets)
                .toString();
    }

    public static String createMovieTypesBody(Set<Type> movieTypes) {
        ObjectMapper objectMapper = new ObjectMapper();
        List<String> movieTypesString = movieTypes.stream()
                .map(Type::toString)
                .toList();
        try {
            return objectMapper.writeValueAsString(movieTypesString);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
