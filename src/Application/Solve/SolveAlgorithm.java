package Application.Solve;

import Application.Application;
import Utility.Exceptions.InvalidMaze;
import Utility.Image.ImageProcessor;
import Utility.Location;
import Utility.Node;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

/**
 * This class contains common methdods that are used by all algorithms
 */
public class SolveAlgorithm {
    boolean scanAll = false;
    ImageProcessor processor;
    HashMap<Location, Node> nodes = new HashMap<>();
    Node entry, exit;

    /**
     * Start the solve process.
     * @param currentApplication  the current application.
     */
    public SolveAlgorithm(Application currentApplication) {
        this.processor = new ImageProcessor(currentApplication);
    }

    public void Solve(String algorithm) {
        if (algorithm.endsWith("Depth First")) new DepthFirst().solve(this);
    }

    public void Scan(String params) {
        if (params.equals("During Loading")) {
            processor.scanAll();
            scanAll = true;
        } else {
            processor.findExits();
        }

        nodes = processor.getNodes();

        ArrayList<Location> exits = processor.getExits();

        //make sure there is at least one entry and exit
        if (exits.size() < 2) try {
            throw new InvalidMaze("Not enough exits");
        } catch (InvalidMaze invalidMaze) {
            invalidMaze.printStackTrace();
        }

        entry = nodes.get(exits.get(0));
        exit = nodes.get(exits.get(1));
    }

    /**
     * Create the path from the start to the finish
     * @return the path;
     */
    public ArrayList<Node> getPath() {
        ArrayList<Node> path = new ArrayList<>();
        Node currentNode = exit;

        while (currentNode != null) {
            path.add(currentNode);
            currentNode = currentNode.getParent();
        }

        return path;
    }

    /**
     * Find the return the neighbours of a specific node
     * @param parent the node to find neighbours of.
     * @return a hashset of all the neighbours
     */
    public void findNeighbours(Node parent) {
        processor.scanAll(parent);
    }
}
