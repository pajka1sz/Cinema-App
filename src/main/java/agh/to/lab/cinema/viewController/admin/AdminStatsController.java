package agh.to.lab.cinema.viewController.admin;

import agh.to.lab.cinema.restController.StatisticsController;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

public class AdminStatsController extends AdminController{
    @FXML
    Text statsText;

    @FXML
    public void initialize() {
        String[] endpoints = {
                "/user_most_money",
                "/most_popular_room",
                "/most_rated_movie",
                "/best_rated_movie",
                "/worst_rated_movie",
                "/average_tickets_last_month"
        };
        StringBuilder sb = new StringBuilder();
        String url;
        for (String endpoint : endpoints) {
            url = StatisticsController.getBaseUrl() + endpoint;
            sb.append(sendGet(url)).append("\n");
        }
        statsText.setText(sb.toString());
    }
}
