import java.util.Iterator;
import java.util.LinkedList;

enum Direction {
    UP, DOWN, LEFT, RIGHT
};

enum MoveResult {
    moved, hitBoard, hitSelf, ateFood
};

public class Snake {
    public boolean inRadius(int x1, int x2, int y1, int y2, int radii) {
        // 1- food, 2 - snake
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

    Direction currDirection;
    boolean hasBomb = false;
    LinkedList<SnakeSegment> segments = new LinkedList<>();
    LinkedList<Food> food = new LinkedList<>();

    boolean offerFood(int foodRow, int foodCol) {
        if (isOnSnake(foodRow, foodCol, false))
            return false;
        food.add(new Food(foodRow, foodCol, 1));
        return true;
    }

    void setBombMatrix() {
        for (int i = 0; i < Game.numberOfBombs; i++) {
            for (int j = 0; j < Game.numberOfBombs; j++) {
                Game.bombMatrix[i][j] = 0;
            }
        }
        for (int i = 0; i < Game.numberOfBombs; i++) {
            for (int j = i; j < Game.numberOfBombs; j++) {
                if (i != j && Math.random() >= 0.5) {
                    Game.bombMatrix[i][j] = 1;
                    Game.bombMatrix[j][i] = 1; // undirected graph
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

    public Snake() {
        SnakeSegment start = new SnakeSegment(50, 50, 50, 150, Direction.RIGHT);
        segments.add(start);
        currDirection = Direction.RIGHT;
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

            switch (temp.dir) {

            case UP:
            case DOWN:
       
                if ((head.col1 == temp.col1 || head.col2 == temp.col2) && temp.row1 <= row && row <= temp.row2)
                    return true;
                break;
            case LEFT:
            case RIGHT:
                if ((head.row1 == temp.row1 || head.row2 == temp.row2) && temp.col1 <= col && col <= temp.col2)
                    return true;
                break;
            }
        }
        return false;
    }

    void shrinkTail() {
        SnakeSegment tail = segments.getLast();
        switch (tail.dir) {
        case UP:
            tail.row2--;
            break;
        case DOWN:
            tail.row1++;
            break;
        case LEFT:
            tail.col2--;
            break;
        case RIGHT:
            tail.col1++;
            break;
        }
        if ((tail.row1 > tail.row2) || (tail.col1 > tail.col2)) {
            segments.removeLast();
        }
    }

    MoveResult expandHead() {
        SnakeSegment head = segments.getFirst();
        switch (head.dir) {
        case UP:
            head.row1--;
            break;
        case DOWN:
            head.row2++;
            break;
        case LEFT:
            head.col1--;
            break;
        case RIGHT:
            head.col2++;
            break;
        }
        if (head.row1 < 1 || head.row2 >= Game.H - 1)
            return MoveResult.hitBoard;
        if (head.col1 < 1 || head.col2 >= Game.W - 1)
            return MoveResult.hitBoard;
        if (isOnSnake(head.row2, head.col2, true))
            return MoveResult.hitSelf;

        MoveResult outcome = MoveResult.moved;
        for (int i = 0; i < Game.numberOfBombs && Game.bombs; i++) {
            if ((inRadius(head.col1, Game.bombList.get(i).getX(), head.row1, Game.bombList.get(i).getY(), Game.bombRadii)
                    || inRadius(head.col2, Game.bombList.get(i).getX(), head.row2, Game.bombList.get(i).getY(),
                            Game.bombRadii))
                    && !Game.bombList.get(i).blast) {
                outcome = MoveResult.hitBoard;
                return outcome;
            }
        }
        for (int i = 0; i < food.size(); i++) {
            if (inRadius(head.col1, food.get(i).getCol(), head.row1, food.get(i).getRow(), Game.foodRadii)
                    || inRadius(head.col2, food.get(i).getCol(), head.row2, food.get(i).getRow(), Game.foodRadii)) {
                Game.score += food.get(i).getScore();
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
        SnakeSegment newHead = new SnakeSegment();
        newHead.dir = currDirection;
        switch (currDirection) {
        case UP:
            if (prevHead.dir == Direction.LEFT) {
                newHead.row1 = prevHead.row1 - 1;
                newHead.row2 = prevHead.row1 - 1;
                newHead.col1 = prevHead.col1;
                newHead.col2 = prevHead.col1;
            } else // right
            {
                newHead.row1 = prevHead.row1 - 1;
                newHead.row2 = prevHead.row1 - 1;
                newHead.col1 = prevHead.col2;
                newHead.col2 = prevHead.col2;
            }
            break;
        case DOWN:
            if (prevHead.dir == Direction.LEFT) {
                newHead.row1 = prevHead.row1 + 1;
                newHead.row2 = prevHead.row1 + 1;
                newHead.col1 = prevHead.col1;
                newHead.col2 = prevHead.col1;
            } else // right
            {
                newHead.row1 = prevHead.row1 + 1;
                newHead.row2 = prevHead.row1 + 1;
                newHead.col1 = prevHead.col2;
                newHead.col2 = prevHead.col2;
            }
            break;
        case LEFT:
            if (prevHead.dir == Direction.UP) {
                newHead.row1 = prevHead.row1;
                newHead.row2 = prevHead.row1;
                newHead.col1 = prevHead.col1 - 1;
                newHead.col2 = prevHead.col1 - 1;
            } else // down
            {
                newHead.row1 = prevHead.row2;
                newHead.row2 = prevHead.row2;
                newHead.col1 = prevHead.col2 - 1;
                newHead.col2 = prevHead.col2 - 1;
            }
            break;
        case RIGHT:
            if (prevHead.dir == Direction.UP) {
                newHead.row1 = prevHead.row1;
                newHead.row2 = prevHead.row1;
                newHead.col1 = prevHead.col1 + 1;
                newHead.col2 = prevHead.col1 + 1;
            } else // down
            {
                newHead.row1 = prevHead.row2;
                newHead.row2 = prevHead.row2;
                newHead.col1 = prevHead.col1 + 1;
                newHead.col2 = prevHead.col1 + 1;
            }
            break;
        }
        segments.addFirst(newHead);
        SnakeSegment head = segments.getFirst();
        if (head.row1 < 1 || head.row2 >= Game.H - 1)
            return MoveResult.hitBoard;
        if (head.col1 < 1 || head.col2 >= Game.W - 1)
            return MoveResult.hitBoard;
        if (isOnSnake(head.row2, head.col2, true))
            return MoveResult.hitSelf;

        MoveResult outcome = MoveResult.moved;
        for (int i = 0; i < Game.numberOfBombs && Game.bombs; i++) {
            if ((inRadius(head.col1, Game.bombList.get(i).getX(), head.row1, Game.bombList.get(i).getY(), Game.bombRadii)
                    || inRadius(head.col2, Game.bombList.get(i).getX(), head.row2, Game.bombList.get(i).getY(),
                            Game.bombRadii)) && !Game.bombList.get(i).blast) {
                outcome = MoveResult.hitBoard;
                return outcome;
            }
        }
        for (int i = 0; i < food.size(); i++) {
            if (inRadius(head.col1, food.get(i).getCol(), head.row1, food.get(i).getRow(), Game.foodRadii)
                    || inRadius(head.col2, food.get(i).getCol(), head.row2, food.get(i).getRow(), Game.foodRadii)) {
                Game.score += food.get(i).getScore();
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
        switch (head.dir) {
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
        switch (head.dir) {
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
        switch (head.dir) {
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
        switch (head.dir) {
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

    public boolean inRadiusBomb(int x, int y, int bombNumber) {
        int x1 = Game.bombList.get(bombNumber).getX() + Game.bombRadii / 2;
        int y1 = Game.bombList.get(bombNumber).getY() + Game.bombRadii / 2;
        if ((x - x1) * (x - x1) + (y - y1) * (y - y1) < Game.bombList.get(bombNumber).getBlastRadius()
                * Game.bombList.get(bombNumber).getBlastRadius()/ 4) {
            return true;
        }
        return false;
    }
    
   

    public boolean isOnBomb(int bombNumber) {

        Iterator<SnakeSegment> it = segments.iterator();
        while (it.hasNext()) {
            SnakeSegment temp = it.next();
            switch (temp.dir) {
            case UP: {
                for (int i = temp.row1; i <= temp.row2; i++) {
                    for (int j = 0; j <= Game.snakeRadii; j++) {
                        if (inRadiusBomb(temp.col1 + j, i, bombNumber)) {
                            return true;
                        }
                    }
                }
            }
            case DOWN: {
                for (int i = temp.row1; i <= temp.row2; i++) {
                    for (int j = 0; j <= Game.snakeRadii; j++) {
                        if (inRadiusBomb(temp.col1 + j, i, bombNumber)) {
                            return true;
                        }
                    }
                }
            }
            case LEFT: {
                for (int i = temp.col1; i <= temp.col2; i++) {
                    for (int j = 0; j <= Game.snakeRadii; j++) {
                        if (inRadiusBomb(i, temp.row1 + j, bombNumber)) {
                            return true;
                        }
                    }
                }

            }
            case RIGHT: {
                for (int i = temp.col1; i <= temp.col2; i++) {
                    for (int j = 0; j <= Game.snakeRadii; j++) {
                        if (inRadiusBomb(i, temp.row1 + j, bombNumber)) {
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