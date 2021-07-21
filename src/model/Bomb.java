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
    private ArrayList<Zombies> roastedZombies;
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
        this.roastedZombies = new ArrayList<Zombies>();
    }

    public void showFire() {
        fire = new ImageView();
        System.out.println("1");
        Image img = new Image("/image/GIF/fire.gif", 180, 160,false,false);
        System.out.println("2");
        fire.setImage(img);
        fire.setX(x-30);
        fire.setY(y-20);
        lawn_pain.getChildren().add(fire);
        fire.setVisible(false);
    }

    @Override
    public void attack(Pane pane) {
        bombTimeLine = new Timeline(new KeyFrame(Duration.millis(100), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                showFire();
                synchronized (PlayGroundController.allZombies) {
                    Iterator<Zombies> i = PlayGroundController.allZombies.iterator();
                    while(i.hasNext()) {
                        Zombies z = i.next();
                        if(z.getLane() == lane){
                            if ( Math.abs(z.getX() - x) <= 50) {
                                fire.setVisible(true);
                                PlayGroundController.allPlants.remove(this);
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
                                bombTimeLine.stop();
                                Thread t = new Thread(() -> {
                                    try {
                                        Thread.sleep(1000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    image.setVisible(false);
                                    fire.setVisible(false);
                                    stopAnimations();
                                });
                                t.start();
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

    @Override
    public void stopAnimations() {
        bombTimeLine.stop();
    }
}
