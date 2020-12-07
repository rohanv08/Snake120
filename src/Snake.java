import java.util.Iterator;
import java.util.LinkedList;

enum Direction {
    UP("UP"), DOWN("DOWN"), LEFT("LEFT"), RIGHT("RIGHT");

    public final String label;

    private Direction(String label) {
        this.label = label;
    }
};

enum MoveResult {
    moved, hitBoard, hitSelf, ateFood, onBomb
};

public class Snake {
    private boolean onBomb(int row, int col) {
        for (int i = -Game.snakeRadii / 2; i <= Game.snakeRadii / 2; i++) {
            for (int j = -Game.snakeRadii / 2; j <= Game.snakeRadii / 2; j++)
                if (Game.occupancyMatrix[row + i][col + j] == 2) {
                    return true;
                }
        }
        return false;

    }

    private boolean checkHitBoard(int row, int col) {
        if (row + Game.snakeRadii / 2 < Game.H && row - Game.snakeRadii / 2 >= 0 && col + Game.snakeRadii / 2 < Game.W
                && col - Game.snakeRadii / 2 >= 0) {
            return true;
        }
        return false;
    }

    private boolean inRadius(int x1, int x2, int y1, int y2, int radii) {
        switch (currDirection) {
        case UP:
        case DOWN: {
            if (y1 != y2) {
                return false;
            } else if (x2 >= x1) {
                if (x2 - x1 < radii) {
                    return true;
                } else {
                    return false;
                }
            } else {
                if (x1 - x2 < Game.snakeRadii) {
                    return true;
                } else {
                    return false;
                }
            }
        }
        case LEFT:
        case RIGHT: {
            if (x1 != x2) {
                return false;
            } else if (y2 >= y1) {
                if (y2 - y1 < radii) {
                    return true;
                } else {
                    return false;
                }
            } else {
                if (y1 - y2 < Game.snakeRadii) {
                    return true;
                } else {
                    return false;
                }
            }
        }

        }
        return false;

    }

    private Direction currDirection;
    boolean hasBomb = false;
    private LinkedList<SnakeSegment> segments = new LinkedList<>();
    LinkedList<Food> food = new LinkedList<>();

    public LinkedList<SnakeSegment> getSegments() {
        return (LinkedList<SnakeSegment>) segments.clone();
    }

    boolean offerFood(int foodRow, int foodCol) {
        boolean ok = true;
        for (int i = -Game.foodRadii / 2; i <= Game.foodRadii / 2; i++) {
            for (int j = -Game.foodRadii / 2; j <= Game.foodRadii / 2; j++) {
                if (foodRow + i < 0 || foodRow + i >= Game.H || foodCol + j < 0 || foodCol + j >= Game.W) {
                    ok = false;
                    break;
                }
                Game.occupancyMatrix[foodRow + i][foodCol + j] = 1;
            }
            if (!ok) {
                break;
            }
        }
        if (!ok) {
            for (int i = -Game.foodRadii / 2; i <= Game.foodRadii / 2; i++) {
                for (int j = -Game.foodRadii / 2; j <= Game.foodRadii / 2; j++) {
                    if (foodRow + i < 0 || foodRow + i >= Game.H || foodCol + j < 0 || foodCol + j >= Game.W) {
                        return false;
                    }
                    Game.occupancyMatrix[foodRow + i][foodCol + j] = 0;
                }
                if (!ok) {
                    break;
                }
            }
        }
        if (isFoodOnSnake()) {
            for (int i = -Game.foodRadii / 2; i <= Game.foodRadii / 2; i++) {
                for (int j = -Game.foodRadii / 2; j <= Game.foodRadii / 2; j++) {
                    Game.occupancyMatrix[foodRow + i][foodCol + j] = 0;
                }
            }
            return false;
        }
        food.add(new Food(foodRow, foodCol, 1));
        return true;
    }

    public Snake() {
        SnakeSegment start = new SnakeSegment(50, 50, 50, 150, Direction.RIGHT);
        segments.add(start);
        currDirection = Direction.RIGHT;
    }

    public Snake(LinkedList<SnakeSegment> segments) {
        this.segments = segments;
        currDirection = segments.getFirst().getDir();
    }

