import java.util.*;

/**
 * This class solves the maze breadth first.
 * It takes a start node, end node and and returns a path between them
 */
class BFS {
    private int pathSize = 0;
    private final ArrayList<MazeNode> path = new ArrayList<>(); //The path

    /**
     * Constructor
     */
    public BFS() {
    }

    /**
     * Solves the maze node.
     * Uses iteration to avoid stack overflow error
     */
    public void solve(MazeNode start, MazeNode destination) {
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
                for (MazeNode node : Objects.requireNonNull(toProcess.poll()).getNeighbours()) {
                    if (!node.isVisited()) {
                        node.setParent(parent);
                        toProcess.offer(node);
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
