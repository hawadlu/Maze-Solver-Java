package MinimumSpanningTree;

import Location.Coordinates;

public class MSTNode {
    final Coordinates location;
    MSTNode parent;
    int depth;

    /**
     * Create a new MinimumSpanningTree.MST node
     */
    public MSTNode(Coordinates location) {
        this.location = location;
        parent = this;
        depth = 0;
    }

    /**
     * Getters
     */
    public Coordinates getLocation() {return this.location;}
}
