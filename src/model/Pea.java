package model;

import controller.PlayGroundController;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import java.util.Iterator;

public class Pea extends GameComponents{
    private int lane;
    private int plantLocation;
    transient private Timeline peaAnimation;
    private boolean flag;

    public Pea(int x, int y, int plantLocation, int lane) {
        super("/image/PNG/pea.png", x, y, 30, 30);
        this.plantLocation = plantLocation;
        this.lane = lane;
    }
    public void movePea(){
        if(x<=1198)
            setX(getX()+1);

        if(this.plantLocation > getX())
            image.setVisible(false);
        else
            image.setVisible(true);

        checkZombieCollision();
    }

    public void shootPea(){
        Timeline animation = new Timeline(new KeyFrame(Duration.millis(3), e -> movePea()));
        animation.setCycleCount(1198);
        animation.play();
        this.peaAnimation = animation;
        PlayGroundController.animationTimelines.add(animation);
        Shooter.setPeaTimeline(animation);
    }

    public void checkZombieCollision() {
        synchronized (PlayGroundController.allZombies) {
            synchronized (PlayGroundController.allZombies) {
                Iterator<Zombies> i = PlayGroundController.allZombies.iterator();
                while (i.hasNext()) {
                    Zombies z = i.next();
                    if(z.getLane() == lane && !flag)
                    {
                        if(Math.abs(z.getX()-getX())<=3 && !flag)
                        {
                            this.flag = true;
                            z.setEnergy(z.getEnergy()-1);
                            image.setVisible(false);
                            image.setDisable(true);
                            peaAnimation.stop();
                        }
                    }
                }
            }
        }
    }
}
