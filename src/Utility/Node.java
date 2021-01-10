package Utility;

import java.util.*;

/**
 * Class used to represent nodes in the maze
 */
public class Node {
  private Location nodeLocation;
  private HashSet<Node> neighbours = new HashSet<>();
  private Node parent;
  private Thread isVisited = null;
  private double cost = Double.POSITIVE_INFINITY; //Field used in Dijkstra, AStar and Kruskals

  //fields used in articulation points
  private double nodeDepth = Double.POSITIVE_INFINITY;
  private double reachBack = 0;
  private ArrayList<Node> children = new ArrayList<>();

  public Node(Location nodeLocation) {
    this.nodeLocation = nodeLocation;
  }

  public Node(Node node) {
    this.nodeLocation = node.getLocation();
    this.neighbours = (HashSet<Node>) node.getNeighbours();
    this.parent = node.getParent();
    this.isVisited = node.isVisited;
    this.cost = node.getCost();
  }

  /**
   * Estimate the size of this object.
   * Used primarily for testing.
   * @return the size estimate (bytes).
   */
  public double estimateSize() {
    double toReturn = 0;

    //8 bytes to a pointer
    int pointerSize = 8;

    if (nodeLocation != null) toReturn += nodeLocation.estimateSize();

    //The neighbours are stored a pointers (8 bytes per pointer)
    if (neighbours != null) toReturn += neighbours.size() * pointerSize;

    //The parent is also a pointer
    if (parent != null) toReturn += pointerSize;

    //The thread is also a pointer
    if (isVisited != null) toReturn += pointerSize;

    //The cost, a double
    toReturn += 8;

    //nodeDepth, a double
    toReturn += 8;

    //reachBack, a double
    toReturn += 8;

    //The children are stored a pointers (8 bytes per pointer)
    if (children != null) toReturn += children.size() * pointerSize;

    return toReturn;
  }

  /**
   * Get the location of the node.
   *
   * @return the location
   */
  public Location getLocation() {
    return this.nodeLocation;
  }

  /**
   * Make these into neighbours
   *
   * @param neighbour the neighbour
   */
  public void addNeighbour(Node neighbour) {
    neighbours.add(neighbour);
  }

  /**
   * @param parent the parent of this node
   */
  public void setParent(Node parent) {
    this.parent = parent;
  }

  /**
   * @return the parent of this node.
   */
  public Node getParent() {
    return this.parent;
  }

  /**
   * Get all of the neighbours
   *
   * @return a collection of all the neighbours
   */
  public Collection<Node> getNeighbours() {
    return this.neighbours;
  }

  /**
   * @return has this node been visited.
   */
  public Thread isVisited() {
    return this.isVisited;
  }

  /**
   * Visit this node
   */
  public void visit(Thread visitor) {
    this.isVisited = visitor;
  }

  /**
   * @param nextNode the node to calculate distance to
   * @return the distance from another node to this one.
   */
  public double calculateDistance(Node nextNode) {
    double toReturn = 0;
    //Return the distance on the x plane
    if (nodeLocation.x == nextNode.getLocation().x) {
      toReturn = calculateXDistance(nextNode);
    } else {
      //return the distance on the y plane
      toReturn = calculateYDistance(nextNode);
    }
    if (toReturn < 0) toReturn *= -1;
    return toReturn;
  }

  /**
   * Calculate the distance between two nodes on the x plane
   *
   * @param nextNode the node to calculate distance to
   * @return the distance
   */
  private double calculateXDistance(Node nextNode) {
    return nextNode.getLocation().x - nodeLocation.x;
  }

  /**
   * Calculate the distance between two nodes on the x plane
   *
   * @param nextNode the node to calculate distance to
   * @return the distance
   */
  private double calculateYDistance(Node nextNode) {
    return nextNode.getLocation().y - nodeLocation.y;
  }

  /**
   * Calculate the distance between two nodes that are not necessary on the same x, y plane
   *
   * @param start       the node to start at
   * @param destination the node to calculate the distance to
   * @return the distance
   */
  public static double calculateEuclideanDistance(Node start, Node destination) {
    double horizontal = Math.pow(Math.abs(start.getLocation().x - destination.getLocation().x), 2);
    double vertical = Math.pow(Math.abs(start.getLocation().y - destination.getLocation().y), 2);

    return Math.sqrt(horizontal + vertical);
  }

  /**
   * @return the cost of this node
   */
  public double getCost() {
    return cost;
  }

  /**
   * Set the cost of this node.
   *
   * @param newCost the new cost
   */
  public void setCost(double newCost) {
    this.cost = newCost;
  }

  /**
   * Get comparator used in priority queues in solving
   *
   * @return a comparator
   */
  public static Comparator<Node> getComparator() {
    return new Comparator<Node>() {
      @Override
      public int compare(Node nodeOne, Node nodeTwo) {
        return Double.compare(nodeOne.cost, nodeTwo.cost);
      }
    };
  }

  /**
   * @return a list of segments based on the neighbours
   */
  public ArrayList<Segment> getSegments(Map<Location, Node> nodeMap) {
    ArrayList<Segment> toReturn = new ArrayList<>();
    for (Node neighbour : getNeighbours()) {
      if (neighbour.isVisited == null) toReturn.add(new Segment(this, neighbour));
    }
    return toReturn;
  }

  /**
   * @param depth new node depth
   */
  public void setNodeDepth(double depth) {
    this.nodeDepth = depth;
  }

  /**
   * @return node depth
   */
  public double getNodeDepth() {
    return this.nodeDepth;
  }


  /**
   * @return the reachBack
   */
  public double getReachBack() {
    return reachBack;
  }

  /**
   * @param reachBack the new reachBack
   */
  public void setReachBack(double reachBack) {
    this.reachBack = reachBack;
  }

  /**
   * @return get the children of this node
   */
  public ArrayList<Node> getChildren() {
    return children;
  }

  /**
   * @param children the new children of this node
   */
  public void setChildren(ArrayList<Node> children) {
    this.children = children;
  }

  @Override
  public String toString() {
    return "Location: " + getLocation() + " Neighbours: " + getNeighbours().size() + " visited: " + isVisited + " Cost: " + cost;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Node node = (Node) o;
    return Objects.equals(nodeLocation, node.nodeLocation);
  }

  @Override
  public int hashCode() {
    return Objects.hash(nodeLocation);
  }
}
