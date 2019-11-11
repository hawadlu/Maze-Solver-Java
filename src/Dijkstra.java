import java.util.*;

/**
 * This class implements a Dijkstra type search
 */

public class Dijkstra {
    int pathSize = 0;
    ArrayList<MazeNode> path = new ArrayList<>(); //The path
    Set<MazeNode> visited = new HashSet<>(); //Set of visited nodes

    /**
     * Constructor
     */
    public Dijkstra() {
    }

    /**
     * Solves the maze node.
     * Uses iteration to avoid stack overflow error
     */
    public void solve(MazeNode start, MazeNode destination) {
        MazeNode parent = null;
        PriorityQueue<MazeNode> toProcess = new PriorityQueue<>();
        start.setCost(0);
        toProcess.offer(start);


        while (!toProcess.isEmpty()) {
            parent = toProcess.peek();
            parent.visit();

            if (parent.equals(destination)) {
                break;
            } else {

                //Add the children
                for (MazeNode node: toProcess.poll().getNeighbours()) {
                    double cost = parent.getCost() + calculateCost(parent, node);
                    if (cost < node.getCost()) {
                        node.setCost(cost);
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
     */
    public double calculateCost(MazeNode start, MazeNode destination) {
        return Math.sqrt(Math.pow(start.getX() + destination.getX(), 2) + Math.pow(start.getY() + destination.getY(), 2));
    }
}
