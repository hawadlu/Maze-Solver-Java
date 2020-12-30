package Algorithm;

import Algorithm.ArticulationPoint.ArticulationPoints;
import Algorithm.MinimumTree.Kruskals;
import Algorithm.MinimumTree.Prims;
import Algorithm.Solvers.*;
import Application.Application;
import Utility.Exceptions.InvalidMaze;
import Utility.Location;
import Utility.Node;
import Utility.PathMaker;
import Utility.Segment;
import Game.Player;

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
  public int delay;
  public Player player; //may be null

  /**
   * Start the solve process.
   *
   * @param currentApplication the current application.
   */
  public SolveAlgorithm(Application currentApplication, int delay, Player player) {
    this.application = currentApplication;
    this.mazeSize = application.getMazeDimensions().width * application.getMazeDimensions().height;
    this.delay = delay;
    this.player = player;
  }

  public void solve(String algorithm, Boolean multiThreading) {
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
    if (buildNodePath) PathMaker.makePath(join, entry, exit, application);
    else if (!segments.isEmpty()) PathMaker.makePath(segments, application);
    else if (!artPts.isEmpty()) PathMaker.makeNodePath(artPts, application);
  }

  private void getExits() {
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
    for (Node node : application.getNodes().values()) {
      for (Node neighbour : node.getNeighbours()) {
        Segment newSegment = new Segment(node, neighbour);
        if (!segments.contains(newSegment)) segments.add(newSegment);
      }
    }
  }

  /**
   * Find the return the neighbours of a specific node
   *
   * @param parent         the node to find neighbours of.
   * @param multiThreading boolean to indicate if the program is currently multithreading.
   */
  public void findNeighbours(Node parent, Boolean multiThreading) {
    application.scanPart(parent, multiThreading);
  }

  /**
   * Set the cost of all known nodes to zero
   */
  public void resetCost() {
    for (Node node : application.getNodes().values()) node.setCost(0);
  }

  /**
   * Get all of the nodes stored in the map
   *
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

  /**
   * Update the player nodes
   *
   * @param parent the new node
   */
  public void updatePlayer(Node parent) {
    player.update(parent);
  }
}
