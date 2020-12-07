
public class SnakeSegment {

    private Direction dir;
    private int row1, row2, col1, col2;

    public SnakeSegment() {

    }

    public SnakeSegment(Direction dir) {
        this.dir = dir;
    }

    public SnakeSegment(int row1, int row2, int col1, int col2, Direction dir) {
        this.col1 = col1;
        this.col2 = col2;
        this.row1 = row1;
        this.row2 = row2;
        this.dir = dir;
    }

    public int getRow1() {
        return row1;
    }

    public int getRow2() {
        return row2;
    }

    public int getCol1() {
        return col1;
    }

    public int getCol2() {
        return col2;
    }

    public void setRow1(int value) {
        row1 = value;
    }

    public void setRow2(int value) {
        row2 = value;
    }

    public void setCol1(int value) {
        col1 = value;
    }

    public void setCol2(int value) {
        col2 = value;
    }

    public Direction getDir() {
        return dir;
    }

}
