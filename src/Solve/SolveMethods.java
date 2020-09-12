package Solve;

import Image.ImageFile;
import Image.ImageManipulation;
import Location.Coordinates;
import customExceptions.SolveFailureException;

import javax.swing.*;
import java.util.HashMap;

/**
 * Class containing methods for invoking different solve algorithms
 */
public class SolveMethods {
    /**
     * Solves the maze depth first
     */
    public static ImageFile solveDFS(ImageFile imgObj, MazeNode start, MazeNode destination, HashMap<Coordinates, MazeNode> nodes, JPanel parentComponent) throws SolveFailureException {
        //Create a DFS object
        DepthFirstSearch depthFirstSearch = new DepthFirstSearch();
        depthFirstSearch.solve(imgObj, start, destination, nodes, parentComponent);

        System.out.println("Maze solved. Nodes in path: " + depthFirstSearch.getPathSize());
        System.out.println("Drawing image");

        //Draw
        return ImageManipulation.drawImage(imgObj, depthFirstSearch.getPath(), start, imgObj.getSegments(), imgObj.getArtPoints());
    }

    /**
     * Solves the maze breadth first
     */
    public static ImageFile solveBFS(ImageFile imgObj, MazeNode start, MazeNode destination, HashMap<Coordinates, MazeNode> nodes, JPanel parentComponent) throws SolveFailureException {

        //Create a BFS object
        BreadthFirstSearch breadthFirstSearch = new BreadthFirstSearch();
        breadthFirstSearch.solve(imgObj, start, destination, nodes, parentComponent);

        System.out.println("Maze solved. Nodes in path: " + breadthFirstSearch.getPathSize());
        System.out.println("Drawing image");

        //Draw
        return ImageManipulation.drawImage(imgObj, breadthFirstSearch.getPath(), start, imgObj.getSegments(), imgObj.getArtPoints());
    }

    /**
     * Solves the maze using the Dijkstra algorithm
     */
    public static ImageFile solveDijkstra(ImageFile imgObj, MazeNode start, MazeNode destination, HashMap<Coordinates, MazeNode> nodes, JPanel parentComponent) throws SolveFailureException {
        //Create a DFS object
        DijkstraSearch dijkstraSearch = new DijkstraSearch();
        dijkstraSearch.solve(imgObj, start, destination, nodes, parentComponent);

        System.out.println("Maze solved. Nodes in path: " + dijkstraSearch.getPathSize());
        System.out.println("Drawing image");


        //Draw
        return ImageManipulation.drawImage(imgObj, dijkstraSearch.getPath(), start, imgObj.getSegments(), imgObj.getArtPoints());
    }

    /**
     * Solves the maze using the AStar algorithm
     */
    public static ImageFile solveAStar(ImageFile imgObj, MazeNode start, MazeNode destination, HashMap<Coordinates, MazeNode> nodes, JPanel parentComponent) throws SolveFailureException {
        //Create an AStar object
        AStarSearch aStarSearch = new AStarSearch();
        aStarSearch.solve(imgObj, start, destination, nodes, parentComponent);

        System.out.println("Maze solved. Nodes in path: " + aStarSearch.getPathSize());
        System.out.println("Drawing image");

        //Draw
        return ImageManipulation.drawImage(imgObj, aStarSearch.getPath(), start, imgObj.getSegments(), imgObj.getArtPoints());
    }

}
