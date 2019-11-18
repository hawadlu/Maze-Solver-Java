import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Path;
import java.util.*;

/**
 * This program loads a png image of a maze.
 * It will attempt to solve it and output a solved image file.
 */

class Solver {
    /**
     * Loads the image file
     */
    private void loadImage() {
        BufferedImage in;

        //Getting the filename
        String filePath = getUserInput("Please enter the file name: ");

        //Checking if the file exists
        try {
            in = ImageIO.read(new File(filePath));

            BufferedImage imgFile = new BufferedImage(
                    in.getWidth(), in.getHeight(), BufferedImage.TYPE_INT_RGB);

            Graphics2D g = imgFile.createGraphics();
            g.drawImage(in, 0, 0, null);
            g.dispose();

            //Checking for large images
            while (true) {
                if (imgFile.getHeight() * imgFile.getWidth() > Math.pow(4000, 2)) {
                    String answer = getUserInput("This image is very large. You may run into memory issues. \n" +
                            "Do you want to continue? y/n ");
                    if (answer.equals("y")) {
                        break;
                    } else if (answer.equals("n")) {
                        System.out.println("Returning to image selection");
                        loadImage();
                        break;
                    } else {
                        System.out.println("Invalid input. Try again!.");
                    }
                } else {
                    break;
                }
            }

            //Calls method to solve the image
            solve(imgFile, filePath);

            loadImage();
        } catch (Exception e) {
            System.out.println("Program aborted: " + e);
            loadImage();
        }
    }

