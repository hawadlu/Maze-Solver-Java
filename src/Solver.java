import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.*;

/**
 * This program loads a png image of a maze.
 * It will attempt to solve it and output a solved image file.
 */

public class Solver {
    /**
     * Loads the image file
     */
    public void loadImage() {
        BufferedImage in;

        //Getting the filename
        String filePath = getUserInput("Please enter the file name: ");

        //Checking if the file exists
        try {
            in = ImageIO.read(new File(filePath));
            System.out.println("Image loaded");

            BufferedImage imgFile = new BufferedImage(
                    in.getWidth(), in.getHeight(), BufferedImage.TYPE_INT_RGB);

            Graphics2D g = imgFile.createGraphics();
            g.drawImage(in, 0, 0, null);
            g.dispose();

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
    public void solve(BufferedImage imgFile, String filePath) {
        long numNodes = 0;

        //Array of all internal maze nodes
        MazeNode[][] nodes = new MazeNode[imgFile.getHeight()][imgFile.getWidth()]; //A boolean indicator to tell if there is a node at pos x,y

        System.out.println("Finding nodes");

        //Performing a one pass over the maze to find all the nodes
        for (int height = 0; height < imgFile.getWidth(); height++) {
            for (int width = 0; width < imgFile.getHeight(); width++) {
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

        System.out.println("Solving");

        //Create a DFS object
        DFS dfs = new DFS();
        dfs.solve(nodes[0][xStart], nodes[imgFile.getHeight() - 1][xEnd]);

        for (MazeNode node: dfs.getPath()) {
            imgFile.setRGB(node.getX(), node.getY(), 325352);
        }

        System.out.println("Maze solved. Nodes in path: " + dfs.getPathSize());
        System.out.println("Drawing image");

        //Draw
        imgFile = drawImage(imgFile, dfs.getPath(), nodes[0][xStart]);

        //Calls the method to save the image
        saveImage(imgFile, filePath, "DFS");
    }

    /**
     * This method draws the solved maze and returns it
     */
    private BufferedImage drawImage(BufferedImage imgFile, ArrayList<MazeNode> nodes, MazeNode entry) {
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
            y = 0;
        }

        return imgFile;
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
    public int getColour(BufferedImage imgFile, int width, int height) {
        return imgFile.getRGB(width, height) & 0x00ff0000 >> 16;
    }

    /**
     * Returns true if two opposite squares are white
     */

    private boolean directOpposite(BufferedImage imgFile, int width, int height) {
        if (getColour(imgFile, width - 1, height) != 0 && getColour(imgFile, width + 1, height) != 0) {
            return true;
        } else if (getColour(imgFile, width, height - 1) != 0 && getColour(imgFile, width, height + 1) != 0) {
            return true;
        }
        return false;
    }

    /**
     * Gets and returns the number of adjacent white squares
     */
    public int getAdjacentWhite(BufferedImage imgFile, int width, int height) {
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

        if (whiteSides > 2) {
            return true;
        } else {
            return false;
        }
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

        if (blackSides > 2) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * Gets user input and returns a string
     */
    public String getUserInput(String question) {
        Scanner userInput = new Scanner(System.in); //Scans the user input
        System.out.print(question);

        return userInput.nextLine();
    }

    /**
     * Strips the image type then adds the 'solved' suffix
     */
    public String insertSuffix(String filePath, String searchType) {
        StringBuilder finalPath = new StringBuilder();
        String[] filePathArr = filePath.split("");

        for (int i = 0; i < filePathArr.length; i++) {
            if (filePathArr[i].equals(".")) {
                finalPath.append(" solved " + searchType + ".");
            } else if (filePathArr[i].equals("/")) {
                finalPath.append("/Solved/");
            } else {
                finalPath.append(filePathArr[i]);
            }
        }
        return finalPath.toString();
    }

    /**
     * Saves solved images in the images folder
     */
    public void saveImage(BufferedImage img, String filePath, String searchType) {
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
        Solver slvr = new Solver();
        slvr.loadImage();
    }
}
