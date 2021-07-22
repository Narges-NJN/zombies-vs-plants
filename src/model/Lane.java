package model;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

public class Lane {

    public static void removeLane(GridPane gridPane, int rowIndex){
        ImageView image = new ImageView();
        Image img=new Image("/image/PNG/lawn_lane.png",(double) 1000, (double) 130,false,false);
        image.setImage(img);
        gridPane.add(image,0,rowIndex,1,1);
    }
}
