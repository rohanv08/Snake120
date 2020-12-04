import java.awt.Color;

public class Bomb {
    int blastRadius = 10;
    boolean blast = false;
    int BombX, BombY;
    int number;
    int timer;
    boolean blastAnimation = false;
    Color prevColor = Color.RED;
    public boolean timerActivated = false;
    boolean aboutToBlast = false;
    public Bomb(int x, int y, int number) {
        this.BombX = x;
        this.BombY = y;
        this.blastRadius = 15;
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
