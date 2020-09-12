package Image;

import ArticulationPoints.APNode;
import Location.Coordinates;
import Solve.MazeNode;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Class containing methods for image manipulation.
 * Has methods for looking for neighbours, loading and saving images.
 * NOTE: This class deals with both Image.ImageFile and BufferedImage
 */
public class ImageManipulation {
    /**
     * This method draws the solved maze and returns it
     */
    public static ImageFile drawImage(ImageFile imgObj, ArrayList<MazeNode> nodes, MazeNode entry, HashSet<Segment> segments, HashSet<APNode> artPoints) {
        imgObj.initSolvedArr();

        for (int height = 0; height < imgObj.getTrueHeight(); height++) {
            for (int width = 0; width < imgObj.getTrueWidth(); width++) {
                if (imgObj.isWhite(width, height)) {
                    imgObj.setRGB(width, height, new DrawColour(Color.red));
                } else {
                    imgObj.setRGB(width, height, (new DrawColour(Color.BLACK));
                }
            }
        }

        //Colour the entry
        if (entry != null) imgObj.setRGB(entry.getX(), entry.getY(), (new DrawColour(Color.BLUE));

        //Draw the msp
        if (segments != null) {
            for (Segment segment : segments) {
                Coordinates start = segment.nodeStart.getLocation();
                Coordinates end = segment.nodeEnd.getLocation();
                draw(imgObj, start.getX(), start.getY(), end.getX(), end.getY(), new DrawColour(Color.green));
            }
        }
        
        //Draw the path
        if (nodes != null) {
            while (nodes.size() > 1) {
                MazeNode start = nodes.remove(0);
                MazeNode end = nodes.get(0);

                draw(imgObj, start.getX(), start.getY(), end.getX(), end.getY(), new DrawColour(Color.red));
            }
        }

        //Draw the articulation points
        if (artPoints != null) {
            for (APNode apNode: artPoints) {
                imgObj.setRGB(apNode.getLocation().getX(), apNode.getLocation().getY(), new DrawColour(Color.green));
            }
        }

        return imgObj;
    }

    /**
     * Draw the solved path in ren
     * @param imgObj the image
     */
    public static void draw(ImageFile imgObj, int startX, int startY, int endX, int endY, DrawColour col) {
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
        for (int height = 0; height < imgObj.getTrueHeight(); height++) {
            for (int width = 0; width < imgObj.getTrueWidth(); width++) {
                //Don't make a node unless the square is white
                if (imgObj.isWhite(width, height)) {
                    if (height == 0 || height == imgObj.getTrueHeight() - 1 || width == 0 || width == imgObj.getTrueWidth() - 1) {
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
                    } else if (ImageManipulation.getAdjacentWhite(imgObj, width, height) == 2 && ImageManipulation.notDirectOpposite(imgObj, width, height)) {
                        nodes.put(new Coordinates(width, height), new MazeNode(width, height));

                        //Find the neighbours
                        ImageManipulation.findNeighboursForSingleOnLoad(nodes, new Coordinates(width, height), imgObj);
                    }
                }
            }
            //For debugging
            if (height % 2000 == 0) {
                System.out.println("Scanned " + height * imgObj.getTrueWidth() + " of " + imgObj.getTrueHeight() * imgObj.getTrueWidth() + " pixels");
            }
        }

        return nodes;
    }



    /**
     * Look for the neighbours of one particular node, assuming that all node locations are known
     * This method takes a set of Location.Coordinates of a known node and looks for neighbours.
     * The program goes left to right, top to bottom, so this will search to the left and top of the position
     */
    public static void findNeighboursForSingleOnLoad(HashMap<Coordinates, MazeNode> nodes, Coordinates currentLocation, ImageFile imgObj) {
        //Look up until a node or a black square is encountered
        for (int y = currentLocation.getY() - 1; y > -1; y--) {
            //Break if a black pixel is detected
            if (!imgObj.isWhite(currentLocation.getX(), y)) {
                break;
                //Break if a node is located
            } else if (nodes.containsKey(new Coordinates(currentLocation.getX(), y))) {
                //If a node is found, mark them as neighbours
                nodes.get(currentLocation).addNeighbour(new Coordinates(currentLocation.getX(), y));
                nodes.get(new Coordinates(currentLocation.getX(), y)).addNeighbour(currentLocation);
                break;
            }
        }

        //Look up left a node or a black square is encountered
        for (int x = currentLocation.getX() - 1; x > -1; x--) {
            //Break if a black pixel is detected
            if (!imgObj.isWhite(x, currentLocation.getY())) {
                break;
                //Break if a node is located
            } else if (nodes.containsKey(new Coordinates(x, currentLocation.getY()))) {
                //If a node is found, mark them as neighbours
                nodes.get(currentLocation).addNeighbour(new Coordinates(x, currentLocation.getY()));
                nodes.get(new Coordinates(x, currentLocation.getY())).addNeighbour(currentLocation);
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
        if (x > 0 && imgObj.isWhite(x - 1, y)) {
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
                } else if (getAdjacentWhite(imgObj, width, y) == 2 && notDirectOpposite(imgObj, width, y)) {
                    neighbours.add(new Coordinates(width, y));
                    break;

                    //Mark nodes at the corners
                } else if (width == 0) {
                        neighbours.add(new Coordinates(width, y));
                }
            }
        }

        //Look Right
        if (x < imgObj.getTrueWidth() - 1 && imgObj.isWhite(x + 1, y)) {
            for (int width = x + 1; width < imgObj.getTrueWidth(); width++) {
                //marking dead end nodes
                if (isDeadEnd(imgObj, width, y)) {
                    neighbours.add(new Coordinates(width, y));
                    break;

                    //Marking nodes at junctions
                } else if (isJunction(imgObj, width, y)) {
                    neighbours.add(new Coordinates(width, y));
                    break;

                    //Marking pixels on corner junctions
                } else if (getAdjacentWhite(imgObj, width, y) == 2 && notDirectOpposite(imgObj, width, y)) {
                    neighbours.add(new Coordinates(width, y));
                    break;

                    //If the node is at the edge mark it.
                } else if (width + 1 >= imgObj.getTrueWidth()) {
                    neighbours.add(new Coordinates(width,y));
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
                } else if (getAdjacentWhite(imgObj, x, height) == 2 && notDirectOpposite(imgObj, x, height)) {
                    neighbours.add(new Coordinates(x, height));
                    break;

                    //Mark nodes at the edge
                } else if (height == 0) {
                    neighbours.add(new Coordinates(x, height));
                    break;
                }
            }
        }

        //Look down
        if (y < imgObj.getTrueHeight() - 1 && imgObj.isWhite(x, y + 1)) {
            for (int height = y + 1; height < imgObj.getTrueHeight(); height++) {
                //The destination node
                if (height == imgObj.getTrueHeight() - 1) {
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
                } else if (getAdjacentWhite(imgObj, x, height) == 2 && notDirectOpposite(imgObj, x, height)) {
                    neighbours.add(new Coordinates(x, height));
                    break;

                } else if (height + 1 >= imgObj.getTrueHeight()) {
                    neighbours.add(new Coordinates(x, height));
                }
            }
        }
        return neighbours;
    }


    /**
     * Returns true if two opposite squares are white
     */
    public static boolean notDirectOpposite(ImageFile imjObj, int width, int height) {
        return (!imjObj.isWhite(width - 1, height) || !imjObj.isWhite(width + 1, height)) &&
                (!imjObj.isWhite(width, height - 1) || !imjObj.isWhite(width, height + 1));
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
        if ((width + 1 < imgObj.getTrueWidth()) && (imgObj.isWhite(width + 1, height))) {
            adjacent++;
        }

        //Looking up
        if ((height - 1 > -1) && (imgObj.isWhite(width, height - 1))) {
            adjacent++;
        }

        //Looking down
        if ((height + 1 < imgObj.getTrueHeight()) && (imgObj.isWhite(width, height + 1))) {
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
        if ((width + 1 < imgObj.getTrueWidth()) && (!imgObj.isWhite(width + 1, height))) {
            blackSides++;
        }
        if ((height - 1 > -1) && (!imgObj.isWhite(width, height - 1))) {
            blackSides++;
        }
        if ((height + 1 < imgObj.getTrueHeight()) && (!imgObj.isWhite(width, height + 1))) {
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
