import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Class containing methods for image manipulation.
 * Has methods for looking for neighbours, loading and saving images.
 * NOTE: This class deals with both ImageFile and BufferedImage
 */
public class ImageManipulation {
    /**
     * This method draws the solved maze and returns it
     */
    public static BufferedImage drawImage(ImageFile imgObj, ArrayList<MazeNode> nodes, MazeNode entry) {
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
        return save;
    }


    /**
     * Perform a one time pass over the image, creating and linking all the nodes
     * @param imgObj the image
     * @return the number of nodes
     */
    public static HashMap<Coordinates, MazeNode> findNeighboursForAll(ImageFile imgObj) {
        HashMap<Coordinates, MazeNode> nodes = new HashMap<>();
        //Performing a one time pass over the maze to find all the nodes. Only look for the neighbours if it has been specified
        for (int height = 0; height < imgObj.getHeight(); height++) {
            for (int width = 0; width < imgObj.getWidth(); width++) {
                //Don't make a node unless the square is white
                if (imgObj.isWhite(width, height)) {
                    if (height == 0) {
                        nodes.put(new Coordinates(width, height), new MazeNode(width, height));

                        //Find the neighbours
                        ImageManipulation.findNeighboursForSingleOnLoad(nodes, new Coordinates(width, height), imgObj);

                    } else if (height == imgObj.getHeight() - 1) {
                        nodes.put(new Coordinates(width, height), new MazeNode(width, height));

                        //Find the neighbours
                        ImageManipulation.findNeighboursForSingleOnLoad(nodes, new Coordinates(width, height), imgObj);

                        //marking dead end nodes
                    } else if (ImageManipulation.isDeadEnd(imgObj, width, height)) {
                        nodes.put(new Coordinates(width, height), new MazeNode(width, height));

                        //Find the neighbours
                        ImageManipulation.findNeighboursForSingleOnLoad(nodes, new Coordinates(width, height), imgObj);

                        //Marking nodes at junctions
                    } else if (ImageManipulation.isJunction(imgObj, width, height)) {
                        nodes.put(new Coordinates(width, height), new MazeNode(width, height));

                        //Find the neighbours
                        ImageManipulation.findNeighboursForSingleOnLoad(nodes, new Coordinates(width, height), imgObj);

                        //Marking pixels on corner junctions
                    } else if (ImageManipulation.getAdjacentWhite(imgObj, width, height) == 2 && !ImageManipulation.directOpposite(imgObj, width, height)) {
                        nodes.put(new Coordinates(width, height), new MazeNode(width, height));

                        //Find the neighbours
                        ImageManipulation.findNeighboursForSingleOnLoad(nodes, new Coordinates(width, height), imgObj);
                    }
                }
            }
            //For debugging
            if (height % 2000 == 0) {
                System.out.println("Scanned " + height * imgObj.getWidth() + " of " + imgObj.getHeight() * imgObj.getWidth() + " pixels");
            }
        }

        return nodes;
    }



