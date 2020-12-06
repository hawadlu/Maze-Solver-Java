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
     * @param closest boolean to indicate if the collection should be sorted
     * @return a collection of all the neighbours
     */
    public Collection<Node> getNeighbours(Boolean closest) {
        if (closest) {
            ArrayList<Node> neighbours = new ArrayList<>(this.neighbours);
            neighbours.sort(Comparator.comparingInt(this::getDistance));
            return neighbours;
        }

        //return the set as it is
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
        //Return the distance on the x plane
        if (other.getLocation().x == this.getLocation().x) return Math.abs(other.getLocation().x - this.getLocation().x);

        //return the distance on the y plane
        return Math.abs(other.getLocation().y - this.getLocation().y);
    }

    @Override
    public String toString() {
        return "Location: " + getLocation() + " Neighbours: " + getNeighbours(false).size() + " visited: " + isVisited;
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
