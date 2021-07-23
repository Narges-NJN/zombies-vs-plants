package model;

import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class GameComponents {
    public String imagePath;
    public int x, y, width, height;
    public ImageView image;

    public GameComponents(String imagePath, int x, int y, int width, int height) {
        this.imagePath=imagePath;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void setImage(Pane pane) {
        image = new ImageView();
        Image img = new Image(imagePath,(double) width,(double) height,false,false);
        image.setImage(img);
        image.setX(x);
        image.setY(y);
        pane.getChildren().add(image);
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
        image.setX(x);
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
        image.setY(y);
    }

    public void endAnimation(Timeline t)
    {
        t.stop();
    }
}
