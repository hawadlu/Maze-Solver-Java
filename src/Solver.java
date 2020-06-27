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

            //Ask the user how they might want to process a large image
            boolean neighbourLoad = true;
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
                        neighbourLoad = false;
                        break;
                    } else {
                        System.out.println("Invalid input. Try again!.");
                    }
                } else {
                    break;
                }
            }

            //Load the image into a 2d array to save space
            ImageFile imgObj = new ImageFile(imgFile);
            System.out.println("Img height: " + imgObj.getHeight());
            System.out.println("Img width: " + imgObj.getWidth());
            System.out.println("Approximately " + imgObj.getHeight() * imgObj.getWidth() * 0.15 + " nodes");
            imgFile = null;

            System.gc();


            //Calls method to solve the image
            solve(imgObj, filePath, neighbourLoad, commandLine, solveArg);

        } catch (Exception e) {
            System.out.println("Program aborted: " + e);
        }
    }

    /**
     * Solves the maze
     */
    private void solve(ImageFile imgObj, String filePath, boolean lookForNeighbours, boolean commadLine, String solveArg) throws IOException, IllegalAccessException {
        long numNodes = 0;

        //Time tracking variables
        long startTime, endTime;
        float midTime;
        startTime = System.currentTimeMillis();

        //HashMap<Coordinates, MazeNode> nodes = new MazeNode[imgFile.getHeight()][imgFile.getWidth()]; //A boolean indicator to tell if there is a node at pos x,y

        //Map containing the positions of each node
        HashMap<Coordinates, MazeNode> nodes = new HashMap<>();

        System.out.println("Finding nodes");

        //Only load this if it is requested
        if (lookForNeighbours) {
            //Performing a one time pass over the maze to find all the nodes. Only look for the neighbours if it has been specified
            for (int height = 0; height < imgObj.getHeight(); height++) {
                for (int width = 0; width < imgObj.getWidth(); width++) {
                    //Don't make a node unless the square is white
                    if (imgObj.isWhite(width, height)) {
                        if (height == 0) {
                            nodes.put(new Coordinates(width, height), new MazeNode(width, height));
                            numNodes++;

                            //Find the neighbours
                            findNeighbours(nodes, new Coordinates(width, height), imgObj);

                        } else if (height == imgObj.getHeight() - 1) {
                            nodes.put(new Coordinates(width, height), new MazeNode(width, height));
                            numNodes++;

                            //Find the neighbours
                            findNeighbours(nodes, new Coordinates(width, height), imgObj);

                            //marking dead end nodes
                        } else if (isDeadEnd(imgObj, width, height)) {
                            nodes.put(new Coordinates(width, height), new MazeNode(width, height));
                            numNodes++;

                            //Find the neighbours
                            findNeighbours(nodes, new Coordinates(width, height), imgObj);

                            //Marking nodes at junctions
                        } else if (isJunction(imgObj, width, height)) {
                            nodes.put(new Coordinates(width, height), new MazeNode(width, height));
                            numNodes++;

                            //Find the neighbours
                            findNeighbours(nodes, new Coordinates(width, height), imgObj);

                            //Marking pixels on corner junctions
                        } else if (getAdjacentWhite(imgObj, width, height) == 2 && !directOpposite(imgObj, width, height)) {
                            nodes.put(new Coordinates(width, height), new MazeNode(width, height));
                            numNodes++;

                            //Find the neighbours
                            findNeighbours(nodes, new Coordinates(width, height), imgObj);
                        }
                    }
                }
                //For debugging
                if (height % 2000 == 0) {
                    System.out.println("Scanned " + height * imgObj.getWidth() + " of " + imgObj.getHeight() * imgObj.getWidth() + " pixels");
                }
            }
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
                        solveAStar(imgObj, nodes.get(new Coordinates(xStart, 0)), nodes.get(new Coordinates(xEnd, imgObj.getHeight() - 1)), filePath, nodes);
                    } else {
                        solveDFS(imgObj, nodes.get(new Coordinates(xStart, 0)), nodes.get(new Coordinates(xEnd, imgObj.getHeight() - 1)), filePath, nodes);

                    }
                    break label;
                case "2":
                    startTime = System.currentTimeMillis();
                    //Moving to faster algorithm if required
                    if (imgObj.getWidth() * imgObj.getHeight() > Math.pow(999, 2)) {
                        System.out.println("Maze to large for BFS. Using AStar instead.");
                        solveAStar(imgObj, nodes.get(new Coordinates(xStart, 0)), nodes.get(new Coordinates(xEnd, imgObj.getHeight() - 1)), filePath, nodes);

                    } else {
                        solveBFS(imgObj, nodes.get(new Coordinates(xStart, 0)), nodes.get(new Coordinates(xEnd, imgObj.getHeight() - 1)), filePath, nodes);
                    }
                    break label;
                case "3":
                    startTime = System.currentTimeMillis();
                    solveDijkstra(imgObj, nodes.get(new Coordinates(xStart, 0)), nodes.get(new Coordinates(xEnd, imgObj.getHeight() - 1)), filePath, nodes);

                    break label;
                case "4":
                    startTime = System.currentTimeMillis();
                    solveAStar(imgObj, nodes.get(new Coordinates(xStart, 0)), nodes.get(new Coordinates(xEnd, imgObj.getHeight() - 1)), filePath, nodes);

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
     * Solves the maze depth first
     */
    private void solveDFS(ImageFile imgObj, MazeNode start, MazeNode destination, String filePath, HashMap<Coordinates, MazeNode> nodes) {
        //Create a DFS object
        DFS dfs = new DFS();
        dfs.solve(start, destination, nodes);

        System.out.println("Maze solved. Nodes in path: " + dfs.getPathSize());
        System.out.println("Drawing image");

        //Draw
        drawImage(imgObj, dfs.getPath(), start, filePath, "DFS");
    }

    /**
     * Solves the maze breadth first
     */
    private void solveBFS(ImageFile imgObj, MazeNode start, MazeNode destination, String filePath, HashMap<Coordinates, MazeNode> nodes) {

        //Create a BFS object
        BFS bfs = new BFS();
        bfs.solve(start, destination, nodes);

        System.out.println("Maze solved. Nodes in path: " + bfs.getPathSize());
        System.out.println("Drawing image");

        //Draw
        drawImage(imgObj, bfs.getPath(), start, filePath, "BFS");
    }

    /**
     * Solves the maze using the Dijkstra algorithm
     */
    private void solveDijkstra(ImageFile imgObj, MazeNode start, MazeNode destination, String filePath, HashMap<Coordinates, MazeNode> nodes) {
        //Create a DFS object
        Dijkstra dijkstra = new Dijkstra();
        dijkstra.solve(start, destination, nodes);

        System.out.println("Maze solved. Nodes in path: " + dijkstra.getPathSize());
        System.out.println("Drawing image");


        //Draw
        drawImage(imgObj, dijkstra.getPath(), start, filePath, "Dijkstra");
    }

    /**
     * Solves the maze using the AStar algorithm
     */
    private void solveAStar(ImageFile imgObj, MazeNode start, MazeNode destination, String filePath, HashMap<Coordinates, MazeNode> nodes) {
        //Create an AStar object
        AStar aStar = new AStar();
        aStar.solve(imgObj, start, destination, nodes);

        //todo delete in the other methods?
        nodes.clear();

        System.out.println("Maze solved. Nodes in path: " + aStar.getPathSize());
        System.out.println("Drawing image");

        //Draw
        drawImage(imgObj, aStar.getPath(), start, filePath, "AStar");
    }

    /**
     * This method draws the solved maze and returns it
     */
    private void drawImage(ImageFile imgObj, ArrayList<MazeNode> nodes, MazeNode entry, String filePath, String searchType) {
        BufferedImage save = new BufferedImage(imgObj.getWidth(), imgObj.getHeight(), BufferedImage.TYPE_INT_RGB);

        for (int height = 0; height < imgObj.getHeight(); height++) {
            for (int width = 0; width < imgObj.getWidth(); width++) {
                if (imgObj.isWhite(width, height)) {
                    save.setRGB(width, height, Color.WHITE.getRGB());
                } else {
                    save.setRGB(width, height, Color.BLACK.getRGB());
                }
            }
        }

        //Colour the entry
        save.setRGB(entry.getX(), entry.getY(), Color.RED.getRGB());

        while (nodes.size() > 1) {
            MazeNode start = nodes.remove(0);
            MazeNode end = nodes.get(0);
            int y, x;

            save.setRGB(start.getX(), start.getY(), Color.RED.getRGB());
            save.setRGB(end.getX(), end.getY(), Color.RED.getRGB());

            //Drawing down
            if (start.getY() < end.getY()) {
                y = start.getY() + 1;
                while (y < end.getY()) {
                    save.setRGB(start.getX(), y, Color.RED.getRGB());
                    y += 1;
                }

                //Drawing up
            } else if (start.getY() > end.getY()) {
                y = start.getY();
                while (y > end.getY()) {
                    save.setRGB(start.getX(), y, Color.RED.getRGB());
                    y-=1;
                }

                //Drawing right
            } else if (start.getX() < end.getX()) {
                x = start.getX();
                while (x < end.getX()) {
                    save.setRGB(x, start.getY(), Color.RED.getRGB());
                    x+=1;
                }

                //Drawing left
            } else if (start.getX() > end.getX()) {
                x = start.getX();
                while (x > end.getX()) {
                    save.setRGB(x, start.getY(), Color.RED.getRGB());
                    x-=1;
                }
            }
        }

        saveImage(save, filePath, searchType);
    }

    /**
     * This method takes a set of Coordinates of a known node and looks for neighbours.
     * The program goes left to right, top to bottom, so this will search to the left and top of the position
     */
    private void findNeighbours(HashMap<Coordinates, MazeNode> nodes, Coordinates currentLocation, ImageFile imgObj) {
        //Look up until a node or a black square is encountered
        for (int y = currentLocation.y - 1; y > -1; y--) {
            //Break if a black pixel is detected
            if (!imgObj.isWhite(currentLocation.x, y)) {
                break;
                //Break if a node is located
            } else if (nodes.containsKey(new Coordinates(currentLocation.x, y))) {
                //If a node is found, mark them as neighbours
                nodes.get(currentLocation).addNeighbour(new Coordinates(currentLocation.x, y));
                nodes.get(new Coordinates(currentLocation.x, y)).addNeighbour(currentLocation);
                break;
            }
        }

        //Look up left a node or a black square is encountered
        for (int x = currentLocation.x - 1; x > -1; x--) {
            //Break if a black pixel is detected
            if (!imgObj.isWhite(x, currentLocation.y)) {
                break;
                //Break if a node is located
            } else if (nodes.containsKey(new Coordinates(x, currentLocation.y))) {
                //If a node is found, mark them as neighbours
                nodes.get(currentLocation).addNeighbour(new Coordinates(x, currentLocation.y));
                nodes.get(new Coordinates(x, currentLocation.y)).addNeighbour(currentLocation);
                break;
            }
        }
    }

    /**
     * Returns true if two opposite squares are white
     */

    private boolean directOpposite(ImageFile imjObj, int width, int height) {
        return (imjObj.isWhite(width - 1, height) && imjObj.isWhite(width + 1, height)) ||
                (imjObj.isWhite(width, height - 1)&& imjObj.isWhite(width, height + 1));


//        return (getColour(imgFile, width - 1, height) != 0 && getColour(imgFile, width + 1, height) != 0) ||
//                (getColour(imgFile, width, height - 1) != 0 && getColour(imgFile, width, height + 1) != 0);
    }

    /**
     * Gets and returns the number of adjacent white squares
     */
    private int getAdjacentWhite(ImageFile imgObj, int width, int height) {
        int adjacent = 0;
        //checking each of the surrounding squares
        //Looking left
        if ((width - 1 > -1) && (imgObj.isWhite(width - 1, height))) {
            adjacent++;
        }

        //Looking right
        if ((width + 1 < imgObj.getWidth()) && (imgObj.isWhite(width + 1, height))) {
            adjacent++;
        }

        //Looking up
        if ((height - 1 > -1) && (imgObj.isWhite(width, height - 1))) {
            adjacent++;
        }

        //Looking down
        if ((height + 1 < imgObj.getHeight()) && (imgObj.isWhite(width, height + 1))) {
            adjacent++;
        }

        return adjacent;
    }

    /**
     * Checks to see if this part of the maze is a junction
     * Returns true if it is.
     */
    private boolean isJunction(ImageFile imgObj, int width, int height) {
        int whiteSides = getAdjacentWhite(imgObj, width, height);

        return whiteSides > 2;
    }

    /**
     * Checks to see if this part of the maze is a dead end.
     * Returns true if it is.
     */
    private boolean isDeadEnd(ImageFile imgObj, int width, int height) {
        int blackSides = 0;

        //checking each of the surrounding squares
        if ((width - 1 > -1) && (!imgObj.isWhite(width - 1, height))) {
            blackSides++;
        }
        if ((width + 1 < imgObj.getWidth()) && (!imgObj.isWhite(width + 1, height))) {
            blackSides++;
        }
        if ((height - 1 > -1) && (!imgObj.isWhite(width, height - 1))) {
            blackSides++;
        }
        if ((height + 1 < imgObj.getHeight()) && (!imgObj.isWhite(width, height + 1))) {
            blackSides++;
        }

        return blackSides > 2;
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
     * Strips the image type then adds the 'solved' suffix
     */
    private String insertSuffix(String filePath, String searchType) {
        StringBuilder finalPath = new StringBuilder();
        String[] filePathArr = filePath.split("");

        for (String s : filePathArr) {
            if (s.equals(".")) {
                finalPath.append(" solved ").append(searchType).append(".");
            } else if (s.equals("/")) {
                finalPath.append("/Solved/");
            } else {
                finalPath.append(s);
            }
        }
        return finalPath.toString();
    }

    /**
     * Saves solved images in the images folder
     */
    private void saveImage(BufferedImage img, String filePath, String searchType) {
        //Checking if the folder exists
        if (!new File("/Images/Solved").isDirectory()) {
            new File("Images/Solved").mkdir();
        }

        //Saving the image
        String fileName = insertSuffix(filePath, searchType);
        try {
            ImageIO.write(img, "png", new File(fileName));
            System.out.println("Image saved as " + fileName);
        } catch (Exception e) {
            System.out.println("Unable to save image: " + e);
        }
    }

    // Main

    /**
     * @param arguments The order is file, large img (y/n), neighbour method (1/2), solve method (1 - 4)
     * Note that the command line part is primarily for testing
     */
    public static void main(String[] arguments) {
        Solver solver = new Solver();
        String filePath = null, loadLarge = null, neighbourSearch = null, searchMethod = null;

        if (arguments.length > 0) {
            filePath = arguments[0];
            loadLarge = arguments[1];
            neighbourSearch = arguments[2];
            searchMethod = arguments[3];
        }
        solver.loadImage(filePath, loadLarge, neighbourSearch, searchMethod);
    }
}
