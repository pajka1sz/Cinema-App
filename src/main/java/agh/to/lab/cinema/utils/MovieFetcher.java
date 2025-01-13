package agh.to.lab.cinema.utils;

import agh.to.lab.cinema.model.movies.MovieDTO;
import agh.to.lab.cinema.model.types.MovieType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

public class MovieFetcher {

    private static final String API_URL = "http://www.omdbapi.com/";
    private static final String API_KEY = "d7f1697c";

    public MovieDTO getMovieDetails(String movieTitle) throws Exception {
        OkHttpClient client = new OkHttpClient();

        String url = API_URL + "?t=" + movieTitle.replace(" ", "+") + "&apikey=" + API_KEY;

        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new RuntimeException("Request failed: " + response);
            }

            String responseBody = response.body().string();
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(responseBody);

            if (jsonNode.has("Error")) {
                throw new RuntimeException("Movie not found: " + jsonNode.get("Error").asText());
            }

            MovieDTO movieDTO = new MovieDTO();
            movieDTO.setDescription(jsonNode.get("Plot").asText());
            movieDTO.setTitle(jsonNode.get("Title").asText());
            movieDTO.setLength(Integer.valueOf(jsonNode.get("Runtime").asText().split(" ")[0]));
            movieDTO.setThumbnail(jsonNode.get("Poster").asText());
            movieDTO.setTypes(Arrays.stream(jsonNode.get("Genre").asText().split(",\\s*"))
                    .filter(MovieType::isTypeValid)
                    .map(MovieType::fromString)
                    .filter(Objects::nonNull)
                    .map(MovieType::toString)
                    .collect(Collectors.toSet()));

            return movieDTO;
        }
    }
}