    /**
     * Solves the maze
     */
    private void solve(BufferedImage imgFile, String filePath) {
        long numNodes = 0;

        //Time tracking variables
        long startTime, endTime;
        float midTime;
        startTime = System.currentTimeMillis();

        //Array of all internal maze nodes
        MazeNode[][] nodes = new MazeNode[imgFile.getHeight()][imgFile.getWidth()]; //A boolean indicator to tell if there is a node at pos x,y

        System.out.println("Finding nodes");

        //Performing a one pass over the maze to find all the nodes
        for (int height = 0; height < imgFile.getHeight(); height++) {
            for (int width = 0; width < imgFile.getWidth(); width++) {
                //Gets the 0-255 value for red. Colour is either white or black so can use R, G or B.
                int colour = getColour(imgFile, width, height);

                //Don't make a node unless the square is white
                if (getColour(imgFile, width, height) != 0) {
                    if (height == 0 && colour != 0) {
                        nodes[height][width] = new MazeNode(width, height);
                        numNodes++;
                    } else if(height == imgFile.getHeight() - 1 && colour != 0) {
                        nodes[height][width] = new MazeNode(width, height);
                        numNodes++;
                        //marking dead end nodes
                    } else if (isDeadEnd(imgFile, width, height)) {
                        nodes[height][width] = new MazeNode(width, height);
                        numNodes++;

                        //Marking nodes at junctions
                    } else if (isJunction(imgFile, width, height)) {
                        nodes[height][width] = new MazeNode(width, height);
                        numNodes++;

                        //Marking pixels on corner junctions
                    } else if (getAdjacentWhite(imgFile, width, height) == 2 && !directOpposite(imgFile, width, height)) {
                        nodes[height][width] = new MazeNode(width, height);
                        numNodes++;
                    } else {
                        //Mark no node at this position
                        nodes[height][width] = null; //Mark node at this position
                    }
                }
            }
        }

        //Calculate the x position of the start and end
        int xStart = 0, xEnd = 0;
        for (int width = 0; width < nodes[0].length; width++) {
            if (nodes[0][width] != null) {
                xStart = width;
            }
        }
        for (int width = 0; width < nodes[0].length; width++) {
            if (nodes[nodes.length - 1][width] != null) {
                xEnd = width;
            }
        }

        System.out.println("Node count: " + numNodes);
        System.out.println("Nodes size: " + nodes.length);
        System.out.println("Finding neighbours");

        //Finding each nodes neighbours
        for (int height = 0; height < imgFile.getHeight(); height++) {
            for (int width = 0; width < imgFile.getWidth(); width++) {
                if (nodes[height][width] != null) {
                    findNeighbours(nodes[height][width], nodes, imgFile);
                }
            }
        }

        //Save the current time and reset.
        midTime = (System.currentTimeMillis() - startTime) / 1000F;

                System.out.println("Solving");

        //Asking the user which method they would like to use to solve the maze
        label:
        while (true) {
            String answer = getUserInput("Press 1 for DFS \n" +
                    "Press 2 for BFS \n" +
                    "Press 3 for Dijkstra \n" +
                    "Press 4 for AStar ");
            switch (answer) {
                case "1":
                    startTime = System.currentTimeMillis();
                    if (imgFile.getWidth() * imgFile.getHeight() > Math.pow(6000, 2)) {
                        System.out.println("Maze to large for DFS. Using AStar instead.");
                        solveAStar(imgFile, nodes[0][xStart], nodes[imgFile.getHeight() - 1][xEnd], filePath);
                    } else {
                        solveDFS(imgFile, nodes[0][xStart], nodes[imgFile.getHeight() - 1][xEnd], filePath);

                    }
                    break label;
                case "2":
                    startTime = System.currentTimeMillis();
                    //Moving to faster algorithm if required
                    if (imgFile.getWidth() * imgFile.getHeight() > Math.pow(3000, 2)) {
                        System.out.println("Maze to large for BFS. Using AStar instead.");
                        solveAStar(imgFile, nodes[0][xStart], nodes[imgFile.getHeight() - 1][xEnd], filePath);

                    } else {
                        solveBFS(imgFile, nodes[0][xStart], nodes[imgFile.getHeight() - 1][xEnd], filePath);
                    }
                    break label;
                case "3":
                    startTime = System.currentTimeMillis();
                    solveDijkstra(imgFile, nodes[0][xStart], nodes[imgFile.getHeight() - 1][xEnd], filePath);

                    break label;
                case "4":
                    startTime = System.currentTimeMillis();
                    solveAStar(imgFile, nodes[0][xStart], nodes[imgFile.getHeight() - 1][xEnd], filePath);

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
    private void solveDFS(BufferedImage imgFile, MazeNode start, MazeNode destination, String filePath) {
        //Create a DFS object
        DFS dfs = new DFS();
        dfs.solve(start, destination);

        System.out.println("Maze solved. Nodes in path: " + dfs.getPathSize());
        System.out.println("Drawing image");

        //Draw
        drawImage(imgFile, dfs.getPath(), start, filePath, "DFS");
    }

    /**
     * Solves the maze breadth first
     */
    private void solveBFS(BufferedImage imgFile, MazeNode start, MazeNode destination, String filePath) {

        //Create a BFS object
        BFS bfs = new BFS();
        bfs.solve(start, destination);

        System.out.println("Maze solved. Nodes in path: " + bfs.getPathSize());
        System.out.println("Drawing image");

        //Draw
        drawImage(imgFile, bfs.getPath(), start, filePath, "BFS");
    }

    /**
     * Solves the maze using the Dijkstra algorithm
     */
    private void solveDijkstra(BufferedImage imgFile, MazeNode start, MazeNode destination, String filePath) {
        //Create a DFS object
        Dijkstra dijkstra = new Dijkstra();
        dijkstra.solve(start, destination);

        System.out.println("Maze solved. Nodes in path: " + dijkstra.getPathSize());
        System.out.println("Drawing image");


        //Draw
        drawImage(imgFile, dijkstra.getPath(), start, filePath, "Dijkstra");
    }

    /**
     * Solves the maze using the AStar algorithm
     */
    private void solveAStar(BufferedImage imgFile, MazeNode start, MazeNode destination, String filePath) {
        //Create a DFS object
        AStar aStar = new AStar();
        aStar.solve(start, destination);

        System.out.println("Maze solved. Nodes in path: " + aStar.getPathSize());
        System.out.println("Drawing image");

        //Draw
        drawImage(imgFile, aStar.getPath(), start, filePath, "AStar");
    }

    /**
     * This method draws the solved maze and returns it
     */
    private void drawImage(BufferedImage imgFile, ArrayList<MazeNode> nodes, MazeNode entry, String filePath, String searchType) {
        //Colour the entry
        imgFile.setRGB(entry.getX(), entry.getY(), Color.RED.getRGB());

        while (nodes.size() > 1) {
            MazeNode start = nodes.remove(0);
            MazeNode end = nodes.get(0);
            int y, x;

            imgFile.setRGB(start.getX(), start.getY(), Color.RED.getRGB());
            imgFile.setRGB(end.getX(), end.getY(), Color.RED.getRGB());

            //Drawing down
            if (start.getY() < end.getY()) {
                y = start.getY();
                while (y < end.getY()) {
                    imgFile.setRGB(start.getX(), y, Color.RED.getRGB());
                    y += 1;
                }

                //Drawing up
            } else if (start.getY() > end.getY()) {
                y = start.getY();
                while (y > end.getY()) {
                    imgFile.setRGB(start.getX(), y, Color.RED.getRGB());
                    y-=1;
                }

                //Drawing right
            } else if (start.getX() < end.getX()) {
                x = start.getX();
                while (x < end.getX()) {
                    imgFile.setRGB(x, start.getY(), Color.RED.getRGB());
                    x+=1;
                }

                //Drawing left
            } else if (start.getX() > end.getX()) {
                x = start.getX();
                while (x > end.getX()) {
                    imgFile.setRGB(x, start.getY(), Color.RED.getRGB());
                    x-=1;
                }
            }
        }

        saveImage(imgFile, filePath, searchType);
    }

    /**
     * This method takes and node and finds all of it neighbours
     * It then adds the neighbours to the neighbours set of the node.
     */
    private void findNeighbours(MazeNode node, MazeNode[][] nodes, BufferedImage imgFile) {
        //Looking down for a neighbour
        for (int shift = 1; node.getY() + shift < nodes.length; shift++) {
            if (nodes[node.getY() + shift][node.getX()] != null) {
                node.addNeighbour(nodes[node.getY() + shift][node.getX()]);
                break;

            //wall
            } else if (getColour(imgFile, node.getX(), node.getY() + shift) == 0) {
                break;
            }
        }

        //Looking up for a neighbour
        for (int shift = 1; node.getY() - shift > -1; shift++) {
            if (nodes[node.getY() - shift][node.getX()] != null) {
                node.addNeighbour(nodes[node.getY() - shift][node.getX()]);
                break;
            } else if (getColour(imgFile, node.getX(), node.getY() - shift) == 0) {
                break;
            }
        }

        //Looking right for a neighbour
        for (int shift = 1; node.getX() + shift < nodes[node.getY()].length; shift++) {
            if (nodes[node.getY()][node.getX() + shift] != null) {
                node.addNeighbour(nodes[node.getY()][node.getX() + shift]);
                break;
            } else if (getColour(imgFile, node.getX() + shift, node.getY()) == 0) {
                break;
            }
        }

        //Looking left for a neighbour
        for (int shift = 1; node.getX() - shift > -1; shift++) {
            if (nodes[node.getY()][node.getX() - shift] != null) {
                node.addNeighbour(nodes[node.getY()][node.getX() - shift]);
                break;
            } else if (getColour(imgFile, node.getX() - shift, node.getY()) == 0) {
                break;
            }
        }

    }

    /**
     * Gets the colour of the specified square (red)
     */
    private int getColour(BufferedImage imgFile, int width, int height) {
        return imgFile.getRGB(width, height) & 0x00ff0000 >> 16;
    }

    /**
     * Returns true if two opposite squares are white
     */

    private boolean directOpposite(BufferedImage imgFile, int width, int height) {
        return (getColour(imgFile, width - 1, height) != 0 && getColour(imgFile, width + 1, height) != 0) ||
                (getColour(imgFile, width, height - 1) != 0 && getColour(imgFile, width, height + 1) != 0);
    }

    /**
     * Gets and returns the number of adjacent white squares
     */
    private int getAdjacentWhite(BufferedImage imgFile, int width, int height) {
        int adjacent = 0;
        //checking each of the surrounding squares
        //Looking left
        if ((width - 1 > -1) && (getColour(imgFile, width - 1, height) != 0)) {
            adjacent++;
        }

        //Looking right
        if ((width + 1 < imgFile.getWidth()) && (getColour(imgFile, width + 1, height) != 0)) {
            adjacent++;
        }

        //Looking up
        if ((height - 1 > -1) && (getColour(imgFile, width, height - 1) != 0)) {
            adjacent++;
        }

        //Looking down
        if ((height + 1 < imgFile.getHeight()) && (getColour(imgFile, width, height + 1) != 0)) {
            adjacent++;
        }

        return adjacent;
    }

    /**
     * Checks to see if this part of the maze is a junction
     * Returns true if it is.
     */
    private boolean isJunction(BufferedImage imgFile, int width, int height) {
        int whiteSides = getAdjacentWhite(imgFile, width, height);

        return whiteSides > 2;
    }

    /**
     * Checks to see if this part of the maze is a dead end.
     * Returns true if it is.
     */
    private boolean isDeadEnd(BufferedImage imgFile, int width, int height) {
        int blackSides = 0;

        //checking each of the surrounding squares
        if ((width - 1 > -1) && (getColour(imgFile, width - 1, height) == 0)) {
            blackSides++;
        }
        if ((width + 1 < imgFile.getWidth()) && (getColour(imgFile, width + 1, height) == 0)) {
            blackSides++;
        }
        if ((height - 1 > -1) && (getColour(imgFile, width, height - 1) == 0)) {
            blackSides++;
        }
        if ((height + 1 < imgFile.getHeight()) && (getColour(imgFile, width, height + 1) == 0)) {
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
    public static void main(String[] arguments) {
        Solver solver = new Solver();
        solver.loadImage();
    }
}
