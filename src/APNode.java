import java.util.ArrayList;
import java.util.HashSet;

//todo look at combining this and the AP node class
public class APNode {
    public APNode firstNode = null;
    public double nodeDepth = Double.POSITIVE_INFINITY;
    public APNode parent = null;
    public Coordinates location;
    public double reachback = -1;
    HashSet<APNode> neighbours = new HashSet<>();
    int numSubtrees;
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

    public HashSet<APNode> getNeighbours() {
        return neighbours;
    }

    public APNode getParent() {
        return parent;
    }
}