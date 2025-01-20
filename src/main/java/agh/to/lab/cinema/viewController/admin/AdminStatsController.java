package agh.to.lab.cinema.viewController.admin;

import agh.to.lab.cinema.restController.StatisticsController;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

public class AdminStatsController extends AdminController{
    @FXML
    Text statsText;

    @FXML
    public void initialize() {
        StringBuilder sb = new StringBuilder();
        String url = StatisticsController.getBaseUrl() + "/user_most_money";
        sb.append("User who spend the most money: ").append(sendGet(url)).append("\n");
        statsText.setText(sb.toString());
    }
}
