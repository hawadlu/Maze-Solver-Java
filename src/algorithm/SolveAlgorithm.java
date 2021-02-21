package algorithm;

import algorithm.ArticulationPoint.ArticulationPoints;
import algorithm.MinimumTree.Kruskals;
import algorithm.MinimumTree.Prims;
import algorithm.Solvers.*;
import image.ImageFile;
import utility.*;
import utility.Exceptions.InvalidMaze;
import game.Player;

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
  public final double mazeSize;
  Node[] join = null;
  public long execTime;
  public ArrayList<Segment> segments = new ArrayList<>();
  public ArrayList<Node> artPts = new ArrayList<>();
  public Player player;
  public Logger logger;

  /**
   * Create a new SolveAlgorithm object.
   *
   * @param player the player that wants to do the solving.
   * @param logger the logger object (for debugging)
   */
  public SolveAlgorithm(Player player, Logger logger) {
    this.mazeSize = player.getImageFile().getDimensions().width * player.getImageFile().getDimensions().height;
    this.player = player;
    this.logger = logger;
  }

  /**
   * Process the parameters supplied by the player and start the appropriate algorithm.
   *  @param algorithm the algorithm that the player wants to use.
   * @param multiThreading boolean to indicate if the player wants to use multiple threads.
   * @return an imagefile containing the solved maze.
   */
  public ImageFile solve(String algorithm, boolean multiThreading) {
    logger.add(player.getName() + "entered solve method");

    boolean buildNodePath = true;
    long startTime = System.nanoTime();

    //Locate the entry and exit
    getExits();

    //Check is the algorithm will be compatible with the maze size
    if ((algorithm.equals("Breadth First") || algorithm.equals("Depth First")) && mazeSize > Math.pow(6001, 2)) {
      System.out.println("Maze is too large for " + algorithm + ". Using Astar instead");
      algorithm = "AStar";
    } else if (algorithm.equals("Dijkstra") && mazeSize > Math.pow(8001, 2)) {
      System.out.println("Maze is too large for " + algorithm + ". Using Astar instead");
      algorithm = "AStar";
    }

    if (algorithm.equals("Depth First")) new DepthFirst().solve(this, multiThreading, player.getName());
    else if (algorithm.equals("Breadth First")) new BreadthFirst().solve(this, multiThreading, player.getName());
    else if (algorithm.equals("Dijkstra")) new Dijkstra().solve(this, multiThreading, player.getName());
    else if (algorithm.equals("AStar")) new AStar().solve(this, multiThreading, player.getName());
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
      ArticulationPoints articulation = new ArticulationPoints(player);
      articulation.solve();
      artPts = articulation.getArticulationPoints();
    }

    long stopTime = System.nanoTime();
    execTime = stopTime - startTime;
    System.out.println("Execution time: " + execTime + "ns");

    logger.add(player.getName() + "completed solving");

    //Build the path if the path can be traced from node to node
    if (buildNodePath) {
      PathMaker.makePath(join, entry, exit, getImageFile());
    } else if (!segments.isEmpty()) {
      PathMaker.makePath(segments, getImageFile());
    } else if (!artPts.isEmpty()) {
      PathMaker.makeNodePath(artPts, getImageFile());
    }

    //Create the image file
    ImageFile solved = new ImageFile(player.getImageFile());
    solved.fillNodePath(PathMaker.generatePathArraylist(exit), true);

    return solved;
  }

  /**
   *
   */
  private void getExits() {
    ArrayList<Location> exits = player.getExits();

    //make sure there is at least one entry and exit
    if (exits.size() < 2) try {
      throw new InvalidMaze("Not enough exits");
    } catch (InvalidMaze invalidMaze) {
      invalidMaze.printStackTrace();
    }

    entry = player.getNodes().get(exits.get(0));
    exit = player.getNodes().get(exits.get(1));
  }

  /**
   * Takes the worker threads from the algorithm and begins execution.
   */
  public void startThreads(SolveWorker workerOne, SolveWorker workerTwo, Boolean multiThreading) {
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

  /**
   * Scan the maze.
   *
   * If param is "loading" scan everything, otherwise only find the exits.
   * @param param String specifying how the user wants to find nodes.
   */
  public void scan(String param) {
    if (param.equals("Loading") && mazeSize > Math.pow(6001, 2)) {
      System.out.println("Maze is too large to scan for all nodes. Scanning on solve.");
      param = "Solving";
    }

    if (param.equals("Loading")) {
      player.scanAll();
      scanAll = true;
    } else {
      player.findExits();
    }
  }

  /**
   * Used for multi threaded solves.
   *
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
   * Populate the set array of segments using the nodes
   */
  public void makeSegments() {
    for (Node node : player.getNodes().values()) {
      for (Node neighbour : node.getNeighbours()) {
        Segment newSegment = new Segment(node, neighbour);
        if (!segments.contains(newSegment)) segments.add(newSegment);
      }
    }
  }

  /**
   * Find the return the neighbours of a specific node
   * @param parent         the node to find neighbours of.
   * @param multiThreading boolean to indicate if the program is currently multithreading.
   */
  public void findNeighbours(Node parent, Boolean multiThreading) {
    player.scanPart(parent, multiThreading);
  }

  /**
   * Set the cost of all known nodes to zero
   */
  public void resetCost() {
    for (Node node : player.getNodes().values()) node.setCost(0);
  }

  /**
   * Get all of the nodes stored in the map
   *
   * @return a collection of nodes
   */
  public Collection<Node> getNodes() {
    return player.getNodes().values();
  }

  /**
   * @return the map of nodes
   */
  public Map<Location, Node> getNodeMap() {
    return player.getNodes();
  }

  /**
   * Update the player nodes
   *
   * @param parent the new node
   */
  public void updatePlayer(Node parent) {
    player.update(parent);
  }

  /**
   * Get the current image file from the player.
   * @return the imageFile.
   */
  public ImageFile getImageFile() {
    return player.getImageFile();
  }

  /**
   * Check if this is running in live mode.
   * @return a boolean to indicate if live or not.
   */
  public boolean isLive() {
    return player.isLive();
  }

  /**
   * Get the delay from the player,
   * @return an int representing the delay
   */
  public int getDelay() {
    return player.getDelay();
  }
}
