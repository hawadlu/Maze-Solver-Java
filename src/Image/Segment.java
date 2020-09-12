package Image;

import MinimumSpanningTree.MSTNode;

/**
 * This class is responsible for drawing lines between the nodes.
 */
public class Segment {
    final MSTNode nodeStart;
    final MSTNode nodeEnd;
    final int cost;

    public Segment(MSTNode start, MSTNode end, int newCost) {
        this.nodeStart = start;
        this.nodeEnd = end;
        this.cost = newCost;
    }

    /**
     * Getters
     */
    public int getCost(){return this.cost;}
    public MSTNode getNodeStart(){return this.nodeStart;}
    public MSTNode getNodeEnd(){return this.nodeEnd;}
}
