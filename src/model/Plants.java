package model;

import controller.PlayGroundController;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

public abstract class Plants extends GameComponents {
    protected int price;
    public int col;
    public int row;
    protected int powerOfAttack;
    protected int energy;

    public Plants(String path, int x, int y, int price, int energy, int width,int height,int col,int row) {
        super(path, x, y, width, height);
        this.col=col;
        this.row=row;
        this.price=price;
        this.energy = energy;
    }

    public void setImage(GridPane lawn){
        image = new ImageView();
        Image imd=new Image(imagePath,(double) width,(double) height,false,false);
        image.setImage(imd);
        lawn.add(image,col,row,1,1);
    }

    public int getEnergy() {
        return energy;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
        if (this.energy<=0){
            PlayGroundController.allPlants.remove(this);
            image.setVisible(false);
            image.setDisable(true);
        }
    }

    public abstract void attack(Pane pane);
    public abstract void stopAnimations();
}
