import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Iterator;
import javax.swing.JComponent;

//static color = 
public class Console extends JComponent {

    @Override
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, Game.W, Game.H);
        for (int i = 0; i < Game.numberOfBombs && Game.bombs; i++) {
            if (!Game.bombList.get(i).blast) {
                if (!Game.bombList.get(i).aboutToBlast) {
                    g2d.setColor(Color.BLACK);
                    g2d.fillRect(Game.bombList.get(i).BombX, Game.bombList.get(i).BombY, Game.bombRadii,
                            Game.bombRadii);
                } else {
                    g2d.setColor(Game.bombList.get(i).prevColor);
                    g2d.fillRect(Game.bombList.get(i).BombX, Game.bombList.get(i).BombY, Game.bombRadii,
                            Game.bombRadii);
                    if (Game.bombList.get(i).prevColor == Color.RED) {
                        Game.bombList.get(i).prevColor = Color.YELLOW;
                    } else {
                        Game.bombList.get(i).prevColor = Color.RED;
                    }
                }
            } else if (!Game.bombList.get(i).blastAnimation) {
                if (Game.bombList.get(i).blastRadius == Game.bombList.get(i).animationRadius) {
                    Game.bombList.get(i).blastAnimation = true;
                    g2d.setColor(Color.WHITE);
                    g2d.fillOval(Game.bombList.get(i).BombX + Game.bombRadii / 2 - Game.bombList.get(i).animationRadius/2,
                            Game.bombList.get(i).BombY + Game.bombRadii / 2 - Game.bombList.get(i).animationRadius/2,
                            Game.bombList.get(i).animationRadius, Game.bombList.get(i).animationRadius);
                } else {
                    g2d.setColor(Color.black);
                    Game.bombList.get(i).animationRadius++;
                    g2d.fillOval(Game.bombList.get(i).BombX + Game.bombRadii / 2 - Game.bombList.get(i).animationRadius/2,
                            Game.bombList.get(i).BombY + Game.bombRadii / 2 - Game.bombList.get(i).animationRadius/2,
                            Game.bombList.get(i).animationRadius, Game.bombList.get(i).animationRadius);
                }

            }
        }
        for (int i = 0; i < Game.numberOfBombs && Game.bombs; i++) {

            for (int j = i; j < Game.numberOfBombs; j++) {
                if (Game.bombMatrix[i][j] == 1 && (!Game.bombList.get(i).blast && !Game.bombList.get(j).blast)) {
                    g2d.setColor(Color.LIGHT_GRAY);
                    g2d.drawLine(Game.bombList.get(i).BombX, Game.bombList.get(i).BombY, Game.bombList.get(j).BombX,
                            Game.bombList.get(j).BombY);
                }
            }
        }
        for (int i = 0; i < Game.H; i++) {
            for (int j = 0; j < Game.W; j++) {
                if (Game.board[i][j] == 2) {
                    g2d.setColor(Color.GREEN);
                    g2d.drawRect(j, i, 2, 2);
                }
            }
        }
        for (Iterator<SnakeSegment> segment = Game.snake.segments.iterator(); segment.hasNext();) {
            SnakeSegment temp = segment.next();
            switch (temp.dir) {
            case UP: {
                for (int i = temp.row1; i <= temp.row2; i++) {
                    g2d.setColor(Color.GREEN);
                    g2d.fillRect(temp.col1, i, Game.snakeRadii, Game.snakeRadii);
                    ;
                }
            }
            case DOWN: {
                for (int i = temp.row1; i <= temp.row2; i++) {
                    g2d.setColor(Color.GREEN);
                    g2d.fillRect(temp.col1, i, Game.snakeRadii, Game.snakeRadii);
                    ;
                }
            }
            case LEFT: {
                for (int i = temp.col1; i <= temp.col2; i++) {
                    g2d.setColor(Color.GREEN);
                    g2d.fillRect(i, temp.row1, Game.snakeRadii, Game.snakeRadii);
                }
            }
            case RIGHT: {
                for (int i = temp.col1; i <= temp.col2; i++) {
                    g2d.setColor(Color.GREEN);
                    g2d.fillRect(i, temp.row1, Game.snakeRadii, Game.snakeRadii);
                }

            }
            }
        }

        for (Iterator<Food> food = Game.snake.food.iterator(); food.hasNext();) {
            Food temp = food.next();
            g2d.setColor(temp.color);
            g2d.fillRect(temp.col, temp.row, Game.foodRadii, Game.foodRadii);
        }

    }
}
