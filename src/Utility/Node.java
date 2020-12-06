package Utility;

import java.util.*;

/**
 * Class used to represent nodes in the maze
 */
public class Node {
    Location nodeLocation;
    HashSet<Node> neighbours = new HashSet<>();
    Node parent;
    Thread isVisited = null;
    double cost = Double.POSITIVE_INFINITY; //Field used in Dijkstra and AStar

    public Node(Location nodeLocation) {
        this.nodeLocation = nodeLocation;
    }

    /**
     * Get the location of the node.
     * @return the location
     */
    public Location getLocation() {
        return this.nodeLocation;
    }

    /**
     * Make these into neighbours
     * @param node the neighbour
     */
    public void addNeighbour(Node node) {
        neighbours.add(node);
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
     * @return the distance from another node to this one.
     */
    public int getDistance(Node other) {
        int toReturn = 0;

        //Return the distance on the x plane
        if (other.getLocation().x == this.getLocation().x) {
            toReturn = other.getLocation().x - nodeLocation.x;
        } else {
            //return the distance on the y plane
            toReturn = other.getLocation().y - nodeLocation.y;
        }
        if (toReturn < 0) toReturn *= -1;
        return toReturn;
    }

    /**
     * @return the cost of this node
     */
    public double getCost() {
        return cost;
    }

    /**
     * Set the cost of this node.
     * @param newCost the new cost
     */
    public void setCost(double newCost) {
        this.cost = newCost;
    }

    /**
     * Get comparator used in priority queues in solving
     * @param algorithm the algorithm that is being used
     * @return a comparator that is tailored to the algorithm
     */
    public static Comparator<Node> getComparator(String algorithm) {
        Comparator<Node> compare = null;

        if (algorithm.equals("Dijkstra")) {
            compare = new Comparator<Node>() {
                @Override
                public int compare(Node nodeOne, Node nodeTwo) {
                    return Double.compare(nodeOne.cost, nodeTwo.cost);
                }
            };
        }

        //todo implement for Astar
        return compare;
    }

    @Override
    public String toString() {
        return "Location: " + getLocation() + " Neighbours: " + getNeighbours().size() + " visited: " + isVisited;
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
