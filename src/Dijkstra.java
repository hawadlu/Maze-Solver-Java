//Todo make an abstract class for all the common methods?
import java.util.*;

/**
 * This class implements a Dijkstra type search
 */

class Dijkstra extends Algorithms{

    /**
     * Constructor
     */
    public Dijkstra() {}

    /**
     * Solves the maze node.
     * Uses iteration to avoid stack overflow error
     */
    public void solve(MazeNode start, MazeNode destination, HashMap<Coordinates, MazeNode> nodes) {
        MazeNode parent = null;
        PriorityQueue<MazeNode> toProcess = setupQueueWithCost(start);


        while (!toProcess.isEmpty()) {
            parent = toProcess.peek();
            parent.visit();

            if (parent.equals(destination)) {
                break;
            } else {

                //Add the children
                for (Coordinates location : Objects.requireNonNull(toProcess.poll()).getNeighbours()) {
                    //Get the node
                    MazeNode node = nodes.get(new Coordinates(location.x, location.y));
                    compareCost(parent, toProcess, node);
                }

            }
        }

        //trace the path back to the start
        backtrack(parent);
    }
}
