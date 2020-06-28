import java.util.*;

/**
 * This class implements an AStar type search
 */

class AStar extends Algorithms{
    /**
     * Constructor
     */
    public AStar() {}

    /**
     * Solves the maze node.
     * Uses iteration to avoid stack overflow error
     */
    public void solve(ImageFile imgObj, MazeNode start, MazeNode destination, HashMap<Coordinates, MazeNode> nodes) {
        System.out.println("Solve started");
        MazeNode parent = null;
        PriorityQueue<MazeNode> toProcess = setupQueueWithCost(start);

        //Choose how the maze should run based on how many nodes have been added
        if (nodes.size() > 2) {
            while (!toProcess.isEmpty()) {
                parent = toProcess.peek();
                parent.visit();

                if (parent.equals(destination)) {
                    break;
                } else {

                    //Add the children
                    for (Coordinates location : Objects.requireNonNull(toProcess.poll()).getNeighbours()) {
                        //Get the node
                        MazeNode node = nodes.get(new Coordinates(location.x, location.y));

                        //Set the estimated heuristic cost of visiting this node
                        if (node != destination && node.getHeuristicCost() == 0) {
                            node.setHeuristicCost(calculateCost(node, destination));
                        }

                        compareCost(parent, toProcess, node);
                    }

                }
            }
        } else {
            //Nodes are being found on the fly
            while (!toProcess.isEmpty()) {
                parent = toProcess.peek();
                parent.visit();

                if (parent.equals(destination)) {
                    break;
                } else {

                    //Add the children
                    MazeNode current = toProcess.poll();
                    for (Coordinates location : ImageManipulation.findNeighboursForSingleSolveTime(imgObj, nodes, current.getX(), current.getY())) {
                    //for (Coordinates location : Objects.requireNonNull(toProcess.poll()).getNeighbours(imgObj, nodes)) {
                        //Get the node
                        MazeNode node = nodes.get(new Coordinates(location.x, location.y));

                        //If there is no node here, make one
                        if (node == null) {
                            node = new MazeNode(location.x, location.y);
                            nodes.put(new Coordinates(location.x, location.y), node);
                        }

                        //Set the estimated heuristic cost of visiting this node
                        if (node != destination && node.getHeuristicCost() == 0) {
                            node.setHeuristicCost(calculateCost(node, destination));
                        }

                        //Calculate the cost in terms of euclidean distance of moving from the parent to this node
                        compareCost(parent, toProcess, node);
                    }

                }
            }
        }

        //trace the path back to the start
        backtrack(parent);
        System.out.println("Traced path from destination to start.");
    }
}
