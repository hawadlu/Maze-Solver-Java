import javax.imageio.ImageIO;
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
     * Constructor
     */
    Solver(String fileArg, String largeArg, String neighbourArg, String solveArg) {
        GUI gui = new GUI();
    }

    /**
     * Solves the maze
     */
    public static BufferedImage solve(File image, Object algorithm, Object searchType) throws IOException, IllegalAccessException {
        //Load the image object
        ImageFile imgObj = new ImageFile(ImageIO.read(image));

        long numNodes = 0;

        //Map containing the positions of each node
        HashMap<Coordinates, MazeNode> nodes = new HashMap<>();

        System.out.println("Finding nodes");

        //Only load this if it is requested
        if (searchType.equals("Search for neighbours during loading")) {
            nodes = ImageManipulation.findNeighboursForAll(imgObj);
            numNodes = nodes.size();
        }

        //Calculate the x position of the start and end
        int xStart = 0, xEnd = 0;
        for (int width = 0; width < imgObj.getWidth(); width++) {
            if (imgObj.isWhite(width, 0)) xStart = width;
        }
        for (int width = 0; width < imgObj.getWidth(); width++) {
            if (imgObj.isWhite(width, imgObj.getHeight() - 1)) xEnd = width;
        }

        //If the nodes are to be initialised while solving add the start and end now
        if (!searchType.equals("Search for neighbours during loading")) {
            //Put the start and end in the map
            nodes.put(new Coordinates(xStart, 0), new MazeNode(xStart, 0));
            nodes.put(new Coordinates(xEnd, imgObj.getHeight() - 1), new MazeNode(xEnd, imgObj.getHeight() - 1));
        }

        System.out.println("Found all nodes");
        System.out.println("Start at: " + xStart + ", " + 0);
        System.out.println("End at: " + xEnd + ", " + (imgObj.getHeight() - 1));
        System.out.println("Node count: " + numNodes);
        System.out.println("Approximately " + (float) numNodes / (imgObj.getHeight() * imgObj.getWidth()) + "% of pixels are nodes. Assumed storage is: " + numNodes * 114 + " bytes");
        System.out.println("Finding neighbours");
        System.out.println("Solving");

        //Determine the method that should be used to solve the maze
        //todo implement automatic switching for larger mazes
        if (algorithm.equals("Depth First")) {
            return SolveMethods.solveDFS(imgObj, nodes.get(new Coordinates(xStart, 0)), nodes.get(new Coordinates(xEnd, imgObj.getHeight() - 1)), nodes);
        } else if (algorithm.equals("Breadth First")) {
            return SolveMethods.solveBFS(imgObj, nodes.get(new Coordinates(xStart, 0)), nodes.get(new Coordinates(xEnd, imgObj.getHeight() - 1)), nodes);
        } else if (algorithm.equals("Dijkstra")) {
            return SolveMethods.solveDijkstra(imgObj, nodes.get(new Coordinates(xStart, 0)), nodes.get(new Coordinates(xEnd, imgObj.getHeight() - 1)), nodes);
        } else {
            return SolveMethods.solveAStar(imgObj, nodes.get(new Coordinates(xStart, 0)), nodes.get(new Coordinates(xEnd, imgObj.getHeight() - 1)), nodes);
        }
    }

    /**
     * Gets user input and returns a string
     */
    private String getUserInput(String question) {
        Scanner userInput = new Scanner(System.in); //Scans the user input
        System.out.print(question);

        return userInput.nextLine();
    }

    /**
     * @param arguments The order is file, large img (y/n), neighbour method (1/2), solve method (1 - 4)
     * Note that the command line part is primarily for testing
     */
    public static void main(String[] arguments) {
        String filePath = null, loadLarge = null, neighbourSearch = null, searchMethod = null;

        if (arguments.length > 0) {
            filePath = arguments[0];
            loadLarge = arguments[1];
            neighbourSearch = arguments[2];
            searchMethod = arguments[3];
        }
        new Solver(filePath, loadLarge, neighbourSearch, searchMethod);
    }
}
