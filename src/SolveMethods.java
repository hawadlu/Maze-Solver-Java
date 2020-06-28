import java.util.HashMap;

/**
 * Class containing methods for invoking different solve algorithms
 */
public class SolveMethods {
    /**
     * Solves the maze depth first
     */
    public static void solveDFS(ImageFile imgObj, MazeNode start, MazeNode destination, String filePath, HashMap<Coordinates, MazeNode> nodes) {
        //Create a DFS object
        DFS dfs = new DFS();
        dfs.solve(start, destination, nodes);

        System.out.println("Maze solved. Nodes in path: " + dfs.getPathSize());
        System.out.println("Drawing image");

        //Draw
        ImageManipulation.drawImage(imgObj, dfs.getPath(), start, filePath, "DFS");
    }

    /**
     * Solves the maze breadth first
     */
    public static void solveBFS(ImageFile imgObj, MazeNode start, MazeNode destination, String filePath, HashMap<Coordinates, MazeNode> nodes) {

        //Create a BFS object
        BFS bfs = new BFS();
        bfs.solve(start, destination, nodes);

        System.out.println("Maze solved. Nodes in path: " + bfs.getPathSize());
        System.out.println("Drawing image");

        //Draw
        ImageManipulation.drawImage(imgObj, bfs.getPath(), start, filePath, "BFS");
    }

    /**
     * Solves the maze using the Dijkstra algorithm
     */
    public static void solveDijkstra(ImageFile imgObj, MazeNode start, MazeNode destination, String filePath, HashMap<Coordinates, MazeNode> nodes) {
        //Create a DFS object
        Dijkstra dijkstra = new Dijkstra();
        dijkstra.solve(start, destination, nodes);

        System.out.println("Maze solved. Nodes in path: " + dijkstra.getPathSize());
        System.out.println("Drawing image");


        //Draw
        ImageManipulation.drawImage(imgObj, dijkstra.getPath(), start, filePath, "Dijkstra");
    }

    /**
     * Solves the maze using the AStar algorithm
     */
    public static void solveAStar(ImageFile imgObj, MazeNode start, MazeNode destination, String filePath, HashMap<Coordinates, MazeNode> nodes) {
        //Create an AStar object
        AStar aStar = new AStar();
        aStar.solve(imgObj, start, destination, nodes);

        //todo delete in the other methods?
        nodes.clear();

        System.out.println("Maze solved. Nodes in path: " + aStar.getPathSize());
        System.out.println("Drawing image");

        //Draw
        ImageManipulation.drawImage(imgObj, aStar.getPath(), start, filePath, "AStar");
    }

}
