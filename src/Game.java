import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
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
    static int bombRadii = 6;
    static boolean startGame = false;
    static boolean dead = false;
    static int[][] bombMatrix = new int[numberOfBombs][numberOfBombs];
    static int[][] board = new int[H][W];
    static boolean foodAvailable = false;
    static boolean bombs = false;
    static LinkedList<Bomb> bombList = new LinkedList<>();
    static Snake snake = new Snake();

    public static boolean checkBombsBlasted() {
        for (int i = 0; i < bombList.size(); i++) {
            if (!bombList.get(i).blast) {
                bombs = true;
                return true;
            }
        }
        bombs = false;
        return false;
    }

    public static void main(String[] args)
    {
        final JFrame f = new JFrame("Snake Game");
        Container pane = f.getContentPane();
        pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));
 
        JButton button = new JButton("New Game");
        button.addActionListener(new ActionListener() 
        {
            
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                new Thread(new Runnable() {
                    
                    @Override
                    public void run() 
                    {
                        f.dispatchEvent(new WindowEvent(f, WindowEvent.WINDOW_CLOSING));
                        Start(false);
                    }
                }).start();
                
            }
        });
        pane.add(button);
        button = new JButton("Load Game");
        button.addActionListener(new ActionListener() 
        {
            
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                new Thread(new Runnable() {
                    
                    @Override
                    public void run() 
                    {
                        f.dispatchEvent(new WindowEvent(f, WindowEvent.WINDOW_CLOSING));
                        Start(true);
                    }
                }).start();
                
            }
        });
        pane.add(button);
        
        f.setSize(W, H+30);
        f.setVisible(true);
        
    }
    
    public static void Start(boolean Load) {
        JFrame f = new JFrame("Snake Game");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(W, H + 30);
        JLabel scoreText = new JLabel("Score: " + score);
        Container cPane = f.getContentPane();
        scoreText.setSize(100, 20);
        Console console = new Console();
        cPane.add(scoreText);
        cPane.add(console);
        startGame = true;
        if (Load) {
            System.out.println("Load Game");
        }
        f.repaint();
        f.setVisible(true);

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
        for (int ii = 0; ii >= 0; ii++) {
            if (bombs) {
                prevScore = score;
            }
            if (snake.food.size() == 0) {
                boolean foodGiven = false;
                while (!foodGiven) {
                    int foodRow = (int) ((H - foodRadii) * Math.random()) + foodRadii / 2;
                    int foodCol = (int) ((W - foodRadii) * Math.random()) + foodRadii / 2;
                    foodGiven = snake.offerFood(foodRow, foodCol);
                }
                foodAvailable = true;
                f.repaint();
            }
            if (!bombs && score - prevScore >= 1) {
                prevScore = score;
                snake.setBombMatrix();
                bombList.clear();
                for (int i = 0; i < numberOfBombs; i++) {
                    while (true) {
                        int x = (int) (Math.random() * W);
                        int y = (int) (Math.random() * H);
                        if (!snake.isOnSnake(y, x, false)) {
                            bombList.add(new Bomb(y, x, i));
                            if (snake.isOnBomb(i)) {
                                bombList.remove(i);
                            } else {
                                break;
                            }

                        }
                    }
                }
                int a = (int) (Math.random() * numberOfBombs);
                bombList.get(a).timerActivated = true;
                bombList.get(a).aboutToBlast = true;
                bombs = true;
            }
            MoveResult outcome = snake.moveSnake();
            if (outcome == MoveResult.hitSelf || outcome == MoveResult.hitBoard || dead) {
                System.out.println("Game Over! " + outcome);
                break;
            }
            try {
                for (int i = 0; i < snake.food.size(); i++) {
                    snake.food.get(i).decreaseLifetime();
                    if (snake.food.get(i).getLifetime() <= 0) {
                        snake.food.remove(i);
                    }
                }
                for (int i = 0; i < bombList.size(); i++) {
                    if (bombList.get(i).timerActivated) {
                        bombList.get(i).decreaseTimer();
                        if (bombList.get(i).getTimer() <= 0 && !bombList.get(i).blast) {
                            bombList.get(i).blast = true;
                            if (snake.isOnBomb(i)) {
                                bombList.get(i).blast = true;
                                dead = true;
                                break;
                            }
                            bombList.get(i).setNeighbours();
                            checkBombsBlasted();
                        }
                    }
                }
                scoreText.setText("Score: " + score);
                Thread.sleep(10);
                f.repaint();
            } catch (Exception e1) {

            }
        }

        try {
            Thread.sleep(10);
        } catch (InterruptedException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }

}
