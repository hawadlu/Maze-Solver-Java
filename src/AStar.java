import customExceptions.SolveFailureException;

import javax.swing.*;
import java.util.*;

/**
 * This class implements an AStar type search
 */

class AStar extends Algorithms{
    /**
     * Solves the maze node.
     * Uses iteration to avoid stack overflow error
     */
    public void solve(ImageFile imgObj, MazeNode start, MazeNode destination, HashMap<Coordinates, MazeNode> nodes, JPanel parentComponent) throws SolveFailureException {
        System.out.println("Solve started");
        MazeNode parent = null;
        PriorityQueue<MazeNode> toProcess = setupQueueWithCost(start);
        int initialNodeSize = nodes.size();

        while (!toProcess.isEmpty()) {
            parent = toProcess.poll();
            parent.visit();

            //Have we reached the end?
            if (parent.equals(destination)) break;

            //Choose how to get neighbours
            ArrayList<Coordinates> nodeLocations = getNeighbours(imgObj, nodes, parent, initialNodeSize);

            for (Coordinates location: nodeLocations) {
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

            //If the stack is empty at this point, solving failed
            if (toProcess.isEmpty()) throw new SolveFailureException("Failed to solve " + imgObj.getAbsolutePath(), parentComponent);

        }

        //Back track to get the path
        backtrack(parent);
        System.out.println("Traced path from destination to start.");
    }
}
