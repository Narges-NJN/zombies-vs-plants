package model;

import controller.PlayGroundController;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Iterator;

public class Bomb extends Plants{
    ImageView fire;
    protected int lane;
    GridPane lawn_grid;
    Pane lawn_pain;
    Timeline bombTimeLine;
    public Bomb(int x, int y, int col, int row, GridPane lawn_grid, Pane lawn_pain) {
        super("/image/PNG/mine.png", x, y, 50, 300, 70, 60, col, row);
        this.lane = row;
        this.lawn_grid = lawn_grid;
        this.lawn_pain = lawn_pain;
    }

    public void showFire() {
        fire = new ImageView();
        Image img = new Image("/image/GIF/fire.gif", 180, 160,false,false);
        fire.setImage(img);
        fire.setX(x-30);
        fire.setY(y-20);
        lawn_pain.getChildren().add(fire);
        fire.setVisible(false);
    }

    @Override
    public void attack(Pane pane) {
        showFire();
        bombTimeLine = new Timeline(new KeyFrame(Duration.millis(100), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                synchronized (PlayGroundController.allZombies) {
                    Iterator<Zombies> i = PlayGroundController.allZombies.iterator();
                    while(i.hasNext()) {
                        Zombies z = i.next();
                        if(z.getLine() == lane){
                            if ( Math.abs(z.getX() - x) <= 50) {
                                fire.setVisible(true);
                                removeBomb();
                                removeNearZombies();
                                bombTimeLine.stop();
                                removeFire();
                            }
                        }
                    }
                }
            }
        }));
        bombTimeLine.setCycleCount(Timeline.INDEFINITE);
        bombTimeLine.play();
        PlayGroundController.animationTimelines.add(bombTimeLine);
    }

    private void removeFire() {
        Thread t = new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            image.setVisible(false);
            image.setDisable(false);
            fire.setVisible(false);
            fire.setDisable(false);
            stopAnimations();
        });
        t.start();
    }

    private void removeNearZombies() {
        synchronized (PlayGroundController.allZombies) {
            Iterator<Zombies> j = PlayGroundController.allZombies.iterator();
            while(j.hasNext()) {
                Zombies x = j.next();
                if(x.getX()<=(getX()+250) && x.getX()>=(getX()-150))
                {
                    if(x.getY()<=(getY()+250) && x.getY()>=(getY()-150)) {
                        PlayGroundController.allZombies.remove(j);
                        x.image.setVisible(false);
                        x.image.setDisable(true);
                        x.pace = 0;
                        x.endAnimation(x.zombieAnimation);
                    }
                }
            }
        }
    }

    private void removeBomb() {
        PlayGroundController.allPlants.remove(this);
    }

    @Override
    public void stopAnimations() {
        bombTimeLine.stop();
    }
}
