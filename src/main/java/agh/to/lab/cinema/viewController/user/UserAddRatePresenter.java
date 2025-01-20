package agh.to.lab.cinema.viewController.user;

import agh.to.lab.cinema.model.movies.Movie;
import agh.to.lab.cinema.model.users.CinemaUser;
import agh.to.lab.cinema.restController.MovieRateController;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.springframework.http.HttpStatus;

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
    @FXML
    private Label rateDialogInfoLabel;
    private BooleanProperty labelVisible = new SimpleBooleanProperty(false);

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
        rateDialogInfoLabel.visibleProperty().bind(labelVisible);

        rateRateTextField.textProperty().addListener((obj, oldVal, newVal)
                -> labelVisible.setValue(newVal.trim().equals(oldVal.trim())));
        rateDescriptionTextField.textProperty().addListener((obj, oldVal, newVal)
                -> labelVisible.setValue(newVal.trim().equals(oldVal.trim())));
    }

    @FXML
    private void handleOkAction() {
        try {
            if (rateRateTextField.getText().isEmpty())
                throw new RuntimeException("Rate field must not be empty!");

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

            if (response.statusCode() == HttpStatus.OK.value()) {
                labelVisible.setValue(false);
                stage.close();
            }
            if (response.statusCode() == HttpStatus.BAD_REQUEST.value()) {
                labelVisible.setValue(true);
                rateDialogInfoLabel.setText("You have already given a rate for this film!");
            }
            else if (response.statusCode() == HttpStatus.NOT_FOUND.value()) {
                labelVisible.setValue(true);
                rateDialogInfoLabel.setText("You have not watched this film yet!");
            }
            System.out.println(response.body());
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
            labelVisible.setValue(true);
            rateDialogInfoLabel.setText("Rate field must not be empty!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleCancelAction() {
        stage.close();
    }
}
