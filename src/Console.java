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
            for (int j = i; j < Game.numberOfBombs; j++) {
                if (Game.bombMatrix[i][j] == 1 && (!Game.bombList.get(i).blast && !Game.bombList.get(j).blast)) {
                    g2d.setColor(Color.LIGHT_GRAY);
                    g2d.drawLine(Game.bombList.get(i).getX()-Game.bombRadii/2, Game.bombList.get(i).getY()-Game.bombRadii/2, Game.bombList.get(j).getX()-Game.bombRadii/2,
                            Game.bombList.get(j).getY()-Game.bombRadii/2);
                }
            }
        }
        for (int i = 0; i < Game.numberOfBombs && Game.bombs; i++) {
            if (!Game.bombList.get(i).blast) {
                if (!Game.bombList.get(i).timerActivated) {
                    g2d.setColor(Color.BLACK);
                    g2d.fillRect(Game.bombList.get(i).getX()- Game.bombRadii/2, Game.bombList.get(i).getY()- Game.bombRadii/2, Game.bombRadii,
                            Game.bombRadii);
                } else {
                    g2d.setColor(Game.bombList.get(i).prevColor);
                    g2d.fillRect(Game.bombList.get(i).getX() - Game.bombRadii/2, Game.bombList.get(i).getY() - Game.bombRadii/2, Game.bombRadii,
                            Game.bombRadii);
                    if (Game.bombList.get(i).prevColor == Color.RED) {
                        Game.bombList.get(i).prevColor = Color.YELLOW;
                    } else {
                        Game.bombList.get(i).prevColor = Color.RED;
                    }
                }
            } else if (!Game.bombList.get(i).blastAnimation) {
                if (Game.bombList.get(i).getBlastRadius() == Game.bombList.get(i).getAnimationR()) {
                    Game.bombList.get(i).blastAnimation = true;
                    g2d.setColor(Color.WHITE);
                    g2d.fillOval(
                            Game.bombList.get(i).getX() + Game.bombRadii / 2 - Game.bombList.get(i).getAnimationR() / 2,
                            Game.bombList.get(i).getY() + Game.bombRadii / 2 - Game.bombList.get(i).getAnimationR() / 2,
                            Game.bombList.get(i).getAnimationR(), Game.bombList.get(i).getAnimationR());
                } else {
                    g2d.setColor(Color.black);
                    Game.bombList.get(i).increaseAnimationR();
                    g2d.fillOval(
                            Game.bombList.get(i).getX() + Game.bombRadii / 2 - Game.bombList.get(i).getAnimationR() / 2,
                            Game.bombList.get(i).getY() + Game.bombRadii / 2 - Game.bombList.get(i).getAnimationR() / 2,
                            Game.bombList.get(i).getAnimationR(), Game.bombList.get(i).getAnimationR());
                }

            }
        }

        for (Iterator<SnakeSegment> segment = Game.snake.getSegments().iterator(); segment.hasNext();) {
            SnakeSegment temp = segment.next();
            switch (temp.dir) {
            case UP:
            case DOWN: {
                for (int i = temp.getRow1(); i <= temp.getRow2(); i++) {
                    g2d.setColor(Color.GREEN);
                    g2d.fillRect(temp.getCol1() - Game.snakeRadii / 2, i - Game.snakeRadii / 2, Game.snakeRadii,
                            Game.snakeRadii);
                    ;
                }
            }
            break;
            case LEFT: 
            case RIGHT: {
                for (int i = temp.getCol1(); i <= temp.getCol2(); i++) {
                    g2d.setColor(Color.GREEN);
                    g2d.fillRect(i - Game.snakeRadii / 2, temp.getRow1() - Game.snakeRadii / 2, Game.snakeRadii,
                            Game.snakeRadii);
                }

            }
            }
        }

        for (Iterator<Food> food = Game.snake.food.iterator(); food.hasNext();) {
            Food temp = food.next();
            g2d.setColor(temp.color);
            g2d.fillRect(temp.getCol() - Game.foodRadii / 2, temp.getRow() - Game.foodRadii / 2, Game.foodRadii,
                    Game.foodRadii);
        }

    }
}
