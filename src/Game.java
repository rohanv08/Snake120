import java.awt.Container;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.LinkedList;
import javax.swing.*;

public class Game {
    static int W = 400;
    static int H = 400;
    static int score = 0;
    static int prevScore = 0;
    static int foodRadii = 8;
    static int snakeRadii = 8;
    static int snakeGrowth = 10;
    static int numberOfBombs = 20;
    static int[][] bombMatrix = new int[numberOfBombs][numberOfBombs];
    static int[][] board = new int[H][W];
    static boolean foodAvailable = false;
    static boolean bombs = false;
    static LinkedList<Bomb> bombList = new LinkedList<>();
    static Snake snake = new Snake();
    public static boolean checkBombsBlasted () {
       for (int i = 0 ; i < bombList.size(); i++)
       {
           if (!bombList.get(i).blast) {
               bombs = true;
               return true;
           }
       }
       bombs = false;
       return false;
    }
    public static void main(String[] args) {
        JFrame f = new JFrame("Snake Game");
        f.setSize(W, H);
        JLabel scoreText = new JLabel("Score: " + score);
        scoreText.setSize(100, 20);
        Console console = new Console();
        Container cPane = f.getContentPane();
        cPane.add(scoreText);
        cPane.add(console);

        f.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyCode();
                switch (keyCode) {
                case KeyEvent.VK_UP:
                    snake.changeDirection(Direction.UP);
                    break;
                case KeyEvent.VK_DOWN:
                    snake.changeDirection(Direction.DOWN);
                    break;
                case KeyEvent.VK_LEFT:
                    snake.changeDirection(Direction.LEFT);
                    break;
                case KeyEvent.VK_RIGHT:
                    snake.changeDirection(Direction.RIGHT);
                    break;
                default:
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                // TODO Auto-generated method stub
            }
        });

        f.repaint();
        f.setVisible(true);
        for (int ii = 0; ii >= 0; ii++) {
            if (snake.food.size() == 0) {
                boolean foodGiven = false;
                while (!foodGiven) {
                    int foodRow = (int) (H * Math.random());
                    int foodCol = (int) (W * Math.random());
                    foodGiven = snake.offerFood(foodRow, foodCol);
                }
                foodAvailable = true;
                f.repaint();
            }
            if (!bombs && score - prevScore >= 5) {
                prevScore = score;
                snake.setBombMatrix();
                bombList.clear();
                for (int i = 0; i < numberOfBombs; i++) {
                    while (true) {
                        int x = (int) (Math.random() * W);
                        int y = (int) (Math.random() * H);
                        if (!snake.isOnSnake(y, x, false)) {
                            bombList.add(new Bomb(y, x, i));
                            break;
                        }
                    }
                }
                int a = (int) (Math.random() * numberOfBombs);
                bombList.get(a).timerActivated = true;
                bombList.get(a).aboutToBlast = true;
                System.out.println("Set " + a);
                bombs = true;
            }
            MoveResult outcome = snake.moveSnake();
            if (outcome == MoveResult.hitSelf || outcome == MoveResult.hitBoard) {
                System.out.println("Game Over! " + outcome);
                break;
            }
            try {
                for (int i = 0; i < snake.food.size(); i++) {
                    snake.food.get(i).lifetime--;
                    if (snake.food.get(i).lifetime <= 0) {
                        snake.food.remove(i);
                    }
                }
                for (int i = 0; i < bombList.size(); i++) {
                    if (bombList.get(i).timerActivated) {
                        bombList.get(i).timer = bombList.get(i).timer - 1;
                        if (bombList.get(i).timer <= 0) {
                            bombList.get(i).blast = true;
                            bombList.get(i).setNeighbours();
                            checkBombsBlasted();
                        }
                    }
                }
                scoreText.setText("Score: " + score);
                Thread.sleep(10);
                f.repaint();
            } catch (Exception e) {

            }

        }
    }
}
