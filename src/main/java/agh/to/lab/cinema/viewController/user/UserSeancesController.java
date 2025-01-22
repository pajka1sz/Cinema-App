package agh.to.lab.cinema.viewController.user;

import agh.to.lab.cinema.app.CinemaApp;
import agh.to.lab.cinema.model.purchases.Purchase;
import agh.to.lab.cinema.model.seances.Seance;
import agh.to.lab.cinema.restController.PurchaseController;
import agh.to.lab.cinema.restController.SeanceController;
import agh.to.lab.cinema.viewController.JsonBodyCreator;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.Optional;

public class UserSeancesController {
    @FXML
    private TableColumn<Seance, LocalDateTime> dateColumn;
    @FXML
    private TableColumn<Seance, Integer> priceColumn;
    @FXML
    private TableColumn<Seance, String> roomColumn;
    @FXML
    private TableView<Seance> seancesTable;
    @FXML
    private TableColumn<Seance, Integer> availableSeatsColumn;
    @FXML
    private Text movieDescription;
    @FXML
    private Label thumbnailLabel;
    @FXML
    private ImageView thumbnail;

    public void initialize() throws IOException, InterruptedException {
        thumbnail.setImage(new Image(CinemaApp.getMovie().getThumbnail()));
        thumbnailLabel.setText(CinemaApp.getMovie().getTitle());
        movieDescription.setText(CinemaApp.getMovie().getDescription());
        String baseUrl = SeanceController.getBaseUrl();
        HttpClient seanceClient = HttpClient.newHttpClient();
        HttpRequest moviesRequest = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl))
                .GET()
                .build();
        HttpResponse<String> response = seanceClient.send(moviesRequest, HttpResponse.BodyHandlers.ofString());
        ObservableList<Seance> seances = FXCollections.observableArrayList(new ObjectMapper().readValue(response.body(), Seance[].class));
        System.out.println(seances);
        seances = FXCollections.observableArrayList(
                seances.stream()
                        .filter(seance -> seance.getMovie().getId().equals(CinemaApp.getMovie().getId()))
                        .filter(seance -> seance.getStartDate().isAfter(LocalDateTime.now()))
                        .toList() // or collect(Collectors.toList())
        );        seancesTable.setItems(seances);
        seancesTable.setRowFactory(tableView -> {
            TableRow<Seance> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty()) { // Ensures the click is on a row and not an empty area
                    Seance seance = row.getItem(); // Get the Seance object for the clicked row
                    showSeanceDetails(seance); // Trigger your method
                }
            });
            return row;
        });
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        roomColumn.setCellValueFactory(new PropertyValueFactory<>("room"));
        availableSeatsColumn.setCellValueFactory(cell -> {
            Seance seance = cell.getValue();
            return new SimpleIntegerProperty(seance.getRoom().getCapacity() - seance.getPurchases().stream().mapToInt(Purchase::getNumberOfTickets).sum()).asObject();
        });
    }

    public void backToMoviesList() {
        CinemaApp.loadView("views/user/userDefault.fxml");
    }

    public void showSeanceDetails(Seance seance) {
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setTitle("Reservation overview");
        alert.setHeaderText("Seance details");
        // Set the button types
        ButtonType okButton = new ButtonType("Reserve", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getDialogPane().getButtonTypes().addAll(okButton, cancelButton);
        if (seance != null) {
            VBox vBox = new VBox();
            vBox.getChildren().add(new Label("Seance date: " + seance.getStartDate()));
            vBox.getChildren().add(new Label("Price: " + seance.getPrice()));
            vBox.getChildren().add(new Label("Room: " + seance.getRoom().getNumber()));
            vBox.getChildren().add(new Label("Available seats: " + (seance.getRoom().getCapacity() - seance.getPurchases().stream().mapToInt(Purchase::getNumberOfTickets).sum())));
            alert.getDialogPane().setContent(vBox);
        }
        ButtonType result = alert.showAndWait().get();
        if (result == okButton) {
            // Reserve
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Reservation");
            dialog.setHeaderText("Enter number of tickets you want to reserve");
            dialog.setContentText("Tickets:");
            Optional<String> result2 = dialog.showAndWait();
            System.out.println(result2);
            int availableSeats = seance.getRoom().getCapacity() - seance.getPurchases().stream().mapToInt(Purchase::getNumberOfTickets).sum();
            result2.ifPresent(tickets -> {
                if (Integer.parseInt(tickets) > availableSeats) {
                    Alert alert2 = new Alert(Alert.AlertType.ERROR);
                    alert2.setTitle("Error");
                    alert2.setHeaderText("Not enough available seats");
                    alert2.showAndWait();
                } else {
                    // Reserve
                    System.out.println("Reserving " + tickets + " tickets for seance " + seance.getId());
                    String baseUrl = PurchaseController.getBaseUrl() + "/add";
                    HttpClient reserveClient = HttpClient.newHttpClient();
                    HttpRequest reserveRequest = HttpRequest.newBuilder()
                            .uri(URI.create(baseUrl))
                            .header("Content-Type", "application/json")
                            .POST(HttpRequest.BodyPublishers.ofString(JsonBodyCreator.createPurchaseBody(CinemaApp.getLoggedUser(), seance, Integer.parseInt(tickets)))
                            )
                            .build();
                    try {
                        HttpResponse<String> reserveResponse = reserveClient.send(reserveRequest, HttpResponse.BodyHandlers.ofString());
                        System.out.println(reserveResponse.body());
                        CinemaApp.loadView("views/user/movieDetails.fxml");
                    } catch (IOException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}
