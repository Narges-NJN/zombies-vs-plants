package controller;

import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.util.Duration;
import util.FileHandler;
import util.SoundHandler;
import util.Style;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MenuPageController implements Initializable {
    public AnchorPane rout_pane;
    public Group start_button;
    public Group recorde_button;
    public Group exit_button;
    public AnchorPane play_sub_scene;
    public AnchorPane records_sub_scene;
    public Button play_button;
    public RadioButton level1;
    public RadioButton level2;
    public RadioButton level3;
    public TextField user_name_field;
    public Group login_sub_scene;
    public Text error_text;
    public Label high_score_label;
    boolean isHidden = true;
    public String fxmlPath;
    public static SoundHandler gameSong;


    public void playClicked(MouseEvent mouseEvent) throws IOException {
        if(user_name_field.getText() != ""){
            saveUserName();
            login_sub_scene.getChildren().clear();
            moveSubScene(play_sub_scene, records_sub_scene);
        }
        else
            showAlert();
    }

    public void recordsClicked(MouseEvent mouseEvent) {
        if(user_name_field.getText() != ""){
            saveUserName();
            login_sub_scene.getChildren().clear();
            moveSubScene(records_sub_scene, play_sub_scene);
            high_score_label.setText(FileHandler.userDataMap.values().toString().replace("[","").replace("]",""));
        }
        else
            showAlert();
    }

    private void saveUserName() {
        Boolean found = FileHandler.searchForUserName(user_name_field.getText());
        if(!found)
            FileHandler.writeInFile(user_name_field.getText(),0);
    }

    private void showAlert() {
        error_text.setOpacity(100);
    }

    public void exitClicked(MouseEvent mouseEvent) {
        Alert alert = new Alert(Alert.AlertType.NONE, "Do you want to exit the game?", ButtonType.NO, ButtonType.YES);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.YES)
            System.exit(0);
    }

    public void moveSubScene(AnchorPane nodeToAppear, AnchorPane nodeToHide){
        TranslateTransition appear = new TranslateTransition();
        TranslateTransition hide = new TranslateTransition();
        appear.setDuration(Duration.seconds(0.4));
        hide.setDuration(Duration.seconds(0.4));
        appear.setNode(nodeToAppear);
        hide.setNode(nodeToHide);
        if (isHidden) {
            hide.setToX(0);
            appear.setToX(-750);
            isHidden = false;
        } else {
            hide.setToX(0);
            appear.setToX(-750);
            isHidden = true ;
        }
        appear.play();
        hide.play();
    }

    public void playOnClick(ActionEvent actionEvent) throws IOException {
        if(PlayGroundController.level!= -1){
            AnchorPane pane= FXMLLoader.load(getClass().getResource(this.fxmlPath));
            rout_pane.getChildren().setAll(pane);
        }
    }

    public void level1(ActionEvent actionEvent) {
        if(level1.isSelected()){
            PlayGroundController.level = 1;
            level2.setSelected(false);
            level3.setSelected(false);
            this.fxmlPath = "../view/game_play_ground1.fxml";
        }
    }

    public void level2(ActionEvent actionEvent) {
        if(level2.isSelected()){
            PlayGroundController.level = 2;
            level1.setSelected(false);
            level3.setSelected(false);
            this.fxmlPath = "../view/game_play_ground2.fxml";
        }
    }

    public void level3(ActionEvent actionEvent) {
        if(level3.isSelected()){
            PlayGroundController.level = 3;
            level2.setSelected(false);
            level1.setSelected(false);
            this.fxmlPath = "../view/game_play_ground3.fxml";
        }
    }

    public void setHoverButtonStyle(MouseEvent mouseEvent) {
        Style.Glow((Node) mouseEvent.getSource(),0.3);
    }

    public void setNormalButtonStyle(MouseEvent mouseEvent) {
        Style.Glow((Node) mouseEvent.getSource(),0);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        SoundHandler sound = new SoundHandler("src/sound/game_music.wav");
        sound.play();
        gameSong = sound;
    }
}
