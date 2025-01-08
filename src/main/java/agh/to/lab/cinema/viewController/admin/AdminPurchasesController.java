package agh.to.lab.cinema.viewController.admin;

import agh.to.lab.cinema.model.purchases.Purchase;
import agh.to.lab.cinema.restController.PurchaseController;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.converter.LocalDateTimeStringConverter;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AdminPurchasesController extends AdminController {
    // Table
    @FXML
    private TableView<Purchase> adminPurchasesTable;
    @FXML
    private TableColumn<Purchase, String> userMail;
    @FXML
    private TableColumn<Purchase, String> userName;
    @FXML
    private TableColumn<Purchase, Integer> numberOfTickets;
    @FXML
    private TableColumn<Purchase, String> movieTitle;
    @FXML
    private TableColumn<Purchase, String> seanceDate;

    // Buttons
    @FXML
    private Button deletePurchasesButton;

    @FXML
    private void initialize() throws IOException, InterruptedException {
        String url = PurchaseController.getBaseUrl();
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Accept", "application/json")
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        ObservableList<Purchase> purchases = FXCollections.observableArrayList(new ObjectMapper().readValue(response.body(), Purchase[].class));
        System.out.println(response.body());

        adminPurchasesTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        userMail.setCellValueFactory(cellData ->
                Bindings.createStringBinding(() -> cellData.getValue().getUser().getEmail()));
        userName.setCellValueFactory(cellData ->
                Bindings.createStringBinding(() -> cellData.getValue().getUser().getUsername()));
        numberOfTickets.setCellValueFactory(new PropertyValueFactory<>("numberOfTickets"));
        movieTitle.setCellValueFactory(cellData ->
                Bindings.createStringBinding(() -> cellData.getValue().getSeance().getMovie().getTitle()));

        String pattern = "yyyy-MM-dd HH:mm:ss";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        LocalDateTimeStringConverter converter = new LocalDateTimeStringConverter(formatter, formatter);
        seanceDate.setCellValueFactory(cellData ->
                Bindings.createStringBinding(() -> converter.toString(cellData.getValue().getSeance().getStartDate())));


        adminPurchasesTable.setItems(purchases);

        deletePurchasesButton.disableProperty().bind(Bindings.isEmpty(adminPurchasesTable.getSelectionModel().getSelectedItems()));
    }

    @FXML
    private void deletePurchases() {
        List<Purchase> purchasesToDelete = adminPurchasesTable.getSelectionModel().getSelectedItems();

        for (Purchase purchase: purchasesToDelete) {
            try {
                String url = PurchaseController.getBaseUrl() + "/delete/" + purchase.getId();
                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(url))
                        .header("Content-Type", "application/json")
                        .DELETE()
                        .build();
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                System.out.println(response.body());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        try {
            initialize();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
