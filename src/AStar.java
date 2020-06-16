import java.util.*;

/**
 * This class implements an AStar type search
 */

class AStar {
    private int pathSize = 0;
    private final ArrayList<MazeNode> path = new ArrayList<>(); //The path

    /**
     * Constructor
     */
    public AStar() {
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

                    //Set the estimated heuristic cost of visiting this node
                    if (node != destination && node.getHeuristicCost() == 0) { node.setHeuristicCost(calculateCost(node, destination)); }

                    //Calculate the cost in terms of euclidean distance of moving from the parent to this node
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
     * Calculates the euclidean distance between two nodes
     */
    private double calculateCost(MazeNode current, MazeNode other) {
        double yDist = Math.abs(current.getY() - other.getY());
        double xDist = Math.abs(current.getX() - other.getX());
        return Math.sqrt(Math.pow(yDist, 2) + Math.pow(xDist, 2));
    }
}
