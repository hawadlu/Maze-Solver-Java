package ArticulationPoints;

import Location.Coordinates;

import java.util.ArrayList;
import java.util.HashSet;

public class APNode {
    APNode firstNode = null;
    double nodeDepth = Double.POSITIVE_INFINITY;
    APNode parent = null;
    Coordinates location;
    double reachBack = -1;
    final HashSet<APNode> neighbours = new HashSet<>();
    ArrayList<APNode> children;


    public APNode(APNode firstNode, double nodeDepth, APNode parent) {
        this.firstNode = firstNode;
        this.nodeDepth = nodeDepth;
        this.parent = parent;
    }

    APNode(Coordinates location){
        this.location = location;
    }

    public void addNeighbour(APNode neighbour) {
        neighbours.add(neighbour);
    }


    /**
     * Getters
     */

    public HashSet<APNode> getNeighbours() {
        return neighbours;
    }

    public APNode getParent() {
        return parent;
    }

    public Coordinates getLocation() {return location;}
}