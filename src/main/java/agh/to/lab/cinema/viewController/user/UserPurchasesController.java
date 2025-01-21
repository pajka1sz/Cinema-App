package agh.to.lab.cinema.viewController.user;

import agh.to.lab.cinema.app.CinemaApp;
import agh.to.lab.cinema.model.movies.Movie;
import agh.to.lab.cinema.model.purchases.Purchase;
import agh.to.lab.cinema.model.rates.MovieRate;
import agh.to.lab.cinema.model.users.CinemaUser;
import agh.to.lab.cinema.restController.MovieRateController;
import agh.to.lab.cinema.restController.PurchaseController;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;

import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.converter.LocalDateTimeStringConverter;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserPurchasesController {
    @FXML
    TableView<Purchase> userPurchasesTable;
    @FXML
    TableColumn<Purchase, String> movieTitleColumn;
    @FXML
    TableColumn<Purchase, Float> seancePriceColumn;
    @FXML
    TableColumn<Purchase, String> seanceDateColumn;
    @FXML
    TableColumn<Purchase, Integer> numberOfTicketsColumn;
    @FXML
    Button deletePurchasesButton;
    @FXML
    Button addRateButton;

    public void initialize() throws IOException, InterruptedException {
        String baseUrl = PurchaseController.getBaseUrl();
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest purchasesRequest = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl))
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(purchasesRequest, HttpResponse.BodyHandlers.ofString());
        ObservableList<Purchase> purchases = FXCollections.observableArrayList(new ObjectMapper().readValue(response.body(), Purchase[].class));
        purchases = FXCollections.observableArrayList(
                purchases.stream()
                        .filter(purchase -> purchase.getUser().getId().equals(CinemaApp.getLoggedUser().getId()))
                        .toList()
        );
        System.out.println(purchases);
        userPurchasesTable.setItems(purchases);

        userPurchasesTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        movieTitleColumn.setCellValueFactory(cellData ->
                Bindings.createStringBinding(() -> cellData.getValue().getSeance().getMovie().getTitle()));
        seancePriceColumn.setCellValueFactory(cellData ->
                Bindings.createFloatBinding(() -> cellData.getValue().getSeance().getPrice()).asObject());

        String pattern = "yyyy-MM-dd HH:mm:ss";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        LocalDateTimeStringConverter converter = new LocalDateTimeStringConverter(formatter, formatter);
        seanceDateColumn.setCellValueFactory(cellData ->
                Bindings.createStringBinding(() -> converter.toString(cellData.getValue().getSeance().getStartDate())));

        numberOfTicketsColumn.setCellValueFactory(new PropertyValueFactory<>("numberOfTickets"));

        deletePurchasesButton.disableProperty().bind(Bindings.isEmpty(userPurchasesTable.getSelectionModel().getSelectedItems()));
        addRateButton.disableProperty().bind(Bindings.size(userPurchasesTable.getSelectionModel().getSelectedItems()).isNotEqualTo(1));
    }

    public void backToUserInfo() {
        CinemaApp.loadView("views/user/userInfo.fxml");
    }

    public void deletePurchases() {
        List<Purchase> purchasesToDelete = userPurchasesTable.getSelectionModel().getSelectedItems();

        int purchasesThatCannotBeDeletedNo = 0;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete reservations");
        alert.setHeaderText("Are you sure you want to delete these reservations?");
        ButtonType result = alert.showAndWait().get();
        if (result.equals(ButtonType.OK)) {
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
                    if (response.statusCode() == HttpStatus.BAD_REQUEST.value()) {
                        purchasesThatCannotBeDeletedNo += 1;
                    }
                    System.out.println(response.body());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        if (purchasesThatCannotBeDeletedNo > 0) {
            Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
            alert1.setTitle("Deletion of old reservations");
            alert1.setHeaderText("You cannot delete old reservations");
            alert1.setContentText("You tried to delete the reservations that already happened, so only the future ones were deleted.");
            alert1.showAndWait();
        }

        try {
            initialize();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void addRate() {
        showRateWindow(userPurchasesTable.getSelectionModel().getSelectedItem().getSeance().getMovie(), CinemaApp.getLoggedUser());
    }

    private void showRateWindow(Movie movie, CinemaUser user) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(UserDefaultController.class.getClassLoader().getResource("views/user/userRateAddDialog.fxml"));
            BorderPane page = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Add rate");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(CinemaApp.getPrimaryStage());
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            UserAddRatePresenter presenter = loader.getController();
            presenter.setDialogStage(dialogStage);
            presenter.setData(movie, user);

            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
