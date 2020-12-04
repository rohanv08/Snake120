
public class SnakeSegment {

    Direction dir;
    int row1;
    int row2;
    int col1;
    int col2;
    public SnakeSegment () {
        
    }
    public SnakeSegment(int row1, int row2, int col1, int col2, Direction dir) {
        this.col1 = col1;
        this.col2 = col2;
        this.row1 = row1;
        this.row2 = row2;
        this.dir = dir;
    }

}
