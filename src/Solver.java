import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.Scanner;

/**
 * This program loads a png image of a maze.
 * It will attempt to solve it and output a solved image file.
 */

public class Solver {
    /**
     * Loads the image file
     */
    public void loadImage() {
        BufferedImage imgFile;

        //Getting the filename
        String filePath = getUserInput("Please enter the file name: ");

        //Checking if the file exists
        try {
            imgFile = ImageIO.read(new File(filePath));
            System.out.println("Image loaded");

            //Calls method to solve the image
            solve(imgFile, filePath);

            loadImage();
        } catch (Exception e){
            System.out.println("File does not exist");
            loadImage();
        }
    }

    /**
     * Solves the maze
     */
    public void solve(BufferedImage imgFile, String filePath) {
        //Creating the start and end nodes
        MazeNode start, end;
        long numNodes = 0;

        //Set of all internal maze nodes
        Set<MazeNode> nodes = new HashSet<>();

        //Performing a one pass over the maze to find all the nodes
        for (int height = 0; height < imgFile.getWidth(); height++) {
            for (int width = 0; width < imgFile.getHeight(); width++) {
                //Gets the 0-255 value for red. Colour is either white or black so can use R, G or B.
                int colour = getColour(imgFile, width, height);

                //Don't make a node unless the square is white
                if (getColour(imgFile, width, height) != 0) {
                    //Marking the start
                    if (height == 0 && colour != 0) {
                        start = new MazeNode(width, height);
                        imgFile.setRGB(width, height, 845909); //Mark the start green
                        numNodes++;

                        //marking the end
                    } else if (height == imgFile.getHeight() - 1 && colour == 255) {
                        end = new MazeNode(width, height);
                        imgFile.setRGB(width, height, 16711680); //Mark the end red
                        numNodes++;

                        //MARKING NODES
                        //marking dead end nodes
                    } else if (isDeadEnd(imgFile, width, height)) {
                        nodes.add(new MazeNode(width, height));
                        //imgFile.setRGB(width, height, 3476712); //Mark the nodes blue
                        numNodes++;

                        //Marking nodes at junctions
                    } else if (isJunction(imgFile, width, height)) {
                        nodes.add(new MazeNode(width, height));
                        //imgFile.setRGB(width, height, 3476712);
                        numNodes++;

                    //Marking pixels on corner junctions
                    } else if (getAdjacentWhite(imgFile, width, height) == 2 && !directOpposite(imgFile, width, height)) {
                        nodes.add(new MazeNode(width, height));
                        //imgFile.setRGB(width, height, 3476712);
                        numNodes++;
                    }
                }
            }
        }

        System.out.println("Node count: " + numNodes);
        System.out.println("Nodes size: " + nodes.size());

        //Calls the method to save the image
        saveImage(imgFile, filePath);
    }

    /**
     * Gets the colour of the specified square
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
        if (getColour(imgFile, width - 1, height) != 0) {
            adjacent++;
        }
        if (getColour(imgFile, width + 1, height) != 0) {
            adjacent++;
        }
        if (getColour(imgFile, width, height - 1) != 0) {
            adjacent++;
        }
        if (getColour(imgFile, width, height + 1) != 0) {
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
        if (getColour(imgFile, width - 1, height) == 0) {
            blackSides++;
        }
        if (getColour(imgFile, width + 1, height) == 0) {
            blackSides++;
        }
        if (getColour(imgFile, width, height - 1) == 0) {
            blackSides++;
        }
        if (getColour(imgFile, width, height + 1) == 0) {
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
    public String insertSuffix(String filePath) {
        StringBuilder finalPath = new StringBuilder();
        String[] filePathArr = filePath.split("");

        for (int i = 0; i < filePathArr.length; i++) {
            if (filePathArr[i].equals(".")) {
                finalPath.append(" solved.");
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
    public void saveImage(BufferedImage img, String filePath) {
        //Saving the image
        String fileName = insertSuffix(filePath);
        //System.out.println("Dir status: " + Files.exists(Paths.get(filePath)));
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

