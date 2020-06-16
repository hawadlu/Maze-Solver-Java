import java.util.*;

/**
 * This class solves the maze depth first.
 * It ttakes a start node, end node and and returns a path between them
 */
class DFS {
    private int pathSize = 0;
    private final ArrayList<MazeNode> path = new ArrayList<>(); //The path

    /**
     * Constructor
     */
    public DFS() {
    }

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

        while (true) {
            if (parent != null) {
                path.add(parent);
                parent = parent.getParent();
                pathSize++;
            } else {
                break;
            }
        }
    }

    /**
     * Get the path
     */
    public ArrayList<MazeNode> getPath() {
        return path;
    }

    /**
     * Return the size of the path
     */
    public int getPathSize(){
        return pathSize;
    }

}
