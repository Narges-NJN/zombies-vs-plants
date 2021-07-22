package model;

import controller.PlayGroundController;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

public class Line {
    private static SunFlower SunFlower;

    public static void removeLine(GridPane gridPane, int rowIndex){
        ImageView image = new ImageView();
        Image img=new Image("/image/PNG/lawn_lane.png",(double) 1000, (double) 130,false,false);
        image.setImage(img);
        gridPane.add(image,0,rowIndex,1,1);
        removeEveryThingUnderTheLine(rowIndex);
    }
    private static void removeEveryThingUnderTheLine(int rowIndex){
        for (int i = 0; i < PlayGroundController.allPlants.size(); i++) {
            Plants p = (Plants) PlayGroundController.allPlants.get(i);
            if (p.row == rowIndex) {
                p.image.setVisible(false);
                p.image.setDisable(true);
                p.stopAnimations();
                PlayGroundController.allPlants.remove(p);
            }
        }
        for (int j = 0; j < PlayGroundController.allZombies.size(); j++) {
            Zombies z = (Zombies) PlayGroundController.allZombies.get(j);
            if (z.getLine() == rowIndex) {
                z.image.setVisible(false);
                z.image.setDisable(true);
                z.endAnimation(z.zombieAnimation);
                PlayGroundController.allZombies.remove(z);
            }
        }
    }
}
