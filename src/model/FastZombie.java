package model;

import controller.PlayGroundController;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.util.Iterator;

public class FastZombie extends Zombies {

    public FastZombie(int x, int y, int lane, GridPane lawn_grid) {
        super("/image/GIF/normal_zombie.gif", x, y, 70, 130, 100, 3, lane, lawn_grid);
    }

    public static void runFastZombie(Pane pane, int lane, int laneNumber, GridPane lawn_grid) {
        FastZombie z = new FastZombie(1200, lane, laneNumber, lawn_grid);
        z.setImage(pane);
        PlayGroundController.allZombies.add(z);
        z.moveZombie(20);
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
    private void removeNearPlants() {
        synchronized (PlayGroundController.allPlants) {
            Iterator<Plants> j = PlayGroundController.allPlants.iterator();
            while(j.hasNext()) {
                Plants p = j.next();
                if(p.getX()<=(getX()+250) && p.getX()>=(getX()-150))
                {
                    if(p.getY()<=(getY()+250) && p.getY()>=(getY()-150)) {
                        PlayGroundController.allPlants.remove(j);
                        p.image.setVisible(false);
                        p.image.setDisable(true);
                        p.stopAnimations();
                    }
                }
            }
        }
    }

    @Override
    protected void customizedZombieAttack(Plants p) {
        removeNearPlants();
        removeNearZombies();
        this.pace = 0;
        this.image.setVisible(false);
        this.image.setDisable(true);
        PlayGroundController.allPlants.remove(p);
        p.image.setVisible(false);
        p.image.setDisable(true);
        p.stopAnimations();
        super.endAnimation(zombieAnimation);
    }
}