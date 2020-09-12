package MinimumSpanningTree;

import Location.Coordinates;

public class MSTNode {
    public final Coordinates location;
    public MSTNode parent;
    public int depth;

    /**
     * Create a new MinimumSpanningTree.MST node
     */
    public MSTNode(Coordinates location) {
        this.location = location;
        parent = this;
        depth = 0;
    }

}