    void changeDirection(Direction dir) {
        switch (currDirection) {
        case UP:
            if (dir != Direction.DOWN)
                currDirection = dir;
            break;
        case DOWN:
            if (dir != Direction.UP)
                currDirection = dir;
            break;
        case LEFT:
            if (dir != Direction.RIGHT)
                currDirection = dir;
            break;
        case RIGHT:
            if (dir != Direction.LEFT)
                currDirection = dir;
            break;
        }
    }

    MoveResult moveSnake() {
        switch (currDirection) {
        case UP:
            return moveUp();

        case DOWN:
            return moveDown();

        case LEFT:
            return moveLeft();

        case RIGHT:
            return moveRight();

        }
        return MoveResult.moved;
    }

    boolean isOnSnake(int row, int col, boolean checkingForHead) {
        if (checkingForHead && segments.size() == 1)
            return false;
        Iterator<SnakeSegment> it = segments.iterator();
        SnakeSegment head = it.next();
        while (it.hasNext()) {
            SnakeSegment temp = it.next();

            switch (temp.getDir()) {
            case UP:
            case DOWN:
                if ((head.getCol1() == temp.getCol1() || head.getCol2() == temp.getCol2()) && temp.getRow1() <= row
                        && row <= temp.getRow2())
                    return true;
                break;
            case LEFT:
            case RIGHT:
                if ((head.getRow1() == temp.getRow1() || head.getRow2() == temp.getRow2()) && temp.getCol1() <= col
                        && col <= temp.getCol2())
                    return true;
                break;
            }
        }
        return false;
    }

