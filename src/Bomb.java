import java.awt.Color;

public class Bomb {
    private int blastRadius, BombX, BombY, number, timer, animationRadius;
    boolean blast = false;
    boolean blastAnimation = false;
    Color prevColor = Color.RED;
    public boolean timerActivated = false;

    public Bomb(int x, int y, int number) {
        this.BombX = x;
        this.BombY = y;
        this.blastRadius = (int) (Math.random() * 15) + 25;
        this.number = number;
        this.timer = (int) (Math.random() * 300) + 500;
        animationRadius = 0;
    }

    public Bomb(int x, int y, int number, int timer, int blastRadius, boolean blast, boolean timerActivated,
            int animationRadius) {
        this.BombX = x;
        this.BombY = y;
        this.blastRadius = blastRadius;
        this.number = number;
        this.timer = timer;
        this.animationRadius = animationRadius;
        this.blast = blast;
        this.timerActivated = timerActivated;
    }

    public void setNeighbours() {
        for (int i = 0; i < Game.numberOfBombs; i++) {
            if (Game.bombMatrix[number][i] == 1) {
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

    public int getNumber() {
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
