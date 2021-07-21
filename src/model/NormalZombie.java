package model;

import controller.PlayGroundController;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

public class NormalZombie extends Zombies{
    public NormalZombie(int x, int y, int lane, GridPane lawn_grid) {
        super("/image/GIF/normal_zombie.gif", x, y, 80, 120, 6, 12, lane, lawn_grid);
    }

    public static void runNormalZombie(Pane pane, int lane, int laneNumber, GridPane lawn_grid) {
        NormalZombie z = new NormalZombie(1200, lane, laneNumber, lawn_grid);
        z.setImage(pane);
        PlayGroundController.allZombies.add(z);
        z.moveZombie(70);
    }
    @Override
    protected void customizedZombieAttack(Plants p) {
        this.pace = 0;
        p.setEnergy(p.getEnergy() - this.powerOfAttack);
        if (p.getEnergy() <= 0) {
            p.setEnergy(0);
            PlayGroundController.allPlants.remove(p);
            p.image.setVisible(false);
            p.image.setDisable(true);
            this.pace = -1;
        }
    }
}
