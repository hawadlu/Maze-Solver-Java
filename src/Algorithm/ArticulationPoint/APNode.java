package Algorithm.ArticulationPoint;

import Utility.Node;

/**
 * todo comment me
 */
public class APNode {
  public final Node firstNode;
  public final double depth;
  public final Node parent;

  /**
   * todo comment me
   * @param firstNode
   * @param depth
   * @param parent
   */
  public APNode(Node firstNode, double depth, Node parent) {
    this.firstNode = firstNode;
    this.depth = depth;
    this.parent = parent;
  }
}