    /**
     * Look for the neighbours of one particular node, assuming that all node locations are known
     * This method takes a set of Coordinates of a known node and looks for neighbours.
     * The program goes left to right, top to bottom, so this will search to the left and top of the position
     */
    public static void findNeighboursForSingleOnLoad(HashMap<Coordinates, MazeNode> nodes, Coordinates currentLocation, ImageFile imgObj) {
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
     * Find the neighbours without looking at every node in the list
     * @param imgObj the array
     * @param nodes set of nodes already in the maze
     * @return set of nodes
     */
    public static Set<Coordinates> findNeighboursForSingleSolveTime(ImageFile imgObj, HashMap<Coordinates, MazeNode> nodes, int x, int y) {
        Set<Coordinates> neighbours = new HashSet<>();
        //Make a node at the current position
        if (!nodes.containsKey(new Coordinates(x, y))) nodes.put(new Coordinates(x, y), new MazeNode(x, y));

        //Look left
        if (imgObj.isWhite(x - 1, y)) {
            for (int width = x - 1; width > -1; width--) {
                //marking dead end nodes
                if (isDeadEnd(imgObj, width, y)) {
                    neighbours.add(new Coordinates(width, y));
                    break;

                    //Marking nodes at junctions
                } else if (isJunction(imgObj, width, y)) {
                    neighbours.add(new Coordinates(width, y));
                    break;

                    //Marking pixels on corner junctions
                } else if (getAdjacentWhite(imgObj, width, y) == 2 && !directOpposite(imgObj, width, y)) {
                    neighbours.add(new Coordinates(width, y));
                    break;
                }
            }
        }

        //Look Right
        if (imgObj.isWhite(x + 1, y)) {
            for (int width = x + 1; width < imgObj.getWidth(); width++) {
                //marking dead end nodes
                if (isDeadEnd(imgObj, width, y)) {
                    neighbours.add(new Coordinates(width, y));
                    break;

                    //Marking nodes at junctions
                } else if (isJunction(imgObj, width, y)) {
                    neighbours.add(new Coordinates(width, y));
                    break;

                    //Marking pixels on corner junctions
                } else if (getAdjacentWhite(imgObj, width, y) == 2 && !directOpposite(imgObj, width, y)) {
                    neighbours.add(new Coordinates(width, y));
                    break;
                }
            }
        }

        //Look up
        if (y > 0 && imgObj.isWhite(x, y - 1)) {
            for (int height = y - 1; height > -1; height--) {
                //marking dead end nodes
                if (isDeadEnd(imgObj, x, height)) {
                    neighbours.add(new Coordinates(x, height));
                    break;

                    //Marking nodes at junctions
                } else if (isJunction(imgObj, x, height)) {
                    neighbours.add(new Coordinates(x, height));
                    break;

                    //Marking pixels on corner junctions
                } else if (getAdjacentWhite(imgObj, x, height) == 2 && !directOpposite(imgObj, x, height)) {
                    neighbours.add(new Coordinates(x, height));
                    break;
                }
            }
        }

        //Look down
        if (y < imgObj.getHeight() && imgObj.isWhite(x, y + 1)) {
            for (int height = y + 1; height < imgObj.getHeight(); height++) {
                //The destination node
                if (height == imgObj.getHeight() - 1) {
                    neighbours.add(new Coordinates(x, height));
                    break;

                    //marking dead end nodes
                } else if (isDeadEnd(imgObj, x, height)) {
                    neighbours.add(new Coordinates(x, height));
                    break;

                    //Marking nodes at junctions
                } else if (isJunction(imgObj, x, height)) {
                    neighbours.add(new Coordinates(x, height));
                    break;

                    //Marking pixels on corner junctions
                } else if (getAdjacentWhite(imgObj, x, height) == 2 && !directOpposite(imgObj, x, height)) {
                    neighbours.add(new Coordinates(x, height));
                    break;
                }
            }
        }


        return neighbours;
    }


    /**
     * Returns true if two opposite squares are white
     */

    public static boolean directOpposite(ImageFile imjObj, int width, int height) {
        return (imjObj.isWhite(width - 1, height) && imjObj.isWhite(width + 1, height)) ||
                (imjObj.isWhite(width, height - 1)&& imjObj.isWhite(width, height + 1));


//        return (getColour(imgFile, width - 1, height) != 0 && getColour(imgFile, width + 1, height) != 0) ||
//                (getColour(imgFile, width, height - 1) != 0 && getColour(imgFile, width, height + 1) != 0);
    }

    /**
     * Gets and returns the number of adjacent white squares
     */
    public static int getAdjacentWhite(ImageFile imgObj, int width, int height) {
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
    public static boolean isJunction(ImageFile imgObj, int width, int height) {
        int whiteSides = getAdjacentWhite(imgObj, width, height);

        return whiteSides > 2;
    }

    /**
     * Checks to see if this part of the maze is a dead end.
     * Returns true if it is.
     */
    public static boolean isDeadEnd(ImageFile imgObj, int width, int height) {
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
     * Strips the image type then adds the 'solved' suffix
     */
    private static String insertSuffix(String filePath, String searchType) {
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
    private static void saveImage(BufferedImage img, String filePath, String searchType) {
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
}
