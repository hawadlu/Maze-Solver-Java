package Utility;

import java.util.*;

/**
 * Class used to represent nodes in the maze
 */
public class Node {
    private Location nodeLocation;
    private HashSet<Location> neighbours = new HashSet<>();
    private Node parent;
    private Thread isVisited = null;
    private double cost = Double.POSITIVE_INFINITY; //Field used in Dijkstra and AStar

    public Node(Location nodeLocation) {
        this.nodeLocation = nodeLocation;
    }

    public Node(Node node) {
        this.nodeLocation = node.getLocation();
        this.neighbours = (HashSet<Location>) node.getNeighbours();
        this.parent = node.getParent();
        this.isVisited = node.isVisited;
        this.cost = node.getCost();
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
     * @param neighbourLocation the neighbour
     */
    public void addNeighbour(Location neighbourLocation) {
        neighbours.add(neighbourLocation);
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
    public Collection<Location> getNeighbours() {
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
    public double calculateCost(Node nextNode) {
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
     * @param nextNode the node to calculate distance to
     * @return the distance
     */
    private double calculateXDistance(Node nextNode) {
        return nextNode.getLocation().x - nodeLocation.x;
    }

    /**
     * Calculate the distance between two nodes on the x plane
     * @param nextNode the node to calculate distance to
     * @return the distance
     */
    private double calculateYDistance(Node nextNode) {
        return nextNode.getLocation().y - nodeLocation.y;
    }

    /**
     * Calculate the cost of moving between two nodes.
     * Factoring both the distance from this node to the next
     * and the next node to the final destination.
     * @param nextNode the node to move to.
     * @param finalDestination the final node.
     * @return the calculated cost
     */
    public double calculateCost(Node nextNode, Node finalDestination) {
        double toReturn = 0;
        //Return the distance on the x plane
        if (nodeLocation.x == nextNode.getLocation().x) {
            toReturn = calculateXDistance(nextNode) + calculateEuclideanDistance(nextNode, finalDestination);
        } else {
            //return the distance on the y plane
            toReturn = calculateYDistance(nextNode);
        }
        if (toReturn < 0) toReturn *= -1;
        return toReturn;
    }

    /**
     * Calculate the distance between two nodes that are not necessary on the same x, y plane
     * @param start the node to start at
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
     * @param newCost the new cost
     */
    public void setCost(double newCost) {
        this.cost = newCost;
    }

    /**
     * Get comparator used in priority queues in solving
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
       for (Location neighbourLocation: getNeighbours()) {
           Node neighbour = nodeMap.get(neighbourLocation);
           if (neighbour.isVisited == null) toReturn.add(new Segment(this, neighbour));
       }
       return toReturn;
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
