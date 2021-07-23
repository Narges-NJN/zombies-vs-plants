package model.plants;

import javafx.scene.layout.Pane;
import model.Pea;

public class AdvancedPeaShooter extends Shooter{
    public AdvancedPeaShooter(int x, int y, int col, int row) {
        super("/image/GIF/advanced_pea_shooter.gif", x, y, 200, 520, 65, 85, col, row);
    }

    @Override
    public void customizedShoot(Pane pane) {
        int pea1StartX = getX() + 50;
        int pea2StartX = getX() - 20;
        int peaStartY = getY() + 26;
        Pea p1 = new Pea(pea1StartX, peaStartY, getX() + 50, row);
        Pea p2 = new Pea(pea2StartX, peaStartY, getX() + 50, row);
        p1.setImage(pane);
        p2.setImage(pane);
        p1.shootPea();
        p2.shootPea();
    }
}
