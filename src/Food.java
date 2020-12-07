import java.awt.Color;

public class Food {
    private int lifetime, score, row, col;
    Color color;

    public Food(int row, int col, int score) {
        this.row = row;
        this.col = col;
        this.score = 1;
        this.color = Color.BLUE;
        this.lifetime = 1000;
    }

    public Food(int row, int col, int score, int lifetime) {
        this.row = row;
        this.col = col;
        this.score = 1;
        this.color = Color.BLUE;
        this.lifetime = lifetime;
    }

    public void decreaseLifetime() {
        lifetime--;
    }

    public int getLifetime() {
        return lifetime;
    }

    public int getScore() {
        return score;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }
}
