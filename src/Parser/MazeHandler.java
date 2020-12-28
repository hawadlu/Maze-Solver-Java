package Parser;

import Application.Application;
import Utility.Location;
import Utility.Node;

import java.util.HashSet;

/**
 * This class contains variables and methods used by the parser during execution.
 */
public class MazeHandler {
  Node lastNode;
  Application application;
  Thread currentThread = new Thread();
  Location start, destination;

  public MazeHandler(Application application) {
    this.application = application;
    application.scanEntireMaze();
    this.start = application.getMazeExits().get(0);
    this.destination = application.getMazeExits().get(1);
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
    System.out.println("Reported done on node: " + lastNode);
    this.lastNode = lastNode;
  }

  /**
   * Get the finish node.
   * @return the last node.
   */
  public Node getLastNode() {
    return lastNode;
  }
}
