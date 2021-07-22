package controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import model.*;
import util.FileHandler;
import util.Style;
import java.io.IOException;
import java.net.URL;
import java.util.*;


public class PlayGroundController implements Initializable {
    public static int[] lane = {75 , 195, 310, 410, 530};
    public static int score = 0;
    private static int sunCount = 25;
    private static Label static_sun_count;
    public ImageView lawn_image;
    public ImageView day_mode_icon;
    public ImageView night_mode_icon;
    private ImageView pause_icon;
    public Line boundLine;
    @FXML
    private Label sun_count_label;
    @FXML
    public AnchorPane lawn_pain;
    @FXML
    public GridPane lawn_grid;
    @FXML
    private Group sun_flower_card;
    @FXML
    private Group mine_card;
    @FXML
    private Group shooter_card;
    @FXML
    private Group advanced_shooter_card;
    @FXML
    private Label score_label;
    private static Label static_score_label;

    private int selectedCard = -1;
    private int selectedCardCost = -1;
    public static int level = -1;
    private boolean nightMode = false;
    public static ArrayList<Timeline> animationTimelines;
    public static List allZombies;
    public static List allPlants;
    public static double boundXCoordinate;
    public static int zombiesCount = 0;
    public static ArrayList<Integer> availableLanes;
    public static ArrayList<Integer> removedLines;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initiateVariables();
        Random rand = new Random();
        Sun.sunRandomX(rand, lawn_pain);
        controlZombies(rand);
        checkGameEnd();
    }

    private void initiateVariables(){
        static_sun_count = sun_count_label;
        static_score_label = score_label;
        boundXCoordinate = boundLine.getLayoutX();
        animationTimelines = new ArrayList<Timeline>();
        availableLanes = new ArrayList<Integer>();
        availableLanes.addAll(Arrays.asList(0, 1, 2, 3, 4));
        removedLines = new ArrayList<Integer>();
        allZombies = new ArrayList<Zombies>();
        allPlants = new ArrayList<Plants>();
        pause_icon = new ImageView();
        Image img = new Image("/image/PNG/pause_icon.png",(double) 30,(double) 30,false,false);
        pause_icon.setImage(img);
        night_mode_icon.setVisible(false);
    }

    private void controlZombies(Random rand){
        if(zombiesCount<5){
            runZombies(rand, 12);
            runZombies(rand, 24);
        }
        else if(5<zombiesCount && zombiesCount<10) {
            runZombies(rand, 4);
            runZombies(rand, 10);
        }
        else {
            runZombies(rand, 2);
            runZombies(rand, 4);
        }
    }

    public void checkGameEnd(){
        Timeline gameEnd = new Timeline(new KeyFrame(Duration.seconds(0.001), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(availableLanes.size() == 0){
                    stopAnimations();
                    gameOverLoader();
                }
            }
        }));
        gameEnd.setCycleCount(Timeline.INDEFINITE);
        gameEnd.play();
        animationTimelines.add(gameEnd);
    }

    public void runZombies(Random rand, double t){
        Timeline runZombie = new Timeline(new KeyFrame(Duration.seconds(t), event -> {
            int index = rand.nextInt(availableLanes.size());
            int laneNumber = availableLanes.get(index);
            int zombieType = rand.nextInt(2);
            try
            {
                if(zombieType==0)
                    NormalZombie.runNormalZombie(lawn_pain, lane[laneNumber], laneNumber, lawn_grid);
                else
                    FastZombie.runFastZombie(lawn_pain, lane[laneNumber], laneNumber, lawn_grid);
                zombiesCount ++;
            }
            catch(IndexOutOfBoundsException e) { System.out.println("exception in runZombie"); }
        }));
        runZombie.setCycleCount(Timeline.INDEFINITE);
        runZombie.play();
        animationTimelines.add(runZombie);
    }

    private void gameOverLoader() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../view/game_over.fxml"));
        Parent gameMenu = null;
        try {
            gameMenu = (Parent) fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage stage = new Stage();
        stage.setScene(new Scene(gameMenu));
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
    }

    public void sunFlowerCardSelected(MouseEvent mouseEvent) {
        selectedCard = 1;
        selectedCardCost = 50;
        Style.Glow((Node) mouseEvent.getSource(),0.4);
    }
    public void mineCardSelected(MouseEvent mouseEvent) {
        selectedCard = 2;
        selectedCardCost = 50;
        Style.Glow((Node) mouseEvent.getSource(),0.4);
    }
    public void shooterCardSelected(MouseEvent mouseEvent) {
        selectedCard = 3;
        selectedCardCost = 100;
        Style.Glow((Node) mouseEvent.getSource(),0.4);
    }
    public void advancedShooterCardSelected(MouseEvent mouseEvent) {
        selectedCard = 4;
        selectedCardCost = 200;
        Style.Glow((Node) mouseEvent.getSource(),0.4);
    }

    public void lawnGridOnClick(MouseEvent mouseEvent) {
        Node source = (Node) mouseEvent.getSource();

        // get x and y index of the regions from grid pain
        Integer colIndex = lawn_grid.getColumnIndex(source);
        Integer rowIndex = lawn_grid.getRowIndex(source);
        if(colIndex == null) colIndex = 0;
        if(rowIndex == null) rowIndex = 0;

        // Check if the line is available
        for (Integer i : removedLines) {
            if(rowIndex == i){
                selectedCard = -1;
            }
        }

        if (selectedCard != -1) {
            boolean flag = true;
            // check if the region is empty
            synchronized (allPlants) {
                Iterator<Plants> i = allPlants.iterator();
                while (i.hasNext()) {
                    Plants p = i.next();
                    // if there already exists a plant,
                    // then new plant on that region is not allowed
                    if (p.col == colIndex && p.row == rowIndex) {
                        flag = false;
                    }
                }
            }
            if (flag) {
                // if had enough money(sunCount) add the selected plant
                // and decrease the number of owned suns
                if (selectedCardCost <= sunCount) {
                    newPlant(selectedCard, (int) (source.getLayoutX() + source.getParent().getLayoutX()), (int) (source.getLayoutY() + source.getParent().getLayoutY()), colIndex, rowIndex);
                    updateSunCount((-1) * selectedCardCost);
                }
            }
        }
    }

    public void newPlant(int val, int x, int y,int col,int row) {
        switch (val) {
            case 1:
                Plants p=new SunFlower(x, y,col,row);
                allPlants.add(p);
                p.setImage(lawn_grid);
                p.attack(lawn_pain);
                Style.Glow((Node)sun_flower_card ,0);
                break;
            case 2:
                p=new Bomb(x, y, col, row, lawn_grid, lawn_pain);
                allPlants.add(p);
                p.setImage(lawn_grid);
                p.attack(lawn_pain);
                Style.Glow((Node)mine_card ,0);
                break;
            case 3:
                p = new PeaShooter(x, y, col, row);
                allPlants.add(p);
                p.setImage(lawn_grid);
                p.attack(lawn_pain);
                Style.Glow((Node)shooter_card ,0);
                break;
            case 4:
                p = new AdvancedPeaShooter(x, y, col, row);
                allPlants.add(p);
                p.setImage(lawn_grid);
                p.attack(lawn_pain);
                Style.Glow((Node)advanced_shooter_card ,0);
                break;
        }
    }

    public static void updateSunCount(int val) {
        sunCount+=val;
        getSunCountLabel().setText(Integer.toString(sunCount));
    }

    public static Label getSunCountLabel() {
        return(static_sun_count);
    }

    public static void updateScore() {
        score+=1;
        getScoreLabel().setText(Integer.toString(score));
    }

    public static Label getScoreLabel() {
        return(static_score_label);
    }

    public void pauseClicked(MouseEvent mouseEvent) throws IOException {
        String userName = FileHandler.userDataMap.keySet().toString().replace("[","").replace("]","");
        String latestHighScore = FileHandler.userDataMap.values().toString().replace("[","").replace("]","");
        pauseAnimations();
        ButtonType exit = new ButtonType("Exit", ButtonBar.ButtonData.OK_DONE);
        ButtonType resume = new ButtonType("Resume", ButtonBar.ButtonData.CANCEL_CLOSE);
        Alert alert = showPauseAlert(userName, latestHighScore, exit, resume);
        if (alert.getResult() == exit){
            System.exit(0);
            // check and update score
            if (Integer.parseInt(latestHighScore) < score){
                FileHandler.updateRecord(userName, String.valueOf(score));
            }
        }
        else startAnimations();
    }
    private Alert showPauseAlert(String userName, String latestHighScore, ButtonType exit, ButtonType resume){
        Alert alert = new Alert(Alert.AlertType.INFORMATION,
                "Go back to game or leave?",
                exit,
                resume);
        alert.initStyle(StageStyle.UNDECORATED);
        alert.setGraphic(this.pause_icon);
        alert.setTitle("Game Paused");
        alert.setHeaderText(userName + ", " + "Latest record: "+ latestHighScore + ", " + "Current Score: " + score);
        alert.showAndWait();
        return alert;
    }

    public static void startAnimations() {
        for(int i = 0; i<animationTimelines.size(); i++)
        {
            animationTimelines.get(i).play();
        }
    }
    public static void pauseAnimations() {
        for(int i = 0; i<animationTimelines.size(); i++)
        {
            animationTimelines.get(i).pause();
        }
    }
    public static void stopAnimations() {
        for(int i = 0; i<animationTimelines.size(); i++)
        {
            animationTimelines.get(i).stop();
        }
    }

    public void modeButtonOnClick(MouseEvent mouseEvent) {
       if (!nightMode){
           nightMode = true;
           night_mode_icon.setVisible(true);
           day_mode_icon.setVisible(false);
           Image img = new Image("/image/PNG/lawn_night.png",(double) 1200,(double) 700,false,false);
           lawn_image.setImage(img);

       }else {
           nightMode = false;
           night_mode_icon.setVisible(false);
           day_mode_icon.setVisible(true);
           Image img = new Image("/image/PNG/lawn.png",(double) 1200,(double) 700,false,false);
           lawn_image.setImage(img);
       }
    }
}