    boolean isFoodOnSnake() {
        Iterator<SnakeSegment> it = segments.iterator();
        while (it.hasNext()) {
            SnakeSegment temp = it.next();
            switch (temp.getDir()) {
            case UP:
            case DOWN:
                for (int j = temp.getRow1(); j <= temp.getRow2(); j++) {
                    for (int i = -Game.snakeRadii / 2; i <= Game.snakeRadii / 2; i++) {
                        if (temp.getCol1() + i < Game.W && temp.getCol1() + i >= 0) {
                            if (Game.occupancyMatrix[j][temp.getCol1() + i] == 1) {
                                return true;
                            }
                        }
                    }
                }

                break;
            case LEFT:
            case RIGHT:
                for (int j = temp.getCol1(); j <= temp.getCol2(); j++) {
                    for (int i = -Game.snakeRadii / 2; i <= Game.snakeRadii / 2; i++) {
                        if (temp.getRow1() + i < Game.H && temp.getRow1() + i >= 0) {
                            if (Game.occupancyMatrix[temp.getRow1() + i][j] == 1) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    void shrinkTail() {
        SnakeSegment tail = segments.getLast();
        switch (tail.getDir()) {
        case UP:
            tail.setRow2(tail.getRow2() - 1);
            break;
        case DOWN:
            tail.setRow1(tail.getRow1() + 1);
            break;
        case LEFT:
            tail.setCol2(tail.getCol2() - 1);
            break;
        case RIGHT:
            tail.setCol1(tail.getCol1() + 1);
            break;
        }
        if ((tail.getRow1() > tail.getRow2()) || (tail.getCol1() > tail.getCol2())) {
            segments.removeLast();
        }
    }

    MoveResult expandHead() {
        SnakeSegment head = segments.getFirst();
        switch (head.getDir()) {
        case UP:
            head.setRow1(head.getRow1() - 1);
            break;
        case DOWN:
            head.setRow2(head.getRow2() + 1);
            break;
        case LEFT:
            head.setCol1(head.getCol1() - 1);
            break;
        case RIGHT:
            head.setCol2(head.getCol2() + 1);
            break;
        }
        if (!(checkHitBoard(head.getRow1(), head.getCol1()) && checkHitBoard(head.getRow2(), head.getCol1())
                && checkHitBoard(head.getRow1(), head.getCol2()) && checkHitBoard(head.getRow2(), head.getCol2()))) {
            return MoveResult.hitBoard;
        }
        if (isOnSnake(head.getRow2(), head.getCol2(), true))
            return MoveResult.hitSelf;

        if (onBomb(head.getRow1(), head.getCol1()) || onBomb(head.getRow2(), head.getCol1())
                || onBomb(head.getRow1(), head.getCol2()) || onBomb(head.getRow2(), head.getCol2())) {
            return MoveResult.onBomb;
        }
        MoveResult outcome = MoveResult.moved;
        for (int i = 0; i < food.size(); i++) {
            if (inRadius(head.getCol1(), food.get(i).getCol(), head.getRow1(), food.get(i).getRow(), Game.foodRadii)
                    || inRadius(head.getCol2(), food.get(i).getCol(), head.getRow2(), food.get(i).getRow(),
                            Game.foodRadii)) {
                Game.score += food.get(i).getScore();
                for (int m = -Game.foodRadii / 2; m <= Game.foodRadii / 2; m++) {
                    for (int j = -Game.foodRadii / 2; j <= Game.foodRadii / 2; j++) {
                        Game.occupancyMatrix[food.get(i).getRow() + m][food.get(i).getCol() + j] = 0;
                    }
                }
                food.remove(i);
                for (int ii = 0; ii < Game.snakeGrowth; ii++) {
                    outcome = expandHead();
                    if (outcome == MoveResult.hitSelf || outcome == MoveResult.hitBoard) {
                        return outcome;
                    }
                }
                if (outcome == MoveResult.moved) {
                    return MoveResult.ateFood;
                }

            }
        }

        return MoveResult.moved;
    }

    MoveResult newHead() {
        SnakeSegment prevHead = segments.getFirst();
        SnakeSegment newHead = new SnakeSegment(currDirection);
        switch (currDirection) {
        case UP:
            if (prevHead.getDir() == Direction.LEFT) {
                newHead.setRow1(prevHead.getRow1() - 1);
                newHead.setRow2(prevHead.getRow1() - 1);
                newHead.setCol1(prevHead.getCol1());
                newHead.setCol2(prevHead.getCol1());
            } else // right
            {
                newHead.setRow1(prevHead.getRow1() - 1);
                newHead.setRow2(prevHead.getRow1() - 1);
                newHead.setCol1(prevHead.getCol2());
                newHead.setCol2(prevHead.getCol2());
            }
            break;
        case DOWN:
            if (prevHead.getDir() == Direction.LEFT) {
                newHead.setRow1(prevHead.getRow1() + 1);
                newHead.setRow2(prevHead.getRow1() + 1);
                newHead.setCol1(prevHead.getCol1());
                newHead.setCol2(prevHead.getCol1());
            } else // right
            {
                newHead.setRow1(prevHead.getRow1() + 1);
                newHead.setRow2(prevHead.getRow1() + 1);
                newHead.setCol1(prevHead.getCol2());
                newHead.setCol2(prevHead.getCol2());
            }
            break;
        case LEFT:
            if (prevHead.getDir() == Direction.UP) {
                newHead.setRow1(prevHead.getRow1());
                newHead.setRow2(prevHead.getRow1());
                newHead.setCol1(prevHead.getCol1() - 1);
                newHead.setCol2(prevHead.getCol1() - 1);
            } else // down
            {
                newHead.setRow1(prevHead.getRow2());
                newHead.setRow2(prevHead.getRow2());
                newHead.setCol1(prevHead.getCol2() - 1);
                newHead.setCol2(prevHead.getCol2() - 1);
            }
            break;
        case RIGHT:
            if (prevHead.getDir() == Direction.UP) {
                newHead.setRow1(prevHead.getRow1());
                newHead.setRow2(prevHead.getRow1());
                newHead.setCol1(prevHead.getCol1() + 1);
                newHead.setCol2(prevHead.getCol1() + 1);
            } else // down
            {
                newHead.setRow1(prevHead.getRow2());
                newHead.setRow2(prevHead.getRow2());
                newHead.setCol1(prevHead.getCol1() + 1);
                newHead.setCol2(prevHead.getCol1() + 1);
            }
            break;
        }
        segments.addFirst(newHead);
        SnakeSegment head = segments.getFirst();
        if (!(checkHitBoard(head.getRow1(), head.getCol1()) && checkHitBoard(head.getRow2(), head.getCol1())
                && checkHitBoard(head.getRow1(), head.getCol2()) && checkHitBoard(head.getRow2(), head.getCol2()))) {
            return MoveResult.hitBoard;
        }
        if (isOnSnake(head.getRow2(), head.getCol2(), true))
            return MoveResult.hitSelf;

        if ((onBomb(head.getRow1(), head.getCol1()) || onBomb(head.getRow2(), head.getCol1())
                || onBomb(head.getRow1(), head.getCol2()) || onBomb(head.getRow2(), head.getCol2()))) {
            return MoveResult.onBomb;
        }

        MoveResult outcome = MoveResult.moved;

        for (int i = 0; i < food.size(); i++) {
            if (inRadius(head.getCol1(), food.get(i).getCol(), head.getRow1(), food.get(i).getRow(), Game.foodRadii)
                    || inRadius(head.getCol2(), food.get(i).getCol(), head.getCol2(), food.get(i).getRow(),
                            Game.foodRadii)) {
                Game.score += food.get(i).getScore();
                for (int m = -Game.foodRadii / 2; m <= Game.foodRadii / 2; m++) {
                    for (int j = -Game.foodRadii / 2; j <= Game.foodRadii / 2; j++) {
                        Game.occupancyMatrix[food.get(i).getRow() + m][food.get(i).getCol() + j] = 0;
                    }
                }
                food.remove(i);

                for (int ii = 0; ii < Game.snakeGrowth; ii++) {
                    outcome = expandHead();
                    if (outcome == MoveResult.hitSelf || outcome == MoveResult.hitBoard) {
                        return outcome;
                    }
                }

                if (outcome == MoveResult.moved) {

                    return MoveResult.ateFood;
                }

            }
        }
        return MoveResult.moved;
    }

    MoveResult moveUp() {
        SnakeSegment head = segments.getFirst();
        MoveResult outcome = MoveResult.moved;
        switch (head.getDir()) {
        case UP:
            outcome = expandHead();
            shrinkTail();
            break;
        case DOWN:
            break;
        case LEFT:
            outcome = newHead();
            shrinkTail();
            break;
        case RIGHT:
            outcome = newHead();
            shrinkTail();
            break;
        }
        return outcome;
    }

    MoveResult moveDown() {
        SnakeSegment head = segments.getFirst();
        MoveResult outcome = MoveResult.moved;
        switch (head.getDir()) {
        case UP:
            break;
        case DOWN:
            outcome = expandHead();
            shrinkTail();
            break;
        case LEFT:
            outcome = newHead();
            shrinkTail();
            break;
        case RIGHT:
            outcome = newHead();
            shrinkTail();
            break;
        }
        return outcome;
    }

    MoveResult moveLeft() {
        SnakeSegment head = segments.getFirst();
        MoveResult outcome = MoveResult.moved;
        switch (head.getDir()) {
        case UP:
            outcome = newHead();
            shrinkTail();
            break;
        case DOWN:
            outcome = newHead();
            shrinkTail();
            break;
        case LEFT:
            outcome = expandHead();
            shrinkTail();
            break;
        case RIGHT:
            break;
        }
        return outcome;
    }

    MoveResult moveRight() {
        SnakeSegment head = segments.getFirst();
        MoveResult outcome = MoveResult.moved;
        switch (head.getDir()) {
        case UP:
            outcome = newHead();
            shrinkTail();
            break;
        case DOWN:
            outcome = newHead();
            shrinkTail();
            break;
        case LEFT:
            break;
        case RIGHT:
            outcome = expandHead();
            shrinkTail();
            break;
        }
        return outcome;
    }

    private boolean inRadiusBomb(int x, int y, int bombNumber) {
        int x1 = Game.bombList.get(bombNumber).getX() + Game.bombRadii / 2;
        int y1 = Game.bombList.get(bombNumber).getY() + Game.bombRadii / 2;
        if ((x - x1) * (x - x1) + (y - y1) * (y - y1) < Game.bombList.get(bombNumber).getBlastRadius()
                * Game.bombList.get(bombNumber).getBlastRadius() / 4) {
            return true;
        }
        return false;
    }

    public boolean isOnBomb(int bombNumber) {

        Iterator<SnakeSegment> it = segments.iterator();
        while (it.hasNext()) {
            SnakeSegment temp = it.next();
            switch (temp.getDir()) {
            case UP:
            case DOWN: {
                for (int i = temp.getRow1(); i <= temp.getRow2(); i++) {
                    for (int j = 0; j <= Game.snakeRadii; j++) {
                        if (inRadiusBomb(temp.getCol1() + j, i, bombNumber)) {
                            return true;
                        }
                    }
                }
            }
                break;
            case LEFT:
            case RIGHT: {
                for (int i = temp.getCol1(); i <= temp.getCol2(); i++) {
                    for (int j = 0; j <= Game.snakeRadii; j++) {
                        if (inRadiusBomb(i, temp.getRow1() + j, bombNumber)) {
                            return true;
                        }
                    }
                }

            }
            }
        }
        return false;

    }
}