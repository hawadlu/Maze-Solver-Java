package Solve;

import Image.ImageFile;
import Location.Coordinates;
import customExceptions.SolveFailureException;

import javax.swing.*;
import java.util.*;

/**
 * This class implements a Dijkstra type search
 */
public class DijkstraSearch extends Algorithms {

    /**
     * Solves the maze node.
     * Uses iteration to avoid stack overflow error
     */
    public void solve(ImageFile imgObj, MazeNode start, MazeNode destination, HashMap<Coordinates, MazeNode> nodes, JPanel parentComponent) throws SolveFailureException {
        MazeNode parent = null;
        PriorityQueue<MazeNode> toProcess = setupQueueWithCost(start);
        int initialNodeSize = nodes.size();

        while (!toProcess.isEmpty()) {
            parent = toProcess.poll();
            parent.visit();

            ArrayList<Coordinates> nodeLocations = getNeighbours(imgObj, nodes, parent, initialNodeSize);

            if (parent.equals(destination)) break;

            for (Coordinates location: nodeLocations) {
                //If there is no node here, make one
                MazeNode node = nodes.get(new Coordinates(location.getX(), location.getY()));

                if (node == null) {
                    node = new MazeNode(location.getX(), location.getY());
                    nodes.put(new Coordinates(location.getX(), location.getY()), node);
                }

                compareCost(parent, toProcess, node);
            }

            //If the stack is empty at this point, solving failed
            if (toProcess.isEmpty()) throw new SolveFailureException("Failed to solve " + imgObj.getAbsolutePath(), parentComponent);
        }

        //Check if the neighbours have already been located
//        if (nodes.size() > 2) {
//            while (!toProcess.isEmpty()) {
//                parent = toProcess.peek();
//                parent.visit();
//
//                if (parent.equals(destination)) {
//                    break;
//                } else {
//
//                    //Add the children
//                    for (Location.Coordinates location : Objects.requireNonNull(toProcess.poll()).getNeighbours()) {
//                        //Get the node
//                        Solve.MazeNode node = nodes.get(new Location.Coordinates(location.getX(), location.getY()));
//                        compareCost(parent, toProcess, node);
//                    }
//
//                }
//            }
//        } else {
//            while (!toProcess.isEmpty()) {
//                parent = toProcess.peek();
//                parent.visit();
//
//                if (parent.equals(destination)) {
//                    break;
//                } else {
//
//                    //Add the children
//                    Solve.MazeNode current = toProcess.poll();
//                    for (Location.Coordinates location : Image.ImageManipulation.findNeighboursForSingleSolveTime(imgObj, nodes, current.getX(), current.getY())) {
//                        //If there is no node here, make one
//                        Solve.MazeNode node = nodes.get(new Location.Coordinates(location.getX(), location.getY()));
//
//                        if (node == null) {
//                            node = new Solve.MazeNode(location.getX(), location.getY());
//                            nodes.put(new Location.Coordinates(location.getX(), location.getY()), node);
//                        }
//
//                        compareCost(parent, toProcess, node);
//                    }
//
//                }
//            }
//        }

        //trace the path back to the start
        backtrack(parent);
    }
}
