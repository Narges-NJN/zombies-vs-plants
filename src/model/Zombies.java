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
    protected int line;
    protected int pace = -1;
    public Timeline zombieAnimation;

    public Zombies(String imagePath, int x, int y, int width, int height, int powerOfAttack, int energy, int line, GridPane lawn_grid) {
        super(imagePath, x, y, width, height);
        this.powerOfAttack = powerOfAttack;
        this.energy = energy;
        this.line = line;
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

    public int getLine() {
        return line;
    }

    public Timeline getZombieAnimation()
    {
        return this.zombieAnimation;
    }

    public void moveZombie(int time) {
        zombieAnimation = new Timeline(new KeyFrame(Duration.millis(time), e -> zombieWalk()));
        zombieAnimation.setCycleCount(Animation.INDEFINITE);
        changeDifficulty();
        zombieAnimation.play();
        PlayGroundController.animationTimelines.add(zombieAnimation);
    }

    protected void changeDifficulty(){
        if(PlayGroundController.zombiesCount>7){
            this.setEnergy(this.getEnergy()+15);
            this.setPowerOfAttack(this.getPowerOfAttack()+10);
        }
        if(PlayGroundController.zombiesCount>10){
            this.setEnergy(this.getEnergy()+20);
            this.setPowerOfAttack(this.getPowerOfAttack()+10);
        }
    }

    public void zombieWalk()
    {
        if(getX()>300 && this.energy>0)
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
                if (p.row == getLine()) {
                    if (Math.abs(p.getX() - image.getX()) <= 50) {
                        customizedZombieAttack(p);
                    }
                }else
                    this.pace = -1;
            }
        }
    }

    public void zombieReachedHouse() {
        if (image.getX() <= 300){
            PlayGroundController.pauseAnimations();
            Line.removeLine(lawn_grid, getLine());
            removeFromAvailableLines();
            this.image.setVisible(false);
            PlayGroundController.allZombies.remove(this);
            super.endAnimation(zombieAnimation);
            removeInLineZombies();
            removeInLinePlants();
            PlayGroundController.startAnimations();
        }

    }

    private void removeInLineZombies() {
        for (int j = 0; j < PlayGroundController.allZombies.size(); j++) {
            Zombies z = (Zombies) PlayGroundController.allZombies.get(j);
            if (z.getLine() == getLine()) {
                z.image.setVisible(false);
                z.image.setDisable(true);
                z.endAnimation(zombieAnimation);
                PlayGroundController.allZombies.remove(z);
            }
        }
    }

    private void removeInLinePlants() {
        for (int i = 0; i < PlayGroundController.allPlants.size(); i++) {
            Plants p = (Plants) PlayGroundController.allPlants.get(i);
            if (p.row == getLine()) {
                p.image.setVisible(false);
                p.image.setDisable(true);
                p.stopAnimations();
                PlayGroundController.allPlants.remove(p);
            }
        }
    }

    protected abstract void customizedZombieAttack(Plants p);

    private void removeFromAvailableLines() {
        for (int i = 0; i < PlayGroundController.availableLanes.size(); i++) {
            if(getLine() == PlayGroundController.availableLanes.get(i)){
                PlayGroundController.availableLanes.remove(i);
            }
        }
        PlayGroundController.removedLines.add(getLine());
    }


}

