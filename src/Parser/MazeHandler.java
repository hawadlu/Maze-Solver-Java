package Parser;

import Application.Application;
import Game.Player;
import Utility.Location;
import Utility.Node;
import Utility.PathMaker;

import java.util.HashSet;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * This class contains variables and methods used by the parser during execution.
 */
public class MazeHandler {
  Node lastNode;
  Application application;
  Thread currentThread = new Thread();
  Location start, destination;
  Player player;
  int delay;
  public boolean done = false;

  public MazeHandler(Application application) {
    this.application = application;
    application.scanEntireMaze();
    this.start = application.getMazeExits().get(0);
    this.destination = application.getMazeExits().get(1);
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
   * @param toUpdate
   * @return
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
   * @return
   */
  public Object setParent(Node childNode, Node parentNode) {
    childNode.setParent(parentNode);
    return null;
  }

  /**
   * Tell the maze handler that this node is done
   * @param lastNode the destination node
   */
  public void reportDone(Node lastNode) {
    System.out.println(player + " Reported done on node: " + lastNode);
    this.done = true;
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
   * Set the delay between execution steps.
   * @param delay the delay.
   */
  public void setDelay(int delay) {
    this.delay = delay;
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

  /**
   * @return Get the player object.
   */
  public Player getPlayer() {
    return this.player;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    MazeHandler that = (MazeHandler) o;
    return delay == that.delay && lastNode.equals(that.lastNode) && application.equals(that.application) && currentThread.equals(that.currentThread) && start.equals(that.start) && destination.equals(that.destination) && player.equals(that.player);
  }

  @Override
  public int hashCode() {
    return Objects.hash(lastNode, application, currentThread, start, destination, player, delay);
  }
}
