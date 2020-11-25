package Utility;

import java.util.HashSet;

/**
 * Class used to represent nodes in the maze
 */
public class Node {
    Location nodeLocation;
    HashSet<Node> neighbours = new HashSet<>();
    Node parent;
    Boolean isVisited = false;

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
    public boolean isVisited() {
        return this.isVisited;
    }

    /**
     * Visit this node
     */
    public void visit() {
        this.isVisited = true;
    }
}
