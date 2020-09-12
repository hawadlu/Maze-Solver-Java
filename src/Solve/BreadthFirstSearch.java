package Solve;

import Image.ImageFile;
import Location.Coordinates;
import customExceptions.SolveFailureException;

import javax.swing.*;
import java.util.*;

/**
 * This class solves the maze breadth first.
 * It takes a start node, end node and and returns a path between them
 */
public class BreadthFirstSearch extends Algorithms {
    /**
     * Constructor
     */
    public BreadthFirstSearch() {}

    /**
     * Solves the maze node.
     * Uses iteration to avoid stack overflow error
     */
    public void solve(ImageFile imgObj, MazeNode start, MazeNode destination, HashMap<Coordinates, MazeNode> nodes, JPanel parentComponent) throws SolveFailureException {
        MazeNode parent = null;
        Queue<MazeNode> toProcess = new ArrayDeque<>();
        start.visit();
        toProcess.offer(start);
        int initialNodeSize = nodes.size();


        while (!toProcess.isEmpty()) {
            parent = toProcess.poll();
            parent.visit();

            if (parent.equals(destination)) break;

            //Choose how to get neighbours
            ArrayList<Coordinates> nodeLocations = getNeighbours(imgObj, nodes, parent, initialNodeSize);

            //Add all the appropriate neighbours to the stack
            for (Coordinates location: nodeLocations) {
                MazeNode node = nodes.get(new Coordinates(location.getX(), location.getY()));

                //If there is no node here, make one
                if (node == null) {
                    node = new MazeNode(location.getX(), location.getY());
                    nodes.put(new Coordinates(location.getX(), location.getY()), node);
                }

                //Add the node to the queue
                if (!node.isVisited()) {
                    node.setParent(parent);
                    toProcess.offer(node);
                }
            }

            //If the stack is empty at this point, solving failed
            if (toProcess.isEmpty()) throw new SolveFailureException("Failed to solve " + imgObj.getAbsolutePath(), parentComponent);
        }

        //Retrace the path
        backtrack(parent);
    }
}
