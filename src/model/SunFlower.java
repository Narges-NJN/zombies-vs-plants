package model;

import controller.PlayGroundController;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import util.Style;

public class SunFlower extends Plants{
    public Timeline sunPopper;
    public Timeline startGlow;
    private Timeline stopGlow;
    public SunFlower(int x, int y, int col, int row) {
        super("/image/GIF/sunflower.gif", x, y, 50, 500, 70, 100, col, row);
    }

    @Override
    public void attack(Pane pane) {popSun(pane); }

    @Override
    public void stopAnimations() {
        this.sunPopper.stop();
        this.stopGlow.stop();
        this.startGlow.stop();
        System.out.println("stopped");
        PlayGroundController.animationTimelines.remove(this.sunPopper);
    }

    private void popSun(Pane pane) {
        stopGlow = new Timeline(new KeyFrame(Duration.seconds(2), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Style.Glow(image,0);
            }
        }));

        sunPopper = new Timeline(new KeyFrame(Duration.seconds(2), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                checkEnergy();
                if(getEnergy()>0)
                {
                    Sun s = new Sun(getX()+20, getY()+30, false);
                    s.setImage(pane);
                }
                stopGlow.play();
            }
        }));
        startGlow = new Timeline(new KeyFrame(Duration.seconds(6), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Style.Glow(image,0.7);
                sunPopper.play();
            }
        }));
        startGlow.setCycleCount(Timeline.INDEFINITE);
        startGlow.play();
        PlayGroundController.animationTimelines.add(sunPopper);
        PlayGroundController.animationTimelines.add(startGlow);
        PlayGroundController.animationTimelines.add(stopGlow);
    }

    public void checkEnergy() {
        if (getEnergy() <= 0)
            endAnimation(this.sunPopper);
    }


}
