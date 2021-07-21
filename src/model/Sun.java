package model;

import controller.PlayGroundController;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.util.Random;

public class Sun extends GameComponents{
    private final int timeOutTime;
    public Sun(int x, int y, boolean fallingSun) {
        super("/image/PNG/sun.png", x, y, 50, 50);
        if(fallingSun) timeOutTime=12000;
        else timeOutTime=4500;
        disappearAfterTime();
    }

    @Override
    public void setImage(Pane p) {
        super.setImage(p);
        // increase sun count when the player clicks on suns
        this.image.setOnMouseClicked(e->{
            this.image.setVisible(false);
            this.image.setDisable(true);
            PlayGroundController.updateSunCount(25);
        });
    }
    public static void sunRandomX(Random rand, Pane lawn_pain) {
        Timeline sunTimeline = new Timeline(new KeyFrame(Duration.seconds(5), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                int sunPosition = rand.nextInt(800);
                sunPosition += 300;
                Sun s = new Sun(sunPosition, 0, true);
                s.setImage(lawn_pain);
                s.sunFall();
            }
        }));
        sunTimeline.setCycleCount(Timeline.INDEFINITE);
        sunTimeline.play();
        PlayGroundController.animationTimelines.add(sunTimeline);
    }
    public void sunFall() {
        Timeline animation = new Timeline(new KeyFrame(Duration.millis(8),e -> moveSun()));
        animation.setCycleCount(550);
        animation.play();
        PlayGroundController.animationTimelines.add(animation);
    }
    public void moveSun() {
        if(getY()<=700)
            setY(getY()+1);
    }
    public void disappearAfterTime(){
        Thread t = new Thread(() -> {
            try {
                Thread.sleep(timeOutTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            image.setVisible(false);
            image.setDisable(true);
        });
        t.start();
    }
}
