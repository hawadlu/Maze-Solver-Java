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
     * Solves the maze node
     */
     public boolean solve(MazeNode next, MazeNode destination) {
        if (next.equals(destination)) {
            path.add(next);
            return true;
        }

        //Going through the neighbours
         for (MazeNode nextNode: next.getNeighbours()) {
             if (!nextNode.isVisted()) {
                 nextNode.visit();

                 if (solve(nextNode, destination)) {
                     path.add(nextNode);
                     System.out.println("True");
                     return true;
                 }
             }
         }
         return false;
     }

    /**
     * Get the path
     */
    public ArrayList<MazeNode> getPath() {
        return path;
    }

}
