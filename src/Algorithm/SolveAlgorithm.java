package Algorithm;

import Algorithm.ArticulationPoint.ArticulationPoints;
import Algorithm.MinimumTree.Kruskals;
import Algorithm.MinimumTree.Prims;
import Algorithm.Solvers.AStar;
import Algorithm.Solvers.BreadthFirst;
import Algorithm.Solvers.DepthFirst;
import Algorithm.Solvers.Dijkstra;
import Application.Application;
import Utility.Exceptions.InvalidMaze;
import Utility.Location;
import Utility.Node;
import Utility.Segment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/**
 * This class contains common methods that are used by all algorithms
 */
public class SolveAlgorithm {
    public boolean scanAll = false;
    public Node entry;
    public Node exit;
    final Application application;
    public final double mazeSize;
    Node[] join = null;
    public long execTime;
    public ArrayList<Segment> segments = new ArrayList<>();
    public ArrayList<Node> artPts = new ArrayList<>();

    /**
     * Start the solve process.
     * @param currentApplication  the current application.
     */
    public SolveAlgorithm(Application currentApplication) {
        this.application = currentApplication;
        this.mazeSize = application.getMazeDimensions().width * application.getMazeDimensions().height;
    }

    public void Solve(String algorithm, Boolean multiThreading) {
        boolean buildNodePath = true;
        long startTime = System.nanoTime();

        //Check is the algorithm will be compatible with the maze size
        if ((algorithm.equals("Breadth First") || algorithm.equals("Depth First")) && mazeSize > Math.pow(6001, 2)) {
            System.out.println("Maze is too large for " + algorithm + ". Using Astar instead");
            algorithm = "AStar";
        } else if (algorithm.equals("Dijkstra") && mazeSize > Math.pow(8001, 2)) {
            System.out.println("Maze is too large for " + algorithm + ". Using Astar instead");
            algorithm = "AStar";
        }

        if (algorithm.equals("Depth First")) new DepthFirst().solve(this, multiThreading);
        else if (algorithm.equals("Breadth First")) new BreadthFirst().solve(this, multiThreading);
        else if (algorithm.equals("Dijkstra")) new Dijkstra().solve(this, multiThreading);
        else if (algorithm.equals("AStar")) new AStar().solve(this, multiThreading);
        else if (algorithm.equals("Prims")) {
            buildNodePath = false;
            Prims prims = new Prims();
            prims.solve(this);
            segments = prims.getSegments();
        } else if (algorithm.equals("Kruskals")) {
            buildNodePath = false;
            Kruskals kruskals = new Kruskals();
            kruskals.solve(this);
            segments = kruskals.getSegments();
        } else if (algorithm.equals("Articulation")) {
            buildNodePath = false;
            ArticulationPoints articulation = new ArticulationPoints(application.getNodes());
            articulation.solve();
            artPts = articulation.getArticulationPoints();
        }

        long stopTime = System.nanoTime();
        execTime = stopTime - startTime;
        System.out.println("Execution time: " + execTime + "ns");

        //Build the path if the path can be traced from node to node
        if (buildNodePath) makePath();
        else if (!segments.isEmpty()) makePath(segments);
        else if (!artPts.isEmpty()) makeNodePath(artPts);
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

    public void scan(String param) {
        if (param.equals("Loading") && mazeSize > Math.pow(6001, 2)) {
            System.out.println("Maze is too large to scan for all nodes. Scanning on solve.");
            param = "Solving";
        }

        if (param.equals("Loading")) {
            application.scanEntireMaze();
            scanAll = true;
        } else {
            application.findMazeExits();
        }

        ArrayList<Location> exits = application.getMazeExits();

        //make sure there is at least one entry and exit
        if (exits.size() < 2) try {
            throw new InvalidMaze("Not enough exits");
        } catch (InvalidMaze invalidMaze) {
            invalidMaze.printStackTrace();
        }

        entry = application.getNodes().get(exits.get(0));
        exit = application.getNodes().get(exits.get(1));
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
        Node currentNode;

        if (join == null) {
            if (exit.getParent() != null) currentNode = exit;
            else currentNode = entry;

            application.getImageFile().fillNodePath(generatePathArraylist(currentNode), true);
        } else {
            application.getImageFile().fillNodePath(generatePathArraylist(join[0]), true);
            application.getImageFile().fillNodePath(generatePathArraylist(join[1]), true);
            ArrayList<Node> tmp = new ArrayList<>();
            tmp.add(join[0]);
            tmp.add(join[1]);
            application.getImageFile().fillNodePath(tmp, true);
        }
    }

    /**
     * Fill the image with the necessary segments
     * @param segments the arraylist of segments
     */
    private void makePath(ArrayList<Segment> segments) {
        application.getImageFile().fillSegmentPath(segments);
    }

    /**
     * Fill in the articulation points on the image
     * @param artPts
     */
    private void makeNodePath(ArrayList<Node> artPts) {
        application.getImageFile().fillNodePath(artPts, false);
    }

    /**
     * Populate the set array of segments using the nodes
     */
    public void makeSegments() {
        for (Node node: application.getNodes().values()) {
            for (Node neighbour: node.getNeighbours()) {
                Segment newSegment = new Segment(node, neighbour);
                if (!segments.contains(newSegment)) segments.add(newSegment);
            }
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
        application.scanPart(parent, multiThreading);
    }

    /**
     * Set the cost of all known nodes to zero
     */
    public void resetCost() {
        for (Node node: application.getNodes().values()) node.setCost(0);
    }

    /**
     * Get all of the nodes stored in the map
     * @return a collection of nodes
     */
    public Collection<Node> getNodes() {
        return application.getNodes().values();
    }

    /**
     * @return the map of nodes
     */
    public Map<Location, Node> getNodeMap() {
        return application.getNodes();
    }
}
