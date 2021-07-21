package model;

import controller.PlayGroundController;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;

import java.util.Iterator;

public abstract class Zombies extends GameComponents{
    public GridPane lawn_grid;
    protected int powerOfAttack;
    protected int energy;
    protected int lane;
    protected int pace = -1;
    protected Timeline zombieAnimation;
    protected boolean reachedPlant = false;
    //protected boolean isEating = false;

    public Zombies(String imagePath, int x, int y, int width, int height, int powerOfAttack, int energy, int lane, GridPane lawn_grid) {
        super(imagePath, x, y, width, height);
        this.powerOfAttack = powerOfAttack;
        this.energy = energy;
        this.lane = lane;
        this.lawn_grid = lawn_grid;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
        if(energy<=0){
            PlayGroundController.score+=1;
            PlayGroundController.updateScore();
            this.image.setVisible(false);
            this.image.setDisable(true);
            this.zombieAnimation.stop();
            for(int i = 0; i< PlayGroundController.allZombies.size(); i++)
            {
                if(this== PlayGroundController.allZombies.get(i))
                {
                    PlayGroundController.allZombies.remove(i);
                    break;
                }
            }
        }
    }
    public int getEnergy() {
        return energy;
    }

    public int getPowerOfAttack() {
        return powerOfAttack;
    }

    public void setPowerOfAttack(int powerOfAttack) {
        this.powerOfAttack = powerOfAttack;
    }


    public void setLane(int lane) {
        this.lane = lane;
    }
    public int getLane() {
        return lane;
    }

    public Timeline getZombieAnimation()
    {
        return this.zombieAnimation;
    }

    public void zombieReachedHouse() {
        if (image.getX() <= 320){
            PlayGroundController.pauseAnimations();
            Lane.removeLane(lawn_grid, getLane());
            for (int i = 0; i < PlayGroundController.availableLanes.size(); i++) {
                if(getLane() == PlayGroundController.availableLanes.get(i)){
                    PlayGroundController.availableLanes.remove(i);
                }
            }
            PlayGroundController.removedLines.add(getLane());
            this.image.setVisible(false);
            PlayGroundController.allZombies.remove(this);
            super.endAnimation(zombieAnimation);
            for (int i = 0; i < PlayGroundController.allPlants.size(); i++) {
                Plants p = (Plants) PlayGroundController.allPlants.get(i);
                if (p.row == getLane()) {
                    p.image.setVisible(false);
                    p.image.setDisable(true);
                    p.stopAnimations();
                    PlayGroundController.allPlants.remove(p);
                }
            }

            for (int j = 0; j < PlayGroundController.allZombies.size(); j++) {
                Zombies z = (Zombies) PlayGroundController.allZombies.get(j);
                if (z.getLane() == getLane()) {
                    z.image.setVisible(false);
                    z.image.setDisable(true);
                    z.endAnimation(zombieAnimation);
                    PlayGroundController.allZombies.remove(z);
                }
            }
            PlayGroundController.startAnimations();
        }

    }

    public void moveZombie(int time) {
        zombieAnimation = new Timeline(new KeyFrame(Duration.millis(time), e -> zombieWalk()));
        zombieAnimation.setCycleCount(Animation.INDEFINITE);
        if(PlayGroundController.zombiesCount>7){
            this.setEnergy(this.getEnergy()+15);
            this.setPowerOfAttack(this.getPowerOfAttack()+10);
        }
        if(PlayGroundController.zombiesCount>10){
            this.setEnergy(this.getEnergy()+20);
            this.setPowerOfAttack(this.getPowerOfAttack()+10);
        }
        zombieAnimation.play();
        PlayGroundController.animationTimelines.add(zombieAnimation);
    }

    public void zombieWalk()
    {
        if(getX()>310 && this.energy>0)
        {
            setX(getX()+this.pace);
            try{
                eatPlant();
            }
            catch(java.util.ConcurrentModificationException e)
            {
                e.printStackTrace();
            }
            zombieReachedHouse();

        }
    }

    public void eatPlant()
    {
        synchronized (PlayGroundController.allPlants)
        {
            Iterator<Plants> i = PlayGroundController.allPlants.iterator();
            while(i.hasNext())
            {
                Plants p = i.next();
                if (p.row == getLane()) {
                    if (Math.abs(p.getX() - image.getX()) <= 50) {
                        customizedZombieAttack(p);
                    }
                }else
                    this.pace = -1;
            }
        }
    }
    protected abstract void customizedZombieAttack(Plants p);
}

