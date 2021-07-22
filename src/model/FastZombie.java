package model;

import controller.PlayGroundController;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

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


    @Override
    protected void customizedZombieAttack(Plants p) {
        this.pace = 0;
        this.image.setVisible(false);
        this.image.setDisable(true);
        PlayGroundController.allPlants.remove(p);
        p.image.setVisible(false);
        p.image.setDisable(true);
        p.stopAnimations();
        synchronized (PlayGroundController.allPlants) {
            Iterator<Plants> i = PlayGroundController.allPlants.iterator();
            while (i.hasNext()) {
                Plants n = i.next();
                if ( (n.row == p.row && n.col== p.col+1) ||
                        (n.row == p.row && n.col== p.col-1) ||
                        (n.row == p.row+1 && n.col== p.col) ||
                        (n.row == p.row-1 && n.col== p.col) ||
                        (n.row == p.row+1 && n.col== p.col+1) ||
                        (n.row == p.row-1 && n.col== p.col+1) ||
                        (n.row == p.row -1 && n.col== p.col-1) ||
                        (n.row == p.row +1 && n.col== p.col-1)) {
                    PlayGroundController.allPlants.remove(n);
                    n.image.setVisible(false);
                    n.image.setDisable(true);
                    n.stopAnimations();
                }
            }
        }
        synchronized (PlayGroundController.allZombies) {
            Iterator<Zombies> a = PlayGroundController.allZombies.iterator();
            while (a.hasNext()) {
                Zombies z = a.next();
                if ( (z.getLine() == p.row && z.getX()== p.getX()+200) ||
                        (z.getLine() == p.row && z.getX()== p.getX()-200) ||
                        (z.getLine() == p.row+1 && z.getX()== p.getX()) ||
                        (z.getLine() == p.row-1 && z.getX()== p.getX()) ||
                        (z.getLine() == p.row+1 && z.getX()== p.getX()+200) ||
                        (z.getLine() == p.row-1 && z.getX()== p.getX()+200) ||
                        (z.getLine() == p.row -1 && z.getX()== p.getX()-200) ||
                        (z.getLine() == p.row +1 && z.getX()== p.getX()-200)) {
                    PlayGroundController.allZombies.remove(z);
                    z.image.setVisible(false);
                    z.image.setDisable(true);
                }
            }
        }
        super.endAnimation(zombieAnimation);
        }
}