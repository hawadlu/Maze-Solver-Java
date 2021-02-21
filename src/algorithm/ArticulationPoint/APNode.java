package algorithm.ArticulationPoint;

import utility.Node;

/**
 * This class is used to store nodes that are used in
 * the articulation points algorithm.
 */
public class APNode {
  public final Node firstNode;
  public final double depth;
  public final Node parent;

  /**
   * Create a new AP node.
   * @param firstNode the first node.
   * @param depth how far down the tree this node is.
   * @param parent the node that cam before this one
   */
  public APNode(Node firstNode, double depth, Node parent) {
    this.firstNode = firstNode;
    this.depth = depth;
    this.parent = parent;
  }
}
