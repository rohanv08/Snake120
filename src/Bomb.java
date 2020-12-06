import java.awt.Color;

public class Bomb {
    private int blastRadius, BombX, BombY, number, timer, animationRadius;
    boolean blast = false;
    boolean blastAnimation = false;
    Color prevColor = Color.RED;
    public boolean timerActivated = false;
    boolean aboutToBlast = false;

    public Bomb(int x, int y, int number) {
        this.BombX = x;
        this.BombY = y;
        this.blastRadius = (int) (Math.random() * 15) + 25;
        this.number = number;
        this.timer = (int) (Math.random() * 300) + 500;
        animationRadius = 0;
    }

    public void setNeighbours() {
        for (int i = 0; i < Game.numberOfBombs; i++) {
            if (Game.bombMatrix[number][i] == 1) {
                Game.bombList.get(i).aboutToBlast = true;
                Game.bombList.get(i).timerActivated = true;
            }
        }
    }

    public int getX() {
        return BombX;
    }

    public int getY() {
        return BombY;
    }

    public int bombNumber() {
        return number;
    }

    public void decreaseTimer() {
        timer--;
    }

    public int getTimer() {
        return timer;
    }

    public int getAnimationR() {
        return animationRadius;
    }

    public int getBlastRadius() {
        return blastRadius;
    }

    public void increaseAnimationR() {
        animationRadius++;
    }

}
