package agh.to.lab.cinema.viewController.admin;

import agh.to.lab.cinema.app.CinemaApp;
import agh.to.lab.cinema.model.movies.Movie;
import agh.to.lab.cinema.model.rooms.Room;
import agh.to.lab.cinema.model.seances.Seance;
import agh.to.lab.cinema.model.types.MovieType;
import agh.to.lab.cinema.model.types.Type;
import agh.to.lab.cinema.restController.MovieController;
import agh.to.lab.cinema.restController.RoomController;
import agh.to.lab.cinema.viewController.JsonBodyCreator;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashSet;
import java.util.Set;

import static agh.to.lab.cinema.app.CinemaApp.loadView;

public abstract class AdminController {
    @FXML
    private void logOut() {
        CinemaApp.setLoggedUser(null);
        loadView("views/login.fxml");
    }

    @FXML
    private String addTestRecords() throws Exception{
        String url = MovieController.getBaseUrl() + "/add";
        HttpClient client = HttpClient.newHttpClient();
        Set<Type> movieTypes = new HashSet<>();
        movieTypes.add(new Type(MovieType.COMEDY));
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(JsonBodyCreator.createMovieBody("Nietykalni",
                        "Sparaliżowany milioner zatrudnia do opieki młodego chłopaka z przedmieścia, który właśnie wyszedł z więzienia.", 112,
                        "https://www.monolith.pl/wp-content/uploads/2021/07/nietykalni-nietykalni.jpg", movieTypes)))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());

        String url2 = RoomController.getBaseUrl() + "/add";
        HttpClient client2 = HttpClient.newHttpClient();
        HttpRequest request2 = HttpRequest.newBuilder()
                .uri(URI.create(url2))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(JsonBodyCreator.createRoomBody(4, 100)))
                .build();
        HttpResponse<String> response2 = client2.send(request2, HttpResponse.BodyHandlers.ofString());
        System.out.println(response2.body());

        return response.body();
    }

    @FXML
    private void getAdminUsersView() {
        loadView("views/admin/adminUsersPanel.fxml");
    }

    @FXML
    private void getAdminMoviesView() {
        loadView("views/admin/adminMoviesPanel.fxml");
    }

    @FXML
    private void getAdminSeancesView() {
        loadView("views/admin/adminSeancesPanel.fxml");
    }

    @FXML
    private void getAdminPurchasesView() {
        loadView("views/admin/adminPurchasesPanel.fxml");
    }

    @FXML
    public void getAdminRoomsView() {
        loadView("views/admin/adminRoomPanel.fxml");
    }

    @FXML
    public void getAdminStatsView() {
        loadView("views/admin/adminStatsPanel.fxml");
    }


    public boolean showMovieEditAndAddDialog(Movie movie) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(AdminController.class.getClassLoader().getResource("views/admin/movieEditOrAddDialog.fxml"));
            BorderPane page = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit or add movie");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(CinemaApp.getPrimaryStage());
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            AdminEditOrAddMoviePresenter presenter = loader.getController();
            presenter.setDialogStage(dialogStage);
            presenter.setData(movie);

            dialogStage.showAndWait();
            return presenter.isApproved();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean showRoomEditAndAddDialog(Room room) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(AdminController.class.getClassLoader().getResource("views/admin/roomEditOrAddDialog.fxml"));
            BorderPane page = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit or add room");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(CinemaApp.getPrimaryStage());
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            AdminEditOrAddRoomPresenter presenter = loader.getController();
            presenter.setDialogStage(dialogStage);
            presenter.setData(room);

            dialogStage.showAndWait();
            return presenter.isApproved();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean showSeanceEditAndAddDialog(Movie movie, Room room) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(AdminController.class.getClassLoader().getResource("views/admin/seanceEditOrAddDialog.fxml"));
            BorderPane page = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit or add seance");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(CinemaApp.getPrimaryStage());
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            AdminEditOrAddSeancePresenter presenter = loader.getController();
            presenter.setDialogStage(dialogStage);
            presenter.setData(movie, room);

            dialogStage.showAndWait();
            return presenter.isApproved();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void showInfoPopup(String message) {
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setTitle("Information");

        Label messageLabel = new Label(message);

        Button closeButton = new Button("Close");
        closeButton.setOnAction(e -> popupStage.close());

        VBox layout = new VBox(10, messageLabel, closeButton);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");

        Scene scene = new Scene(layout, 300, 150);
        popupStage.setScene(scene);

        popupStage.showAndWait();
    }

    protected String sendGet(String url) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }
}
