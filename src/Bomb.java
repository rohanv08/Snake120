import java.awt.Color;

public class Bomb {
    int blastRadius;
    boolean blast = false;
    int BombX, BombY;
    int number;
    int timer;
    boolean blastAnimation = false;
    int animationRadius = 0;
    Color prevColor = Color.RED;
    public boolean timerActivated = false;
    boolean aboutToBlast = false;
    public Bomb(int x, int y, int number) {
        this.BombX = x;
        this.BombY = y;
        this.blastRadius = (int)(Math.random()*15) + 25;
        this.number = number;
        this.timer = (int)(Math.random()*500) + 500; 
    }
    public void setNeighbours() {
        for (int i = 0; i < Game.numberOfBombs; i++) {
            if (Game.bombMatrix[number][i] == 1) {
                Game.bombList.get(i).aboutToBlast = true;
                Game.bombList.get(i).timerActivated = true;
            }
        }
    }
    
}
