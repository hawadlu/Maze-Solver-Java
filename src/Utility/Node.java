package Utility;

import java.util.HashSet;
import java.util.Objects;

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
     * @return a set of all the neighbours
     */
    public HashSet<Node> getNeighbours() {
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
