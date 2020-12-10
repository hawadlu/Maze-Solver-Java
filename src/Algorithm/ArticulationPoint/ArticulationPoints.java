package Algorithm.ArticulationPoint;

import Utility.Location;
import Utility.Node;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


public class ArticulationPoints {
  final ConcurrentHashMap<Location, Node> allNodes;
  final HashSet<Node> articulationPoints = new HashSet<>();
  final HashSet<Node> unvisited = new HashSet<>();

  /**
   * The Articulation points constructor
   * @param allNodes a map containing all of the nodes in the graph
   */
  public ArticulationPoints(ConcurrentHashMap<Location, Node> allNodes) {
    this.allNodes = allNodes;
  }

  /**
   * Get the articulation points
   * @return arraylist containing all of the articulation points.
   */
  public ArrayList<Node> getArticulationPoints() {
    return new ArrayList<>(articulationPoints);
  }

  /**
   * Begins the search for the articulation points
   * @return boolean indicating a failed/successful search
   */
  public boolean solve() {
    System.out.println("Looking for aps");

    unvisited.addAll(allNodes.values());
    while (!unvisited.isEmpty()) {

      //Cast the map of nodes to an arraylist then pick a random node
      Node parent = new ArrayList<>(unvisited).get(new Random().nextInt(unvisited.size()));

      //If this node has only one neighbour mark is as visited and ignore
      parent.setNodeDepth(0);
      parent.setSubTrees(0);

      unvisited.remove(parent);
      int subtrees = 0;
      //Perform the search
      for (Node child : parent.getNeighbours()) {
        unvisited.remove(child);
        if (child.getNodeDepth() == Double.POSITIVE_INFINITY) { //Is the neighbour visited

          iterativeSearch(child, 1, parent);
          subtrees++;
        }

        if (subtrees > 1) articulationPoints.add(parent);
      }
    }

    System.out.println("AP search complete. Found " + articulationPoints.size() + " articulation points");

    //Return a successful search
    return true;
  }

  /**
   * Look for articulation points using a iterative algorithm
   * @param firstNode The first node to look at
   * @param depth The current depth
   * @param root essentially the node that came before this one.
   */
  public void iterativeSearch(Node firstNode, double depth, Node root) {
    Stack<APNode> toProcess = new Stack<>();
    //Put the first element on the stack
    toProcess.push(new APNode(firstNode, depth, root));

    while (!toProcess.isEmpty()) {
      APNode currentAPNode = toProcess.peek(); //The node currently being process

      //Variable declarations for the sake of simplicity
      Node currentNode = currentAPNode.firstNode;
      Node parentNode = currentAPNode.parent;

      unvisited.remove(currentNode);

      if (currentNode.getNodeDepth() == Double.POSITIVE_INFINITY) {
        //The first time visiting this node

        currentNode.setNodeDepth(currentAPNode.depth);
        currentNode.setReachBack(currentAPNode.depth);

        //Get all the neighbours except for the parent
        currentNode.setChildren(new ArrayList<>(currentNode.getNeighbours()));
      } else if (!currentNode.getChildren().isEmpty()) {
        Node child = currentNode.getChildren().remove(0);

        if (child.getNodeDepth() < Double.POSITIVE_INFINITY) {
          //Direct alternative path
          currentNode.setReachBack(Math.min(child.getNodeDepth(), currentNode.getReachBack()));
        } else {
          //Add the child to the stack because it is unvisited
          toProcess.push(new APNode(child, currentAPNode.depth + 1, currentNode));
        }
      } else {
        if (currentNode != firstNode) {
          parentNode.setReachBack(Math.min(currentNode.getReachBack(), parentNode.getReachBack()));

          //Add this node to the articulation points if it matches the requirements
          if (currentNode.getReachBack() >= parentNode.getNodeDepth()) {
            articulationPoints.add(parentNode);
          }
        }
        toProcess.pop();
      }
    }
  }
}
