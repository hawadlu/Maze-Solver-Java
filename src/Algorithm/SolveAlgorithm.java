package Algorithm;

import Application.Application;
import Utility.Exceptions.InvalidMaze;
import Utility.Image.ImageProcessor;
import Utility.Location;
import Utility.Node;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class contains common methdods that are used by all algorithms
 */
public class SolveAlgorithm {
    boolean scanAll = false;
    ImageProcessor processor;
    HashMap<Location, Node> nodes = new HashMap<>();
    Node entry, exit;
    Application application;
    double singleThreadSize = Math.pow(100, 2); //The size of maze that should be handles by a single thread
    double mazeSize;

    /**
     * Start the solve process.
     * @param currentApplication  the current application.
     */
    public SolveAlgorithm(Application currentApplication) {
        this.application = currentApplication;
        this.processor = new ImageProcessor(currentApplication);
        this.mazeSize = application.getMazeDimensions().width * application.getMazeDimensions().height;
    }

    public void Solve(String algorithm) {
        long startTime = System.nanoTime();

        //Check if the maze is large enough for two threads.
        Boolean twoThread = false;
        System.out.println("maze size: " + mazeSize);
        System.out.println("Single thread size: " + singleThreadSize);
        if (mazeSize > singleThreadSize) twoThread = true;

        if (algorithm.endsWith("Depth First")) new DepthFirst().solve(this, twoThread);
        long stopTime = System.nanoTime();
        System.out.println("Execution time: " + (stopTime - startTime) + "ns");
    }

    public void Scan(String params) {
        if (params.equals("Loading")) {
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
