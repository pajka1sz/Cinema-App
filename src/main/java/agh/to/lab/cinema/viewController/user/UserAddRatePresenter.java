package agh.to.lab.cinema.viewController.user;

import agh.to.lab.cinema.model.movies.Movie;
import agh.to.lab.cinema.model.users.CinemaUser;
import agh.to.lab.cinema.restController.MovieRateController;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class UserAddRatePresenter {
    private Movie movie;
    private CinemaUser user;
    private Stage stage;
    @FXML
    private TextField rateMovieTitleTextField;
    @FXML
    private TextField rateRateTextField;
    @FXML
    private TextField rateDescriptionTextField;

    public UserAddRatePresenter(Movie movie, CinemaUser user) {
        this.movie = movie;
        this.user = user;
    }

    public UserAddRatePresenter() {

    }

    public void setDialogStage(Stage stage) {
        this.stage = stage;
    }

    public void setData(Movie movie, CinemaUser user) {
        System.out.println(movie.toString());
        System.out.println(user.toString());
        this.movie = movie;
        this.user = user;

        rateMovieTitleTextField.setText(movie.getTitle());
        rateMovieTitleTextField.setEditable(false);
    }

    @FXML
    private void handleOkAction() {
        try {
            HttpClient client = HttpClient.newHttpClient();
            String url = MovieRateController.getBaseUrl() + "/add";
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(new ObjectMapper().createObjectNode()
                            .put("movie_id", movie.getId())
                            .put("user_id", user.getId())
                            .put("rate", Integer.valueOf(rateRateTextField.getText()))
                            .put("description", rateDescriptionTextField.getText())
                            .toString()))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response.body());

            stage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleCancelAction() {
        stage.close();
    }
}
