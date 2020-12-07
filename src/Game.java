import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
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
    static int[][] occupancyMatrix = new int[H][W];
    static boolean foodAvailable = false;
    static boolean bombs = false;
    static LinkedList<Bomb> bombList;;
    static Snake snake;

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

    static void setBombMatrix() {
        for (int i = 0; i < Game.numberOfBombs; i++) {
            for (int j = 0; j < Game.numberOfBombs; j++) {
                Game.bombMatrix[i][j] = 0;
            }
        }
        for (int i = 0; i < Game.numberOfBombs; i++) {
            for (int j = i; j < Game.numberOfBombs; j++) {
                if (i != j && Math.random() >= 0.5) {
                    Game.bombMatrix[i][j] = 1;
                    Game.bombMatrix[j][i] = 1;
                }
            }
        }

        LinkedList<Integer> connected = new LinkedList<>();
        connected.add(0);
        for (int i = 1; i < Game.numberOfBombs; i++) {
            boolean isConnected = false;
            for (int j = 0; j < connected.size(); j++) {
                if (Game.bombMatrix[i][j] == 1) {
                    isConnected = true;
                    connected.add(i);
                    break;
                }
            }
            if (!isConnected) {
                int a = (int) (Math.random() * connected.size());
                Game.bombMatrix[i][connected.get(a)] = 1;
                Game.bombMatrix[connected.get(a)][i] = 1;
                connected.add(i);
            }
        }

    }

    public static void main(String[] args) {
        final JFrame f = new JFrame("Snake Game");
        Container pane = f.getContentPane();
        BoxLayout boxLayout = new BoxLayout(pane, BoxLayout.Y_AXIS);
        pane.setLayout(boxLayout);
        pane.add(Box.createVerticalStrut(20));
        JLabel title = new JLabel("SNAKE");
        Font labelFont = title.getFont();
        title.setFont(new Font(labelFont.getName(), Font.PLAIN, 20));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        pane.add(title);
        pane.add(Box.createVerticalStrut(20));
        JTextArea instructions = new JTextArea();
        instructions.setText(
                "Instructions: \n 1. Use the arrow keys to change the snake's direction. \n 2. Eat food to grow and gain points. \n 3. Beware of bombs, especially the blinking ones! If I were you,  I wouldn't even go near them. \n 4."
                        + "Press S to save and exit the game. \n"
                        + "\nYour Objective: Eat as many food bits as you can! \n \nPro-tip about the bombs:"
                        + "bombs get activated only if there is a connection between a dormant bomb and a bomb that just blasted! \n \nGood luck!");
        instructions.setAlignmentX(Component.CENTER_ALIGNMENT);
        instructions.setEditable(false);
        instructions.setLineWrap(true);
        instructions.setWrapStyleWord(true);
        pane.add(instructions);
        pane.add(Box.createVerticalStrut(20));
        JButton button = new JButton("Start New Game");
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        f.dispatchEvent(new WindowEvent(f, WindowEvent.WINDOW_CLOSING));
                        StartTheGame(false);
                    }
                }).start();

            }
        });
        pane.add(button);
        pane.add(Box.createVerticalStrut(20));
        button = new JButton("Load Saved Game");
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        File file = new File("save.txt");
                        if (!file.exists()) {

                            JOptionPane.showMessageDialog(f, "Saved data does not exist. Please start a new game.");
                        } else {
                            f.dispatchEvent(new WindowEvent(f, WindowEvent.WINDOW_CLOSING));
                            StartTheGame(true);
                        }
                    }
                }).start();

            }
        });
        pane.add(button);
        pane.add(Box.createVerticalStrut(20));
        f.setSize(W, H + 40);
        f.setVisible(true);

    }

    public static void StartTheGame(boolean Load) {
        JFrame f = new JFrame("Snake Game");

        f.setSize(W, H + 30);
        for (int i = 0; i < H; i++) {
            for (int j = 0; j < W; j++) {
                occupancyMatrix[i][j] = 0;
            }
        }
        prevScore = 0;
        score = 0;
        bombs = false;
        dead = false;
        JLabel scoreText = new JLabel("Score: " + score);
        Container cPane = f.getContentPane();
        scoreText.setSize(100, 20);
        Console console = new Console();
        cPane.add(scoreText);
        cPane.add(console);
        startGame = true;
        if (!Load) {
            snake = new Snake();
            bombList = new LinkedList<Bomb>();
        } else {
            try {
                File file = new File("save.txt");
                FileReader reader = new FileReader(file);
                BufferedReader buffer = new BufferedReader(reader);
                String s = buffer.readLine();
                prevScore = Integer.parseInt(s);
                s = buffer.readLine();
                score = Integer.parseInt(s);
                s = buffer.readLine();
                LinkedList<SnakeSegment> segments = new LinkedList<SnakeSegment>();
                for (int i = 0; i < Integer.parseInt(s); i++) {
                    String a = buffer.readLine();
                    String[] b = a.split(" ");
                    int col1 = Integer.parseInt(b[0]);
                    int col2 = Integer.parseInt(b[1]);
                    int row1 = Integer.parseInt(b[2]);
                    int row2 = Integer.parseInt(b[3]);
                    Direction dir = Direction.valueOf(b[4]);
                    segments.add(new SnakeSegment(row1, row2, col1, col2, dir));
                }
                snake = new Snake(segments);
                s = buffer.readLine();
                bombList = new LinkedList<Bomb>();
                if (Boolean.parseBoolean(s)) {

                    bombs = true;
                    s = buffer.readLine();
                    try {
                        Thread.sleep(2000);

                    } catch (Exception eee) {

                    }
                    for (int i = 0; i < Integer.parseInt(s); i++) {
                        String a = buffer.readLine();
                        String[] b = a.split(" ");
                        int x = Integer.parseInt(b[0]);
                        int y = Integer.parseInt(b[1]);
                        int timer = Integer.parseInt(b[2]);
                        int blastRadius = Integer.parseInt(b[3]);
                        boolean blast = Boolean.parseBoolean(b[4]);
                        boolean timerActivated = Boolean.parseBoolean(b[5]);
                        int animationRadius = Integer.parseInt(b[6]);
                        bombList.add(new Bomb(x, y, i, timer, blastRadius, blast, timerActivated, animationRadius));

                    }
                    for (int i = 0; i < numberOfBombs; i++) {
                        String a = buffer.readLine();
                        String[] b = a.split(" ");
                        for (int k = 0; k < b.length; k++) {
                            bombMatrix[i][k] = Integer.parseInt(b[k]);

                        }
                    }

                } else {
                    bombs = false;
                }
                s = buffer.readLine();
                for (int i = 0; i < Integer.parseInt(s); i++) {
                    String a = buffer.readLine();
                    String[] b = a.split(" ");
                    snake.food.add(new Food(Integer.parseInt(b[1]), Integer.parseInt(b[0]), Integer.parseInt(b[2]),
                            Integer.parseInt(b[3])));
                }
                for (int i = 0; i < H; i++) {
                    String a = buffer.readLine();
                    String[] b = a.split(" ");
                    for (int k = 0; k < b.length; k++) {

                        occupancyMatrix[i][k] = Integer.parseInt(b[k]);
                    }
                }
                buffer.close();

            } catch (Exception e) {
                int input = JOptionPane.showOptionDialog(null,
                        "Saved game data corrupted! Would you like to play a new game instead?", "Snake",
                        JOptionPane.OK_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);
                if (input == JOptionPane.OK_OPTION) {
                    prevScore = 0;
                    score = 0;
                    bombs = false;
                    dead = false;
                    snake = new Snake();
                    bombList = new LinkedList<Bomb>();
                } else {
                    System.exit(0);
                }
            }
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
                case KeyEvent.VK_S:
                    if (startGame) {
                        try {
                            FileWriter writer = new FileWriter("save.txt", false);
                            BufferedWriter buffer = new BufferedWriter(writer);
                            buffer.write(Integer.toString(prevScore));
                            buffer.newLine();
                            buffer.write(Integer.toString(score));
                            buffer.newLine();
                            buffer.write(Integer.toString(snake.getSegments().size()));
                            buffer.newLine();
                            for (int i = 0; i < snake.getSegments().size(); i++) {
                                buffer.write(Integer.toString(snake.getSegments().get(i).getCol1()) + " "
                                        + Integer.toString(snake.getSegments().get(i).getCol2()) + " "
                                        + Integer.toString(snake.getSegments().get(i).getRow1()) + " "
                                        + Integer.toString(snake.getSegments().get(i).getRow2()) + " "
                                        + snake.getSegments().get(i).getDir());
                                buffer.newLine();
                            }
                            if (bombs) {
                                buffer.write(Boolean.toString(bombs));
                                buffer.newLine();
                                buffer.write(Integer.toString(bombList.size()));
                                buffer.newLine();
                                for (int i = 0; i < bombList.size(); i++) {
                                    buffer.write(Integer.toString(bombList.get(i).getX()) + " "
                                            + Integer.toString(bombList.get(i).getY()) + " "
                                            + Integer.toString(bombList.get(i).getTimer()) + " "
                                            + Integer.toString(bombList.get(i).getBlastRadius()) + " "
                                            + Boolean.toString(bombList.get(i).blast) + " "
                                            + Boolean.toString(bombList.get(i).timerActivated) + " "
                                            + Integer.toString(bombList.get(i).getAnimationR()));
                                    buffer.newLine();
                                }
                                for (int i = 0; i < numberOfBombs; i++) {
                                    String s = "";
                                    for (int j = 0; j < numberOfBombs; j++) {
                                        if (j != numberOfBombs - 1) {
                                            s += Integer.toString(bombMatrix[i][j]) + " ";
                                        } else {
                                            s += Integer.toString(bombMatrix[i][j]);
                                        }
                                    }
                                    buffer.write(s);
                                    buffer.newLine();
                                }
                            } else {
                                buffer.write(Boolean.toString(bombs));
                                buffer.newLine();
                            }
                            buffer.write(Integer.toString(snake.food.size()));
                            buffer.newLine();
                            for (int i = 0; i < snake.food.size(); i++) {
                                buffer.write(Integer.toString(snake.food.get(i).getCol()) + " "
                                        + Integer.toString(snake.food.get(i).getRow()) + " "
                                        + Integer.toString(snake.food.get(i).getScore()) + " "
                                        + Integer.toString(snake.food.get(i).getLifetime()));
                                buffer.newLine();
                            }
                            for (int i = 0; i < H; i++) {
                                String s = "";
                                for (int j = 0; j < W; j++) {

                                    if (j != W - 1) {
                                        s += Integer.toString(occupancyMatrix[i][j]) + " ";
                                    } else {
                                        s += Integer.toString(occupancyMatrix[i][j]);
                                    }
                                }
                                buffer.write(s);
                                buffer.newLine();
                            }

                            buffer.close();
                            f.dispatchEvent(new WindowEvent(f, WindowEvent.WINDOW_CLOSING));
                            System.exit(0);
                        } catch (Exception e3) {
                            System.out.println(e3);
                            System.exit(0);
                        }
                    }
                default:
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });

        while (true) {

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
                bombList.clear();
                setBombMatrix();
                for (int i = 0; i < numberOfBombs; i++) {
                    while (true) {
                        boolean ok = true;
                        int x = (int) (Math.random() * W);
                        int y = (int) (Math.random() * H);
                        bombList.add(new Bomb(y, x, i));
                        for (int l = -bombRadii / 2; l <= bombRadii / 2; l++) {
                            for (int m = -bombRadii / 2; m <= bombRadii / 2; m++) {
                                if (x + l >= W || x + l < 0 || y + m >= H || y + m < 0
                                        || occupancyMatrix[x + l][y + m] != 0) {
                                    ok = false;
                                    bombList.remove(i);
                                    break;
                                } else {
                                    occupancyMatrix[x + l][y + m] = 2;
                                }

                            }
                            if (!ok) {
                                break;
                            }
                        }
                        if (!ok) {
                            for (int l = -bombRadii / 2; l <= bombRadii / 2; l++) {
                                for (int m = -bombRadii / 2; m <= bombRadii / 2; m++) {
                                    if (x + l >= W || x + l < 0 || y + m >= H || y + m < 0
                                            || occupancyMatrix[x + l][y + m] != 0) {
                                        break;
                                    } else {
                                        occupancyMatrix[x + l][y + m] = 0;
                                    }

                                }
                            }
                        }
                        if (ok) {
                            if (snake.isOnBomb(i)) {
                                bombList.remove(i);
                                for (int l = -bombRadii / 2; l <= bombRadii / 2; l++) {
                                    for (int m = -bombRadii / 2; m <= bombRadii / 2; m++) {
                                        occupancyMatrix[x + l][y + m] = 0;
                                    }
                                }
                            } else {
                                break;
                            }
                        }
                    }
                }
                int a = (int) (Math.random() * numberOfBombs);
                bombList.get(a).timerActivated = true;
                bombs = true;
            }

            MoveResult outcome = snake.moveSnake();
            if (outcome == MoveResult.hitSelf || outcome == MoveResult.hitBoard || dead
                    || outcome == MoveResult.onBomb) {

                for (int i = 0; i <= 20; i++) {
                    try {
                        f.repaint();
                        Thread.sleep(10);
                    } catch (Exception ee) {

                    }

                }
                int input = JOptionPane.showOptionDialog(null,
                        "Game Over. " + "Your score: " + score + "\n" + "Would you like to give it another try?",
                        "Snake", JOptionPane.OK_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);
                if (input == JOptionPane.OK_OPTION) {
                    new Thread(new Runnable() {

                        @Override
                        public void run() {
                            f.dispatchEvent(new WindowEvent(f, WindowEvent.WINDOW_CLOSING));
                            StartTheGame(false);
                        }
                    }).start();

                } else {
                    System.exit(0);
                }
                startGame = false;
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
                            for (int l = -Game.bombRadii / 2; l <= Game.bombRadii / 2; l++) {
                                for (int m = -Game.bombRadii / 2; m <= Game.bombRadii / 2; m++) {
                                    occupancyMatrix[bombList.get(i).getY() + l][bombList.get(i).getX() + m] = 0;
                                }
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
                System.out.println(e1);
            }
        }
    }

}
