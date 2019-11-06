import java.util.*;

/**
 * This class solves the maze depth first.
 * It takes a start node, end node and and returns a path between them
 */
public class DFS {
    int pathSize = 0;
    ArrayList<MazeNode> path = new ArrayList<>(); //The path
    Set<MazeNode> visited = new HashSet<>(); //Set of visited nodes

    /**
     * Constructor
     */
    public DFS() {
    }

    /**
     * Solves the maze node.
     * Uses iteration to avoid stack overflow error
     */
     public void solve(MazeNode start, MazeNode destination) {
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
                 for (MazeNode node : toProcess.pop().getNeighbours()) {
                     if (!node.isVisted()) {
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
