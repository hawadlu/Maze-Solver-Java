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
    //todo change so that the red line can be painted
    public static ImageFile drawImage(ImageFile imgObj, ArrayList<MazeNode> nodes, MazeNode entry, HashSet<Segment> segments, ArrayList<APNode> artPoints) {
        imgObj.initSolvedArr();

        for (int height = 0; height < imgObj.getHeight(); height++) {
            for (int width = 0; width < imgObj.getWidth(); width++) {
                if (imgObj.isWhite(width, height)) {
                    imgObj.setRGB(width, height, (byte) 1);
                } else {
                    imgObj.setRGB(width, height, (byte) 0);
                }
            }
        }

        //Colour the entry
        if (entry != null) imgObj.setRGB(entry.getX(), entry.getY(), (byte) 2);

        //Draw the msp
        if (segments != null) {
            for (Segment segment : segments) {
                Coordinates start = segment.nodeStart.location;
                Coordinates end = segment.nodeEnd.location;
                draw(imgObj, start.x, start.y, end.x, end.y, (byte) 3);
            }
        }
        
        //Draw the path
        if (nodes != null) {
            while (nodes.size() > 1) {
                MazeNode start = nodes.remove(0);
                MazeNode end = nodes.get(0);

                draw(imgObj, start.getX(), start.getY(), end.getX(), end.getY(), (byte) 2);
            }
        }

        //Draw the articulation points
        if (artPoints != null) {
            for (APNode apNode: artPoints) {
                imgObj.setRGB(apNode.location.x, apNode.location.y, (byte) 4);
            }
        }

        return imgObj;
    }

    /**
     * Draw the segments
     * @param imgObj the image
     * @param segment the segment
     */
    private static void drawSegment(ImageFile imgObj, Segment segment) {
        MSTNode start = segment.nodeStart;
        MSTNode end = segment.nodeEnd;
    }

    /**
     * Draw the solved path in ren
     * @param imgObj the image
     */
    private static void draw(ImageFile imgObj, int startX, int startY, int endX, int endY, byte col) {
        int y, x;

        imgObj.setRGB(startX, startY, col);
        imgObj.setRGB(endX, endY, col);

        //Drawing down
        if (startY < endY) {
            y = startY + 1;
            while (y < endY) {
                imgObj.setRGB(startX, y, col);
                y += 1;
            }

            //Drawing up
        } else if (startY > endY) {
            y = startY;
            while (y > endY) {
                imgObj.setRGB(startX, y, col);
                y-=1;
            }

            //Drawing right
        } else if (startX < endX) {
            x = startX;
            while (x < endX) {
                imgObj.setRGB(x, startY, col);
                x+=1;
            }

            //Drawing left
        } else if (startX > endX) {
            x = startX;
            while (x > endX) {
                imgObj.setRGB(x, startY, col);
                x-=1;
            }
        }
    }


    /**
     * Perform a one time pass over the image, creating and linking all the nodes
     * @param imgObj the image
     * @return the number of nodes
     */
    public static HashMap<Coordinates, MazeNode> findNeighboursForAll(ImageFile imgObj) {
        HashMap<Coordinates, MazeNode> nodes = new HashMap<>();
        //Performing a one time pass over the maze to find all the nodes.
        for (int height = 0; height < imgObj.getHeight(); height++) {
            for (int width = 0; width < imgObj.getWidth(); width++) {
                //Don't make a node unless the square is white
                if (imgObj.isWhite(width, height)) {
                    if (height == 0 || height == imgObj.getHeight() - 1 || width == 0 || width == imgObj.getWidth() - 1) {
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
     * Saves solved images in the images folder
     */
    public static void saveImage(ImageFile img, String fileName) {
        //Saving the image
        try {
            ImageIO.write(img.makeImage(), "png", new File(fileName));
            System.out.println("Image saved as " + fileName);
        } catch (Exception e) {
            System.out.println("Unable to save image: " + e);
        }
    }
}
