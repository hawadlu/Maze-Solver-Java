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
                for (MazeNode node: Objects.requireNonNull(toProcess.poll()).getNeighbours()) {
                    if (!node.isVisited()) {
                        double cost = parent.getCost() + calculateCost(parent, node);
                        if (cost < node.getCost()) {
                            node.setCost(cost);
                            node.setParent(parent);
                            toProcess.offer(node);
                        }
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
    private double calculateCost(MazeNode current, MazeNode destination) {
        return Math.sqrt(Math.pow(current.getX() + destination.getX(), 2) + Math.pow(current.getY() + destination.getY(), 2)) + destination.getY();
    }
}
