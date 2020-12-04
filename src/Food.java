import java.awt.Color;

public class Food {
    int row, col;
    public int lifetime, score;
    Color color;
    
    public Food (int row, int col, int score) {
        this.row = row;
        this.col = col;
        this.score = 1;
        this.color = Color.BLUE;
        this.lifetime = 1000;
    }
}
