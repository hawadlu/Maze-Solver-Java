package Utility.Image;

import Application.Application;
import Utility.Colours;
import Utility.Location;
import Utility.Node;
import Utility.Colours.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class ImageProcessor {
    static Application application;
    ArrayList<Location> exits = new ArrayList<>();
    HashMap<Location, Node> nodes = new HashMap<>();

    public ImageProcessor(Application application) {
        this.application = application;
    }



    /**
     * Take the maze image and scan it for all the nodes.
     */
    public void scanAll() {
        nodes.clear();
        colEnum[][] imgArr = application.getImageFile().getArray();

        //Find the exits
        findExits();

        for (int height = 0; height < imgArr.length; height++) {
            for (int width = 0; width < imgArr[0].length; width++) {
                Location currentLocation = new Location(width, height);
                if (isNode(currentLocation)) {
                    //Create a node and look for the neighbours
                    Node newNode = new Node(currentLocation);
                    neighbourSearch(newNode, nodes);
                    nodes.put(currentLocation, newNode);
                }
            }
        }

        //Link the exits to the appropriate nodes
        for (Location location: exits) {
            neighbourSearch(nodes.get(location), nodes);
        }
    }

    /**
     * Take a node in the maze and look for all its neighbours
     */
    public void scanAll(Node currentNode) {
        //Look up
        for (int height = currentNode.getLocation().y - 1; height > -1; height--) {
            Location currentLocation = new Location(currentNode.getLocation().x, height);
            if (checkNodeExistsOnSolve(currentNode, currentLocation)) break;
        }

        //look down
        for (int height = currentNode.getLocation().y + 1; height < application.getImageFile().getArray().length; height++) {
            Location currentLocation = new Location(currentNode.getLocation().x, height);
            if (checkNodeExistsOnSolve(currentNode, currentLocation)) break;
        }

        //Look left
        for (int width = currentNode.getLocation().x - 1; width > -1; width--) {
            Location currentLocation = new Location(width, currentNode.getLocation().y);
            if (checkNodeExistsOnSolve(currentNode, currentLocation)) break;
        }

        //Look right
        for (int width = currentNode.getLocation().x + 1; width < application.getImageFile().getArray()[0].length; width++) {
            Location currentLocation = new Location(width, currentNode.getLocation().y);
            if (checkNodeExistsOnSolve(currentNode, currentLocation)) break;
        }
    }

    private boolean checkNodeExistsOnSolve(Node currentNode, Location currentLocation) {
        //If this is an exit add it
        if (exits.contains(currentLocation)) {
            Node neighbour = nodes.get(exits.get(exits.indexOf(currentLocation)));
            neighbour.addNeighbour(currentNode);
            currentNode.addNeighbour(neighbour);
            return true;
        } else if (isNode(currentLocation)) {
            Node newNode = new Node(currentLocation);
            nodes.put(currentLocation, newNode);
            newNode.addNeighbour(currentNode);
            currentNode.addNeighbour(newNode);
            return true;
        } else if (application.getImageFile().getArray()[currentLocation.y][currentLocation.x].equals(colEnum.BLACK)) {
            //This is a black square, break.
            return true;
        }
        return false;
    }

    /**
     * Locate the entry and exit
     */
    public void findExits() {
        colEnum[][] imgArray = application.getImageFile().getArray();

        //Look at the top and bottom rows
        int topY = 0, bottomY = imgArray.length - 1;
        for (int width = 0; width < imgArray[0].length; width++) {
            if (imgArray[topY][width] == colEnum.WHITE) {
                Location location = new Location(width, topY);
                exits.add(location);
                nodes.put(location, new Node(location));
            }

            if (imgArray[bottomY][width] == colEnum.WHITE) {
                Location location = new Location(width, bottomY);
                exits.add(location);
                nodes.put(location, new Node(location));
            }
        }

        //Look at the left and right columns
        for (int height = 0; height < imgArray.length; height++) {
            if (imgArray[height][0] == colEnum.WHITE) {
                Location location = new Location(0, height);
                exits.add(location);
                nodes.put(location, new Node(location));
            }
            if (imgArray[height][imgArray[0].length - 1] == colEnum.WHITE) {
                Location location = new Location(imgArray[0].length - 1, height);
                exits.add(location);
                nodes.put(location, new Node(location));
            }
        }
    }

    /**
     * Look for neighbours in the map. Only need to look left and up.
     * @param node the node to use to search for neighbours.
     * @param nodes the current nodes
     */
    private static void neighbourSearch(Node node, HashMap<Location, Node> nodes) {
        colEnum[][] imgArray = application.getImageFile().getArray();

        //Look left until a black node is found
        for (int width = node.getLocation().x - 1; width >= 0; width--) {
            if (nodes.containsKey(new Location(width, node.getLocation().y))) {
                //Add both as neighbours
                Node neighbour = nodes.get(new Location(width, node.getLocation().y));
                neighbour.addNeighbour(node);
                node.addNeighbour(neighbour);
                break;
            } else if (imgArray[node.getLocation().y][width] == colEnum.BLACK) break;
        }

        //look up until a black node is found
        for (int height = node.getLocation().y - 1; height >= 0; height--) {
            if (nodes.containsKey(new Location(node.getLocation().x, height))) {
                //Add both as neighbours
                Node neighbour = nodes.get(new Location(node.getLocation().x, height));
                neighbour.addNeighbour(node);
                node.addNeighbour(neighbour);
                break;
            } else if (imgArray[height][node.getLocation().x] == colEnum.BLACK) break;
        }
    }

    private boolean isNode(Location location) {
        colEnum[][] imgArray = application.getImageFile().getArray();


        //If this has already been marked as an exit, it does not need to be considered
        if (nodes.containsKey(location)) return false;

        //Check if it is a white square
        if (imgArray[location.y][location.x] != colEnum.WHITE) return false;

        int whiteNeighbours = numWhiteNeighbours(location);

        //Check if this is a dead end
        if (whiteNeighbours == 1) return true;

        //Check if this is a junction
        if (whiteNeighbours >= 3) return true;

        //Check if this is a corner
        return whiteNeighbours == 2 && !oppositeBlack(location);
    }

    /**
     * Check if the location has black squares that are opposite
     * @param location the current location
     * @return
     */
    private boolean oppositeBlack(Location location) {
        colEnum[][] imgArray = application.getImageFile().getArray();

        if (imgArray[location.y - 1][location.x] == colEnum.BLACK && imgArray[location.y + 1][location.x] == colEnum.BLACK) return true;
        else return imgArray[location.y][location.x - 1] == colEnum.BLACK && imgArray[location.y][location.x + 1] == colEnum.BLACK;
    }

    /**
     * Count the number of white tiles surrounding this one
     * @param location the current location
     * @return the count of neighbours
     */
    private int numWhiteNeighbours(Location location) {
        colEnum[][] imgArray = application.getImageFile().getArray();

        int count = 0;
        if (imgArray[location.y - 1][location.x] == colEnum.WHITE) count++;
        if (imgArray[location.y + 1][location.x] == colEnum.WHITE) count++;
        if (imgArray[location.y][location.x - 1] == colEnum.WHITE) count++;
        if (imgArray[location.y][location.x + 1] == colEnum.WHITE) count++;
        return count;
    }

    /**
     * @return The maze exits in the image
     */
    public ArrayList<Location> getExits() {
        return this.exits;
    }

    /**
     * @return the nodes.
     */
    public HashMap<Location, Node> getNodes() {
        return this.nodes;
    }
}
