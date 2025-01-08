package agh.to.lab.cinema.viewController.admin;

import agh.to.lab.cinema.app.CinemaApp;
import agh.to.lab.cinema.model.purchases.Purchase;
import agh.to.lab.cinema.model.roles.Role;
import agh.to.lab.cinema.model.roles.RoleType;
import agh.to.lab.cinema.model.users.CinemaUser;
import agh.to.lab.cinema.restController.UserController;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javafx.util.StringConverter;
import javafx.util.converter.LocalDateTimeStringConverter;

public class AdminUsersController extends AdminController {
    @FXML
    TextField changeRoleTextField;

    ObservableList<String> roleOptions;

    @FXML
    ComboBox changeRoleComboBox = new ComboBox(roleOptions);

    @FXML
    TextField deleteUserTextField;

    @FXML
    Label deleteUserLabel;

    @FXML
    Label changeRoleLabel;

    // Table
    @FXML
    TableView<CinemaUser> adminUsersTable;
    @FXML
    TableColumn<CinemaUser, Integer> userId;
    @FXML
    TableColumn<CinemaUser, String> userUsername;
    @FXML
    TableColumn<CinemaUser, String> userEmail;
    @FXML
    TableColumn<CinemaUser, String> userRole;
    @FXML
    TableColumn<CinemaUser, String> userCreationDate;
    @FXML
    TableColumn<CinemaUser, List<Purchase>> userPurchases;

    @FXML
    private void initialize() throws Exception{
        String url = UserController.getBaseUrl();
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Accept", "application/json")
                .GET().
                build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        ObservableList<CinemaUser> users = FXCollections.observableArrayList(new ObjectMapper().readValue(response.body(), CinemaUser[].class));

        userId.setCellValueFactory(new PropertyValueFactory<>("id"));
        userUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        userEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        userRole.setCellValueFactory(cellData -> Bindings.createStringBinding(() -> cellData.getValue().getRole().getRoleToString()));

        String pattern = "yyyy-MM-dd HH:mm:ss";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        LocalDateTimeStringConverter converter = new LocalDateTimeStringConverter(formatter, formatter);
        userCreationDate.setCellValueFactory(cellData -> Bindings.createStringBinding(() -> converter.toString(cellData.getValue().getCreatedAt())));

        //userCreationDate.setCellValueFactory(new PropertyValueFactory<>("createdAt"));
        userPurchases.setCellValueFactory(new PropertyValueFactory<>("purchases"));
        adminUsersTable.setItems(users);

        List<Role> roles = Arrays.stream(RoleType.values())
                        .map(Role::new)
                        .collect(Collectors.toList());
        //roleOptions = FXCollections.observableArrayList(roles);
        changeRoleComboBox.setItems(FXCollections.observableArrayList(roles));
//        changeRoleComboBox.setItems(FXCollections.observableArrayList(
//                Arrays.stream(RoleType.values())
//                        .map(Enum::toString)
//                        .collect(Collectors.toList())
//        ));

        changeRoleComboBox.setConverter(new StringConverter<Role>() {
            @Override
            public String toString(Role role) {
                return role.getRoleToString();
            }

            @Override
            public Role fromString(String string) {
                return roles.stream()
                        .filter(role -> role.getRoleToString().equals(string))
                        .findFirst()
                        .orElse(null);
            }
        });
    }

    @FXML
    private String deleteUser() throws Exception {
        String url = UserController.getBaseUrl();
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Accept", "application/json")
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        List<CinemaUser> users = Arrays.asList(new ObjectMapper().readValue(response.body(), CinemaUser[].class));
        CinemaUser foundUser = null;
        for (CinemaUser user: users) {
            if (user.getUsername().equals(deleteUserTextField.getText())) {
                foundUser = user;
            }
        }
        if (foundUser != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete account");
            alert.setHeaderText("Are you sure you want to delete account of " + foundUser.getUsername() + "?");
            ButtonType result = alert.showAndWait().get();
            if (result.equals(ButtonType.OK)) {
                String baseUrl = url + "/delete/";
                String deleteUrl = baseUrl.concat(String.valueOf(foundUser.getId()));
                HttpClient deleteClient = HttpClient.newHttpClient();
                HttpRequest deleteRequest = HttpRequest.newBuilder()
                        .uri(URI.create(deleteUrl))
                        .header("Accept", "application/json")
                        .DELETE()
                        .build();
                HttpResponse<String> deleteResponse = deleteClient.send(deleteRequest, HttpResponse.BodyHandlers.ofString());
                System.out.println(deleteResponse.body());
                deleteUserLabel.setText("Deleted user: " + foundUser.getUsername());
                deleteUserLabel.setVisible(true);
                if (foundUser.getUsername().equals("admin")) {
                    CinemaApp.setLoggedUser(null);
                    CinemaApp.loadView("views/login.fxml");
                }
            }
        } else {
            deleteUserLabel.setText("This user does not exist!");
            deleteUserLabel.setVisible(true);
        }
        System.out.println(response.body());
        return response.body();
    }

    @FXML
    private String changeUserRole() throws Exception {
        String url = UserController.getBaseUrl();
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Accept", "application/json")
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        List<CinemaUser> users = Arrays.asList(new ObjectMapper().readValue(response.body(), CinemaUser[].class));
        CinemaUser foundUser = null;
        for (CinemaUser user: users) {
            if (user.getUsername().equals(changeRoleTextField.getText())) {
                foundUser = user;
            }
        }
        Role selectedRole = (Role) changeRoleComboBox.getValue();
        if (foundUser != null && selectedRole != null) {
            //foundUser.setRole(selectedRole);
            //System.out.println(foundUser.getRole().getRole().getRoleName());
            //System.out.println("Changed " + foundUser.getUsername() + " role to " + foundUser.getRole().getRole().getRoleName());
            String updateUrl = UserController.getBaseUrl() + "/update_role/" + foundUser.getId();
            //System.out.println(updateUrl);
            HttpRequest updateRequest = HttpRequest.newBuilder()
                    .uri(URI.create(updateUrl))
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(new ObjectMapper().writeValueAsString(selectedRole)))
                    .build();
            HttpResponse<String> updateResponse = client.send(updateRequest, HttpResponse.BodyHandlers.ofString());
            System.out.println(updateResponse.body());



        }
        return "lala";
    }

}
