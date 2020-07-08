import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.PriorityQueue;

/**
 * Class containing common method implementations and variables used between the various solve algorithms
 */
public class Algorithms {
    public int pathSize = 0;
    public final ArrayList<MazeNode> path = new ArrayList<>(); //The path


    /**
     * Get the path
     */
    public ArrayList<MazeNode> getPath() {
        return path;
    }

    /**
     * Return the size of the path
     */
    public int getPathSize(){
        return pathSize;
    }

    /**
     * Setup a priority queue
     * @param start the node to put in first
     * @return the new queue
     */
    public PriorityQueue<MazeNode> setupQueueWithCost(MazeNode start) {
        PriorityQueue<MazeNode> toProcess = new PriorityQueue<>();
        start.setPathCost(0);
        toProcess.offer(start);
        return toProcess;
    }

    /**
     * Trace the path from the parent back to the start
     * @param parent the node at the end
     */
    public void backtrack(MazeNode parent) {
        while (true) {
            if (parent != null) {
                path.add(parent);
                parent = parent.getParent();
                pathSize++;
            } else {
                break;
            }
        }
    }

    /**
     * Calculates the euclidean distance between two nodes
     */
    public double calculateCost(MazeNode current, MazeNode other) {
        double yDist = Math.abs(current.getY() - other.getY());
        double xDist = Math.abs(current.getX() - other.getX());
        return Math.sqrt(Math.pow(yDist, 2) + Math.pow(xDist, 2));
    }

    /**
     * Compare the cost of two nodes to decide what should happen
     * @param parent Parent of the current node
     * @param toProcess the priority queue
     * @param node the current node
     */
    public void compareCost(MazeNode parent, PriorityQueue<MazeNode> toProcess, MazeNode node) {
        //The cost function calculates the euclidean distance between this node and the start.
        double cost = parent.getPathCost() + calculateCost(parent, node);
        if (cost < node.getPathCost()) {
            node.setPathCost(cost);
            node.setParent(parent);
            toProcess.offer(node);
        }
    }

    /**
     * Choose the appropriate way to get neighbours for this node
     * @param imgObj the image object
     * @param nodes all the nodes in the maze so far
     * @param parent the current node
     * @param initialNodeSize the size of the nodes hashset when solving began
     * @return arraylist of node locations
     */
    public ArrayList<Coordinates> getNeighbours(ImageFile imgObj, HashMap<Coordinates, MazeNode> nodes, MazeNode parent, int initialNodeSize) {
        ArrayList<Coordinates> nodeLocations = new ArrayList<>();
        if (initialNodeSize > 2) nodeLocations.addAll(Objects.requireNonNull(parent.getNeighbours()));
        else nodeLocations.addAll(ImageManipulation.findNeighboursForSingleSolveTime(imgObj, nodes, parent.getX(), parent.getY()));
        return nodeLocations;
    }
}
