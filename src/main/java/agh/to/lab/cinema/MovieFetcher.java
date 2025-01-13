package agh.to.lab.cinema;

import agh.to.lab.cinema.model.movies.MovieDTO;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MovieFetcher {

    private static final String API_URL = "http://www.omdbapi.com/";
    private static final String API_KEY = "d7f1697c";

    public static MovieDTO getMovieDetails(String movieTitle) throws Exception {
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

            // Parse the JSON response
            String title = jsonNode.get("Title").asText();
            String description = jsonNode.get("Plot").asText();
            String length = jsonNode.get("Runtime").asText();
            String posterUrl = jsonNode.get("Poster").asText();

            MovieDTO movieDTO = new MovieDTO();
            movieDTO.setDescription(description);
            movieDTO.setTitle(title);
            movieDTO.setThumbnail(posterUrl);
            movieDTO.setLength(Integer.valueOf(length.split(" ")[0]));
            return movieDTO;
        }
    }

    public static void main(String[] args) {
        try {
            MovieDTO movie = getMovieDetails("Mustang");
            System.out.println("Title: " + movie.getTitle());
            System.out.println("Description: " + movie.getDescription());
            System.out.println("Length: " + movie.getLength());
            System.out.println("Poster: " + movie.getThumbnail());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

