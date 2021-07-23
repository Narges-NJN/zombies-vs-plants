package model.plants;

import controller.PlayGroundController;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import model.zombies.Zombies;

import java.util.Iterator;

public abstract class Shooter extends Plants {
    protected Timeline shooterTimeline;
    public Timeline peaTimeline;
    protected int lane;

    public static void setPeaTimeline(Timeline peaTimeline) {
    }

    public Shooter(String path, int x, int y, int price, int energy, int width, int height, int col, int row) {
        super(path, x, y, price, energy, width, height, col, row);
        this.lane = row;
    }

    @Override
    public void attack(Pane pane) {
        shooterTimeline = new Timeline(new KeyFrame(Duration.millis(2000), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                synchronized (PlayGroundController.allZombies) {
                    Iterator<Zombies> i = PlayGroundController.allZombies.iterator();
                    while (i.hasNext()) {
                        Zombies z = i.next();
                        if (z.getX() < PlayGroundController.boundXCoordinate-95 && z.getLine() == getShooterLane() && getX() <= z.getX()) {
                            customizedShoot(pane);
                            checkEnergy();
                        }
                    }
                }
            }
        }));
        shooterTimeline.setCycleCount(Timeline.INDEFINITE);
        shooterTimeline.play();
        PlayGroundController.animationTimelines.add(shooterTimeline);
    }

    public abstract void customizedShoot(Pane pane);

    public Timeline getShooterTimeline() {
        return (this.shooterTimeline);
    }

    public int getShooterLane() {
        return (this.lane);
    }

    public void checkEnergy() {
        if (getEnergy() <= 0)
            endAnimation(this.shooterTimeline);
    }
    @Override
    public void stopAnimations(){
        shooterTimeline.stop();
        if(peaTimeline!=null)
            peaTimeline.stop();
    }
}