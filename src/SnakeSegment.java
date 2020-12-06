
public class SnakeSegment {

    Direction dir;
    public int row1, row2, col1, col2;

    public SnakeSegment() {

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

    public void addRow1(int value) {
        row1+=value;
    }

    public void addRow2(int value) {
        row2+=value;
    }
    public void addCol1(int value) {
        col1+=value;
    }
    public void addCol2(int value) {
        col2+=value;
    }
    public Direction getDir () {
        return dir;
    }
    

}
