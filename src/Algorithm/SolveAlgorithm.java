package Algorithm;

import Application.Application;
import Utility.Exceptions.InvalidMaze;
import Utility.Image.ImageProcessor;
import Utility.Location;
import Utility.Node;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This class contains common methods that are used by all algorithms
 */
public class SolveAlgorithm {
    boolean scanAll = false;
    final ImageProcessor processor;
    ConcurrentHashMap<Location, Node> nodes = new ConcurrentHashMap<>();
    Node entry, exit;
    final Application application;
    public final double mazeSize;
    Node[] join = null;
    public long execTime;

    /**
     * Start the solve process.
     * @param currentApplication  the current application.
     */
    public SolveAlgorithm(Application currentApplication) {
        this.application = currentApplication;
        this.processor = new ImageProcessor(currentApplication);
        this.mazeSize = application.getMazeDimensions().width * application.getMazeDimensions().height;
    }

    public void Solve(String algorithm, Boolean multiThreading) {
        long startTime = System.nanoTime();

        if (algorithm.equals("Depth First")) new DepthFirst().solve(this, multiThreading);
        else if (algorithm.equals("Breadth First")) new BreadthFirst().solve(this, multiThreading);
        else if (algorithm.equals("Dijkstra")) new Dijkstra().solve(this, multiThreading);
//        else if (algorithm.equals("AStar")) new AStar().solve(this, multiThreading);

        long stopTime = System.nanoTime();
        execTime = stopTime - startTime;
        System.out.println("Execution time: " + execTime + "ns");

        //Build the path
        makePath();
    }

    /**
     * Takes the worker threads from the algorithm and begins execution.
     */
    public void startThreads(AlgorithmWorker workerOne, AlgorithmWorker workerTwo, Boolean multiThreading) {
        workerOne.other = workerTwo;
        workerTwo.other = workerOne;
        workerOne.start();

        //Only start the second worker in the case that the maze is large enough
        if (multiThreading) {
            workerTwo.start();
        }

        //Wait for the worker to finish
        try {
            workerOne.join();
            workerTwo.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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
     * Used for multi threaded solves.
     * @param nodeOne node one at meeting point
     * @param nodeTwo node two at meeting point
     */
    public void addJoinerNodes(Node nodeOne, Node nodeTwo) {
        if (join == null) {
            join = new Node[2];
            join[0] = nodeOne;
            join[1] = nodeTwo;
        }
    }

    /**
     * Create the path from the start to the finish
     */
    public void makePath() {
        //If there is no multithreading

        Node currentNode;

        if (join == null) {
            if (exit.getParent() != null) currentNode = exit;
            else currentNode = entry;

            application.getImageFile().fillPath(generatePathArraylist(currentNode));
        } else {
            application.getImageFile().fillPath(generatePathArraylist(join[0]));
            application.getImageFile().fillPath(generatePathArraylist(join[1]));
            ArrayList<Node> tmp = new ArrayList<>();
            tmp.add(join[0]);
            tmp.add(join[1]);
            application.getImageFile().fillPath(tmp);
        }
    }

    private ArrayList<Node> generatePathArraylist(Node currentNode) {
        ArrayList<Node> path = new ArrayList<>();

        while (currentNode != null) {
            path.add(currentNode);
            currentNode = currentNode.getParent();
        }
        return path;
    }

    /**
     * Find the return the neighbours of a specific node
     * @param parent the node to find neighbours of.
     * @param multiThreading boolean to indicate if the program is currently multithreading.
     */
    public void findNeighbours(Node parent, Boolean multiThreading) {
        processor.scanAll(parent, multiThreading);
    }
}
