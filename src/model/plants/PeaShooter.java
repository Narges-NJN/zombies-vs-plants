package model.plants;

import javafx.scene.layout.Pane;
import model.Pea;
import model.plants.Shooter;

public class PeaShooter extends Shooter {
    public PeaShooter(int x, int y, int col, int row) {
        super("/image/GIF/shooter.gif", x, y, 100, 480, 80, 100, col, row);
    }

    @Override
    public void customizedShoot(Pane pane) {
        int peaStartX = getX() + 50;
        int peaStartY = getY() + 25;
        Pea p = new Pea(peaStartX, peaStartY, getX() + 50, row);
        p.setImage(pane);
        p.shootPea();
    }
}
