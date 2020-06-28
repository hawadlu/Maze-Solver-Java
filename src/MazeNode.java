import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * This class contains each node in the maze. It has fields to store the position and neighbours.
 * The maximum size of this object is somewhere in the region of 114 bytes
 */

public class MazeNode implements Comparable<MazeNode>{
    private final int xPos;
    private final int yPos;
    private double pathCost = Double.POSITIVE_INFINITY;
    private double heuristicCost = 0.0; //Estimated cost from this node to the end
    private final Set<Coordinates> neighbours = new HashSet<>();//Set that stores the x any y of neighbours
    private Boolean isVisited = false;
    private MazeNode parent = null; //Used to track which node this was visited from

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
     * Get and set the neighbours
     */
    public void addNeighbour(Coordinates newCoords) {
        neighbours.add(newCoords);
    }

    public Set<Coordinates> getNeighbours() {
        return neighbours;
    }



    /**
     * Visit and un-visit
     */
    public boolean isVisited() {return isVisited; }
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
    public double getPathCost() { return pathCost; }

    public double getHeuristicCost() {
        return heuristicCost;
    }

    public void setPathCost(double newCost) { pathCost = newCost; }

    public void setHeuristicCost(double newCost) { heuristicCost = newCost; }



    /**
     * toString
     */
    public String toString() {
        return "x: " + getX() + " y:" + getY() + " num neighbours: ";
    }

    /**
     * Comparator
     */
    public int compareTo(MazeNode nextNode) {
        return Double.compare(this.pathCost + this.heuristicCost, nextNode.pathCost + nextNode.heuristicCost);
    }
}