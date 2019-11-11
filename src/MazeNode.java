import java.io.Serializable;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

/**
 * This class contains each node in the maze. It has fields to store the position and neighbours.
 */

public class MazeNode implements Comparable<MazeNode>{
    int xPos;
    int yPos;
    double cost = Double.POSITIVE_INFINITY;
    Set<MazeNode> neighbours = new HashSet<>();
    Boolean isVisited = false;
    MazeNode parent = null; //Used to track which node this was visited from

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

    /**
     * Visit and un-visit
     */
    public boolean isVisted() {return isVisited; }
    public void visit() { isVisited = true; }
    //public void unVisit() { isVisited = false; }

    /**
     * Get and set the parent
     */
    public void setParent(MazeNode node) {
        parent = node;
    }

    public MazeNode getParent() {
        return parent;
    }

    /**
     * Get and set the cost
     */
    public double getCost() { return cost; }
    public void  setCost(double newCost) { cost = newCost; }


    /**
     * toString
     */
    public String toString() {
        return "x: " + getX() + " y:" + getY() + " num neighbours: " + neighbours.size();
    }

    /**
     * Comparator
     */
    public int compareTo(MazeNode nextNode) {
        if (this.getCost() > nextNode.getCost()) {
            return 1;
        } else {
            return -1;
        }
    }

}