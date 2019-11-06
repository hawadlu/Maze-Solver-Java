import java.util.*;

/**
 * This class solves the maze depth first.
 * It takes a start node, end node and and returns a path between them
 */
public class DFS {
    int pathSize = 0;
    ArrayList<MazeNode> path = new ArrayList<>(); //The path
    Set<MazeNode> visited = new HashSet<>(); //Set of visited nodes

    //Start and end nodes
    MazeNode start, end;

    /**
     * Constructor
     */
    public DFS(MazeNode start, MazeNode end) {
        start = start;
        end = end;
    }

    /**
     * Solves the maze node
     */
     public ArrayList<MazeNode> solve() {
         Queue<MazeNode> toDo = new ArrayDeque<>(); //The nodes to be processed
         toDo.offer(start);
        return null;
    }
}
