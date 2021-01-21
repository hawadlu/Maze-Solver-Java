package parser;


import Application.Application;
import Game.Player;
import Utility.Thread.AlgorithmDispatcher;
import parser.nodes.variables.VariableNode;
import Utility.Location;
import Utility.Node;
import Utility.PathMaker;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * This class contains variables and methods used by the parser during execution.
 * It provides an interface for interaction with methods implemented by the underlying program.
 * It also hosts the map of variables used during execution.
 */
public class Handler {
  Node lastNode;
  final Application application;
  final Thread currentThread = new AlgorithmDispatcher();
  final Location start;
  final Location destination;
  Player player;
  int delay;
  private final HashMap<String, VariableNode> variables = new HashMap<>();
  private boolean popup = false;

  public Handler(Application application, int delay) {
    this.application = application;
    if (this.application.getNodes().size() == 0) this.application.scanEntireMaze();
    this.start = this.application.getMazeExits().get(0);
    this.destination = this.application.getMazeExits().get(1);
    this.delay = delay;
  }

  public Handler(Application application) {
    this.application = application;
    if (this.application.getNodes().size() == 0) this.application.scanEntireMaze();
    this.start = this.application.getMazeExits().get(0);
    this.destination = this.application.getMazeExits().get(1);
  }

  /**
   * Set the player that is using this handler.
   * Used in the game mode.
   * @param player the player that is using this handler.
   */
  public void setPlayer(Player player) {
    this.player = player;
  }

  /**
   * @return the start node
   */
  public Node getStart() {
    return application.getNodes().get(start);
  }

  /**
   * Visit a node in the maze.
   * @param toVisit the node to visit
   */
  public void visit(Node toVisit) {
    toVisit.visit(currentThread);

    //Update the image if necessary
    if (player != null) {
      //Update the player
      player.update(toVisit);

      //Pause execution
      try {
        TimeUnit.MILLISECONDS.sleep(delay);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * Check if this node matches the destination.
   * @param toUpdate the node to check.
   * @return boolean indicating if this is done.
   */
  public boolean checkDone(Node toUpdate) {
    return toUpdate.getLocation().equals(destination);
  }

  /**
   * Return an arraylist of all the neighbours of this node
   * @param toUpdate the node to get the neighbours from.
   * @return a hashset containing all of the neighbours found
   */
  public HashSet<Node> getNeighbours(Node toUpdate) {
    return (HashSet<Node>) toUpdate.getNeighbours();
  }

  /**
   * Check if a node is visited.
   * @param toUpdate the node to check
   * @return boolean indicating visited
   */
  public boolean isVisited(Node toUpdate) {
    return toUpdate.isVisited() != null;
  }

  /**
   * Set the parent of the child node to the supplied parent.
   * @param childNode the child.
   * @param parentNode the parent
   */
  public void setParent(Node childNode, Node parentNode) {
    childNode.setParent(parentNode);
  }

  /**
   * Tell the maze handler that this node is done
   * @param lastNode the destination node
   */
  public void reportDone(Node lastNode) {
    System.out.println(player + " Reported done on node: " + lastNode);
    this.lastNode = lastNode;
    if (player != null) player.markDone();
    else application.getImageFile().fillNodePath(PathMaker.generatePathArraylist(lastNode), true);
  }

  /**
   * Get the finish node.
   * @return the last node.
   */
  public Node getLastNode() {
    return lastNode;
  }

  /**
   * Get the cost of a node.
   * @param toUpdate the node to get the cost for.
   * @return the cost.
   */
  public double getCost(Node toUpdate) {
    return toUpdate.getCost();
  }

  /**
   * Update the cost of a node.
   * @param toUpdate the node to update.
   * @param cost the new cost.
   */
  public void setCost(Node toUpdate, double cost) {
    toUpdate.setCost(cost);
  }

  /**
   * Return the distance between two nodes.
   * @param nodeOne the first node.
   * @param nodeTwo the second node.
   * @return the distance.
   */
  public double getDistance(Node nodeOne, Node nodeTwo) {
    return nodeOne.calculateDistance(nodeTwo);
  }

  /**
   * Get the distance from any node, to the destination.
   * @param node the node to get the distance from.
   * @return the distance.
   */
  public double getDistanceToDestination(Node node) {
    return node.calculateDistance(application.getNodes().get(destination));
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Handler that = (Handler) o;
    return delay == that.delay && lastNode.equals(that.lastNode) && application.equals(that.application) && currentThread.equals(that.currentThread) && start.equals(that.start) && destination.equals(that.destination) && player.equals(that.player);
  }

  @Override
  public int hashCode() {
    return Objects.hash(lastNode, application, currentThread, start, destination, player, delay);
  }

  /**
   * Get a variable out of the variable map.
   * Check if the variable exits, if not throw an error.
   * @param key the name of the variable.
   * @return the variable value.
   */
  public VariableNode getFromMap(String key) {
    if (!variables.containsKey(key)) {
      Parser.fail("Could not find variable '" + key + "'", "Execution", null, getPopup());
    }
    return variables.get(key);
  }

  /**
   * Check if a variable is stored in the map.
   * @param varName the name of the variable to look for.
   * @return a boolean indicating if the key is in the variable map.
   */
  public boolean hasVariable(String varName) {
    return variables.containsKey(varName);
  }

  /**
   * Add a variable to the map of variables.
   * @param name the name of the variable to add.
   * @param variableNode the variable to add.
   */
  public void addVariable(String name, VariableNode variableNode) {
    variables.put(name, variableNode);
  }

  /**
   * Remove a variable from the variable map.
   * Check if the key is contained in the map. If not throw an error.
   * @param key the key of the value to remove.
   */
  public void removeFromMap(String key) {
    if (!variables.containsKey(key)) Parser.fail("Could not find variable '" + key + "'", "Execution", null, getPopup());
    variables.remove(key);
  }

  public boolean getPopup() {
    return popup;
  }

  public void setPopup(boolean popup) {
    this.popup = popup;
  }
}
