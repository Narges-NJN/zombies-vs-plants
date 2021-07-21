package controller;

import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import util.FileHandler;

import java.net.URL;
import java.util.ResourceBundle;

public class GameOverController implements Initializable {
    public AnchorPane game_over_page;
    public Label current_score;
    public Label latest_high_score;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String userName = FileHandler.userDataMap.keySet().toString().replace("[","").replace("]","");
        String latestHighScore = FileHandler.userDataMap.values().toString().replace("[","").replace("]","");
        current_score.setText(String.valueOf(PlayGroundController.score));
        latest_high_score.setText(latestHighScore);
        if (Integer.parseInt(latestHighScore) < PlayGroundController.score){
            FileHandler.updateRecord(userName, String.valueOf(PlayGroundController.score));
        }
    }

    public void mainMenuOnClick(MouseEvent mouseEvent) {
    }

    public void exitOnClick(MouseEvent mouseEvent) {
        System.exit(0);
    }
}