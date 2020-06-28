import java.util.*;

/**
 * This class solves the maze breadth first.
 * It takes a start node, end node and and returns a path between them
 */
class BFS extends Algorithms{
    /**
     * Constructor
     */
    public BFS() {}

    /**
     * Solves the maze node.
     * Uses iteration to avoid stack overflow error
     */
    public void solve(MazeNode start, MazeNode destination, HashMap<Coordinates, MazeNode> nodes) {
        MazeNode parent = null;
        Queue<MazeNode> toProcess = new ArrayDeque<>();
        start.visit();
        toProcess.offer(start);


        while (!toProcess.isEmpty()) {
            parent = toProcess.peek();
            parent.visit(); //set visited
            if (parent.equals(destination)) {
                break;
            } else {

                //Add all children
                for (Coordinates location: Objects.requireNonNull(toProcess.poll()).getNeighbours()) {
                    //Get the node

                    MazeNode node = nodes.get(new Coordinates(location.x, location.y));
                    if (!node.isVisited()) {
                        node.setParent(parent);
                        toProcess.offer(node);
                    }
                }
            }
        }

        //retrace from the parent to the start
        backtrack(parent);
    }
}
