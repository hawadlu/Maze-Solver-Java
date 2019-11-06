import java.util.HashSet;
import java.util.Set;

/**
 * This class contains each node in the maze. It has fields to store the position and neighbours.
 */

public class MazeNode {
    int xPos;
    int yPos;
    Set<MazeNode> neighbours = new HashSet<>();

    /**
     * Make a new node
     */
    public MazeNode(int x, int y){
        xPos = x;
        yPos = y;
    }

    /**
     * Get the x and y values
     */
    public int getX(){
        return xPos;
    }

    public int getY() {
        return yPos;
    }

    /**
     * Set the x and y values
     */
    public void setX(int x) { xPos = x; }

    public void setY(int y) { yPos = y; }

    /**
     * Get and set the neighbours
     */
    public void addNeighbour(MazeNode neighbour) {
        neighbours.add(neighbour);
    }

    public Set<MazeNode> getNeighbours() {
        return neighbours;
    }

}