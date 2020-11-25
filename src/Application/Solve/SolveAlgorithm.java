package Application.Solve;

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
    ArrayList<Location> exits = new ArrayList<>();
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
        this.processor.setSolve(this);
        if (params.equals("During Loading")) {
            nodes = processor.scanAll();
            scanAll = true;
        }

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
     * Add an exit location to the solver
     * @param exitLocation the location of the exit node
     */
    public void addExit(Location exitLocation) {
        exits.add(exitLocation);

        //Check if their are too many exits
        if (exits.size() > 2) try {
            throw new InvalidMaze("Too many exits");
        } catch (InvalidMaze invalidMaze) {
            invalidMaze.printStackTrace();
        }
    }

    /**
     * Create the path from the start to the finish
     * @return the path;
     */
    public ArrayList<Node> getPath() {
        ArrayList<Node> path = new ArrayList<>();
        Node currentNode = exit;

        while (currentNode.getParent() != null) {
            path.add(currentNode);
            currentNode = currentNode.getParent();
        }

        return path;
    }
}
