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
        loadImage(fileArg, largeArg, neighbourArg, solveArg);
    }
    /**
     * Loads the image file. This can take command line arguments for the filepath, solve method, neighbour method and continuing for large imgs.
     * The order is file, large img (y/n), neighbour method(1/2), solve method (1 - 4)
     */
    private void loadImage(String fileArg, String largeArg, String neighbourArg, String solveArg) {
        //flag to indicate that the program is running using command line arguments and not user inputs
        boolean commandLine = (fileArg != null); //if the fileArg != null there are arguments specified
        BufferedImage in;

        //Getting the filename
        String filePath;
        if (commandLine) filePath = fileArg;
        else filePath = getUserInput("Please enter the file name: ");

        //Checking if the file exists
        try {
            in = ImageIO.read(new File(filePath));

            BufferedImage imgFile = new BufferedImage(in.getWidth(), in.getHeight(), BufferedImage.TYPE_INT_RGB);

            Graphics2D g = imgFile.createGraphics();
            g.drawImage(in, 0, 0, null);
            g.dispose();
            checkForLargeImage(largeArg, commandLine, imgFile);

            //Ask the user how they might want to process a large image
            boolean neighbourLoad = getNeighbourMethod(neighbourArg, commandLine, imgFile);

            //Load the image into a 2d array to save space
            ImageFile imgObj = new ImageFile(imgFile);
            System.out.println("Img height: " + imgObj.getHeight());
            System.out.println("Img width: " + imgObj.getWidth());
            System.out.println("Approximately " + imgObj.getHeight() * imgObj.getWidth() * 0.15 + " nodes");

            //Release the image memory
            imgFile = null;
            System.gc();


            //Calls method to solve the image
            solve(imgObj, filePath, neighbourLoad, commandLine, solveArg);

        } catch (Exception e) {
            System.out.println("Program aborted: " + e);
        }
    }


    /**
     * Checks if the user wants to find all the neighbours before or during solving.
     * @param neighbourArg Used when the program is invoked via the command line
     * @param commandLine Flag to signal that the program was invoked from the command line
     * @param imgFile the image file to process
     * @return the search that the user wants to use
     */
    private boolean getNeighbourMethod(String neighbourArg, boolean commandLine, BufferedImage imgFile) {
        while (true) {
            if (imgFile.getHeight() * imgFile.getWidth() > Math.pow(6, 2)) {
                //look for command line input
                String answer;
                if (commandLine) {
                    answer = neighbourArg;
                } else {
                    answer = getUserInput("Press 1 to look for neighbours during loading (faster) or press 2 to look during solving (more memory efficient)? \n");
                }
                if (answer.equals("1")) {
                    break;
                } else if (answer.equals("2")) {
                    return false;
                } else {
                    System.out.println("Invalid input. Try again!.");
                }
            } else {
                break;
            }
        }
        return true;
    }

    /**
     * Check for large images and ask the user how they want to proceede.
     * @param largeArg Used when the program is invoked via the command line
     * @param commandLine Flag to signal that the program was invoked from the command line
     * @param imgFile the image file to process
     */
    private void checkForLargeImage(String largeArg, boolean commandLine, BufferedImage imgFile) {
        //Checking for large images
        while (true) {
            //should be 4000
            if (imgFile.getHeight() * imgFile.getWidth() > Math.pow(1999, 2)) {
                //Look for command line input
                String answer;
                if (commandLine) {
                    answer = largeArg;
                } else {
                    answer = getUserInput("This image is very large. You may run into memory issues. \n" + "Do you want to continue? y/n ");
                }
                if (answer.equals("y")) {
                    break;
                } else if (answer.equals("n")) {
                    System.out.println("Returning to image selection");
                    loadImage(null, null, null, null);
                    break;
                } else {
                    System.out.println("Invalid input. Try again!.");
                }
            } else {
                break;
            }
        }
    }

    /**
     * Solves the maze
     */
    private void solve(ImageFile imgObj, String filePath, boolean lookForNeighbours, boolean commadLine, String solveArg) throws IOException, IllegalAccessException {
        long numNodes = 0;

        //Time tracking variables
        long startTime;
        float midTime;
        startTime = System.currentTimeMillis();


        //Map containing the positions of each node
        HashMap<Coordinates, MazeNode> nodes = new HashMap<>();

        System.out.println("Finding nodes");

        //Only load this if it is requested
        if (lookForNeighbours) {
            nodes = ImageManipulation.findNeighboursForAll(imgObj);
            numNodes = nodes.size();
        }

        //Save the current time and reset.
        midTime = (System.currentTimeMillis() - startTime) / 1000F;

        //Calculate the x position of the start and end
        int xStart = 0, xEnd = 0;
        for (int width = 0; width < imgObj.getWidth(); width++) {
            if (imgObj.isWhite(width, 0)) xStart = width;
        }
        for (int width = 0; width < imgObj.getWidth(); width++) {
            if (imgObj.isWhite(width, imgObj.getHeight() - 1)) xEnd = width;
        }

        //If the nodes are to be initialised while solving add the start and end now
        if (!lookForNeighbours) {
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

        //Asking the user which method they would like to use to solve the maze
        label:
        while (true) {
            String answer;
            if (commadLine) {
                answer = solveArg;
            } else {
                answer = getUserInput("Press 1 for DFS \n" +
                        "Press 2 for BFS \n" +
                        "Press 3 for Dijkstra \n" +
                        "Press 4 for AStar ");
            }
            switch (answer) {
                case "1":
                    startTime = System.currentTimeMillis();
                    if (imgObj.getWidth() * imgObj.getHeight() > Math.pow(6000, 2)) {
                        System.out.println("Maze to large for DFS. Using AStar instead.");
                        SolveMethods.solveAStar(imgObj, nodes.get(new Coordinates(xStart, 0)), nodes.get(new Coordinates(xEnd, imgObj.getHeight() - 1)), filePath, nodes);
                    } else {
                        SolveMethods.solveDFS(imgObj, nodes.get(new Coordinates(xStart, 0)), nodes.get(new Coordinates(xEnd, imgObj.getHeight() - 1)), filePath, nodes);

                    }
                    break label;
                case "2":
                    startTime = System.currentTimeMillis();
                    //Moving to faster algorithm if required
                    if (imgObj.getWidth() * imgObj.getHeight() > Math.pow(999, 2)) {
                        System.out.println("Maze to large for BFS. Using AStar instead.");
                        SolveMethods.solveAStar(imgObj, nodes.get(new Coordinates(xStart, 0)), nodes.get(new Coordinates(xEnd, imgObj.getHeight() - 1)), filePath, nodes);

                    } else {
                        SolveMethods.solveBFS(imgObj, nodes.get(new Coordinates(xStart, 0)), nodes.get(new Coordinates(xEnd, imgObj.getHeight() - 1)), filePath, nodes);
                    }
                    break label;
                case "3":
                    startTime = System.currentTimeMillis();
                    SolveMethods.solveDijkstra(imgObj, nodes.get(new Coordinates(xStart, 0)), nodes.get(new Coordinates(xEnd, imgObj.getHeight() - 1)), filePath, nodes);

                    break label;
                case "4":
                    startTime = System.currentTimeMillis();
                    SolveMethods.solveAStar(imgObj, nodes.get(new Coordinates(xStart, 0)), nodes.get(new Coordinates(xEnd, imgObj.getHeight() - 1)), filePath, nodes);

                    break label;
                default:
                    System.out.println("Invalid input!");
                    break;
            }
        }

        //Print the time at the end
        float seconds = ((System.currentTimeMillis() - startTime) / 1000F) + midTime;
        System.out.println("Time spent solving: " + seconds + "s");
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
