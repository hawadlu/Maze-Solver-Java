//Todo make an abstract class for all the common methods?
import java.util.*;

/**
 * This class implements a Dijkstra type search
 */

class Dijkstra {
    private int pathSize = 0;
    private final ArrayList<MazeNode> path = new ArrayList<>(); //The path

    /**
     * Constructor
     */
    public Dijkstra() {
    }

    /**
     * Solves the maze node.
     * Uses iteration to avoid stack overflow error
     */
    public void solve(MazeNode start, MazeNode destination, HashMap<Coordinates, MazeNode> nodes) {
        MazeNode parent = null;
        PriorityQueue<MazeNode> toProcess = new PriorityQueue<>();
        start.setPathCost(0);
        toProcess.offer(start);


        while (!toProcess.isEmpty()) {
            parent = toProcess.peek();
            parent.visit();

            if (parent.equals(destination)) {
                break;
            } else {

                //Add the children
                for (Coordinates location: Objects.requireNonNull(toProcess.poll()).getNeighbours()) {
                    //Get the node
                    MazeNode node = nodes.get(new Coordinates(location.x, location.y));
                    double cost = parent.getPathCost() + calculateCost(parent, node);
                    if (cost < node.getPathCost()) {
                        node.setPathCost(cost);
                        node.setParent(parent);
                        toProcess.offer(node);
                    }
                }

            }
        }

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
     * Calculates the cost of moving between two nodes
     * Factors the distance between nodes (current and destination) and distance of the destination node to the start.
     */
    /**
     * Calculates the euclidean distance between two nodes
     */
    private double calculateCost(MazeNode current, MazeNode other) {
        double yDist = Math.abs(current.getY() - other.getY());
        double xDist = Math.abs(current.getX() - other.getX());
        return Math.sqrt(Math.pow(yDist, 2) + Math.pow(xDist, 2));
    }
}
