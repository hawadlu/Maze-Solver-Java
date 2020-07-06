import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * This program loads a png image of a maze.
 * It will attempt to solve it and output a solved image file.
 */

class Solver {
    /**
     * Solves the maze
     */
    public static ImageFile solve(ImageFile image, Object algorithm, Object searchType, JPanel parentComponent) throws IOException, IllegalAccessException {
        long numNodes = 0;

        //Map containing the positions of each node
        HashMap<Coordinates, MazeNode> nodes = new HashMap<>();

        System.out.println("Finding nodes");

        //Validate the parameters
        ArrayList<Object> parameters = validateParameters(image, algorithm, searchType, parentComponent);
        algorithm = parameters.get(0);
        searchType = parameters.get(1);

        //Only load this if it is requested
        if (searchType.equals("Search for neighbours during loading")) {
            nodes = ImageManipulation.findNeighboursForAll(image);
            numNodes = nodes.size();
        }

        //If the nodes are to be initialised while solving add the start and end now
        if (!searchType.equals("Search for neighbours during loading")) {
            //Put the start and end in the map
            nodes.put(image.entry, new MazeNode(image.entry.x, image.entry.y));
            nodes.put(image.exit, new MazeNode(image.exit.x, image.exit.y));
        }

        System.out.println("Found all nodes");
        System.out.println("Start at: " + image.entry.x + ", " + 0);
        System.out.println("End at: " + image.exit.x + ", " + (image.getHeight() - 1));
        System.out.println("Node count: " + numNodes);
        System.out.println("Approximately " + (float) numNodes / (image.getHeight() * image.getWidth()) + "% of pixels are nodes. Assumed storage is: " + numNodes * 114 + " bytes");
        System.out.println("Finding neighbours");
        System.out.println("Solving");

        //Determine the method that should be used to solve the maze
        //todo implement automatic switching for larger mazes
        if (algorithm.equals("Depth First")) {
            return SolveMethods.solveDFS(image, nodes.get(image.entry), nodes.get(image.exit), nodes);
        } else if (algorithm.equals("Breadth First")) {
            return SolveMethods.solveBFS(image, nodes.get(image.entry), nodes.get(image.exit), nodes);
        } else if (algorithm.equals("Dijkstra")) {
            return SolveMethods.solveDijkstra(image, nodes.get(image.entry), nodes.get(image.exit), nodes);
        } else {
            return SolveMethods.solveAStar(image, nodes.get(image.entry), nodes.get(image.exit), nodes);
        }
    }

    /**
     * This method looks at the parameters that the user has entered and checks if they are vaible for solving the maze.
     *
     * @param imgObj     the image to solve
     * @param algorithm  the algorithm to use
     * @param searchType the method for finding neighbours
     * @return
     */
    private static ArrayList<Object> validateParameters(ImageFile imgObj, Object algorithm, Object searchType, JPanel parentComponent) {
        //Determine the correct algorithm
        double imgSize = imgObj.getHeight() * imgObj.getWidth();
        //Really big mazes have to use astar
        if (imgSize > 64000000 && !algorithm.equals("AStar")) {
            GUI.displayMessage(parentComponent, "This maze is too large for " + algorithm + ". Using AStar instead.");
            algorithm = "AStar";
        } else if (algorithm.equals("Breadth First") && imgSize > 16000000) {
            GUI.displayMessage(parentComponent, "This maze is too large for " + algorithm + ". Using AStar instead.");
            algorithm = "AStar";
        } else if (imgSize > 36000000 && algorithm.equals("Depth First")) {
            GUI.displayMessage(parentComponent, "This maze is too large for " + algorithm + ". Using AStar instead.");
            algorithm = "AStar";
        }

        //Determine the correct neighbour method
        if (imgSize > 36000000 && searchType.equals("Search for neighbours during loading")) {
            searchType = "Search for neighbours during solving";
            GUI.displayMessage(parentComponent, "Looking for neighbours during loading is too memory intensive.\n" +
                    " Looking for neighbours during solving instead.");
        }

        ArrayList<Object> toReturn = new ArrayList<>();
        toReturn.add(algorithm);
        toReturn.add(searchType);
        return toReturn;
    }

    /**
     * Generate maze
     * @param width maze width
     * @param height maze height
     * @param perfect should the maze be perfect?
     */
    public static BufferedImage generateMaze(String width, String height, boolean perfect) {
        return null;
    }

    /**
     * Gets user input and returns a string
     */
    private String getUserInput(String question) {
        Scanner userInput = new Scanner(System.in); //Scans the user input
        System.out.print(question);

        return userInput.nextLine();
    }
}
