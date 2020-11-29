package Utility;

import Algorithm.SolveWorker;

import java.util.HashSet;

/**
 * Class used to represent nodes in the maze
 */
public class Node {
    Location nodeLocation;
    HashSet<Node> neighbours = new HashSet<>();
    Node parent;
    SolveWorker visitor = null;

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
    public SolveWorker isVisited() {
        return this.visitor;
    }

    /**
     * Visit this node
     * @param visitor the thread that visited this.
     */
    public void visit(SolveWorker visitor) {
        this.visitor = visitor;
    }

    @Override
    public String toString() {
        return "Location: " + getLocation() + " Neighbours: " + getNeighbours().size() + " visited: " + visitor;
    }
}
