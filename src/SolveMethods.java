import java.awt.image.BufferedImage;
import java.util.HashMap;

/**
 * Class containing methods for invoking different solve algorithms
 */
public class SolveMethods {
    /**
     * Solves the maze depth first
     */
    public static ImageFile solveDFS(ImageFile imgObj, MazeNode start, MazeNode destination, HashMap<Coordinates, MazeNode> nodes) {
        //Create a DFS object
        DFS dfs = new DFS();
        dfs.solve(imgObj, start, destination, nodes);

        System.out.println("Maze solved. Nodes in path: " + dfs.getPathSize());
        System.out.println("Drawing image");

        //Draw
        return ImageManipulation.drawImage(imgObj, dfs.getPath(), start);
    }

    /**
     * Solves the maze breadth first
     */
    public static ImageFile solveBFS(ImageFile imgObj, MazeNode start, MazeNode destination, HashMap<Coordinates, MazeNode> nodes) {

        //Create a BFS object
        BFS bfs = new BFS();
        bfs.solve(imgObj, start, destination, nodes);

        System.out.println("Maze solved. Nodes in path: " + bfs.getPathSize());
        System.out.println("Drawing image");

        //Draw
        return ImageManipulation.drawImage(imgObj, bfs.getPath(), start);
    }

    /**
     * Solves the maze using the Dijkstra algorithm
     */
    public static ImageFile solveDijkstra(ImageFile imgObj, MazeNode start, MazeNode destination, HashMap<Coordinates, MazeNode> nodes) {
        //Create a DFS object
        Dijkstra dijkstra = new Dijkstra();
        dijkstra.solve(imgObj, start, destination, nodes);

        System.out.println("Maze solved. Nodes in path: " + dijkstra.getPathSize());
        System.out.println("Drawing image");


        //Draw
        return ImageManipulation.drawImage(imgObj, dijkstra.getPath(), start);
    }

    /**
     * Solves the maze using the AStar algorithm
     */
    public static ImageFile solveAStar(ImageFile imgObj, MazeNode start, MazeNode destination, HashMap<Coordinates, MazeNode> nodes) {
        //Create an AStar object
        AStar aStar = new AStar();
        aStar.solve(imgObj, start, destination, nodes);

        //todo delete in the other methods?
        nodes.clear();

        System.out.println("Maze solved. Nodes in path: " + aStar.getPathSize());
        System.out.println("Drawing image");

        //Draw
        return ImageManipulation.drawImage(imgObj, aStar.getPath(), start);
    }

}
