import java.util.*;

/**
 * This class solves the maze depth first.
 * It takes a start node, end node and and returns a path between them
 */
class DFS extends Algorithms{
    /**
     * Constructor
     */
    public DFS() {}

    /**
     * Solves the maze node.
     * Uses iteration to avoid stack overflow error
     */
    public void solve(MazeNode start, MazeNode destination, HashMap<Coordinates, MazeNode> nodes) {
        MazeNode parent = null;
        Stack<MazeNode> toProcess = new Stack<>();
        start.visit();
        toProcess.push(start);

        while (!toProcess.isEmpty()) {
            parent = toProcess.peek();
            parent.visit(); //set visited
            if (parent.equals(destination)) {
                break;
            } else {

                //Add all children
                for (Coordinates location: Objects.requireNonNull(toProcess.pop()).getNeighbours()) {
                    //Get the node
                    MazeNode node = nodes.get(new Coordinates(location.x, location.y));
                    if (!node.isVisited()) {
                        node.setParent(parent);
                        toProcess.push(node);
                    }
                }
            }
        }

        //retrace from the parent to the start
        backtrack(parent);
    }
}
