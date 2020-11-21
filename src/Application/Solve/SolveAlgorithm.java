package Application.Solve;

import Application.Application;
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
    HashMap<Location, Node> nodes;

    /**
     * Start the solve process.
     * @param algorithm the algorithm that should be used
     * @param params additional parameters.
     */
    public SolveAlgorithm(String algorithm, String params, Application currentApplication) {
        this.processor = new ImageProcessor(currentApplication);

        if (params.equals("During Loading")) {
            processor.scanMaze();
            scanAll = true;
        }
        if (algorithm.endsWith("Depth First")) new DepthFirst().Solve(this);
    }

    /**
     * Create the path from the start to the finish
     * @return the path;
     */
    public ArrayList<Location> getPath() {
        //todo implement me
        return null;
    }
}
