package Utility.Image;

import Application.Application;
import Application.Solve.SolveAlgorithm;
import Utility.Location;
import Utility.Node;

import java.util.ArrayList;
import java.util.HashMap;

public class ImageProcessor {
    static Application application;
    SolveAlgorithm solve;

    public ImageProcessor(Application application) {
        this.application = application;
    }

    /**
     * @param solve The object that contains the info tht will be used for solving.
     */
    public void setSolve(SolveAlgorithm solve) {
        this.solve = solve;
    }


    /**
     * Take the maze image and scan it for all the nodes
     */
    public HashMap<Location, Node> scanAll() {
        HashMap<Location, Node> nodes = new HashMap<>();
        Boolean[][] imgArr = application.getImageFile().getArray();

        for (int height = 0; height < imgArr.length; height++) {
            for (int width = 0; width < imgArr[0].length; width++) {
                Location currentLocation = new Location(width, height);
                if (isNode(currentLocation, imgArr)) {
                    //Create a node and look for the neighbours
                    Node newNode = new Node(currentLocation);
                    neighbourSearch(newNode, nodes, imgArr);
                    nodes.put(currentLocation, newNode);
                }
            }
        }

        return nodes;
    }

    /**
     * Look for neighbours in the map. Only need to look left and up.
     * @param node the node to use to search for neighbours.
     * @param nodes the current nodes
     * @param imgArr the image array
     */
    private static void neighbourSearch(Node node, HashMap<Location, Node> nodes, Boolean[][] imgArr) {
        //Look left until a black node is found
        for (int width = node.getLocation().x; width >= 0; width--) {
            if (nodes.containsKey(new Location(width, node.getLocation().y))) {
                //Add both as neighbours
                Node neighbour = nodes.get(new Location(width, node.getLocation().y));
                neighbour.addNeighbour(node);
                node.addNeighbour(neighbour);
                break;
            } else if (imgArr[node.getLocation().y][width]) break;
        }
    }

    /**
     * Take a node in the maze and look for all its neighbours
     */
    private HashMap<Location, Node> scanAll(Node currentNode) {
        HashMap<Location, Node> nodes = new HashMap<>();
        Boolean[][] imgArr = application.getImageFile().getArray();
        return nodes;
    }

    private boolean isNode(Location location, Boolean[][] imgArr) {
        //Check if it is a white square
        if (!imgArr[location.y][location.x]) return false;

        //If this is on one of the edges
        if (location.y == 0 || location.y == imgArr.length - 1) {
            solve.addExit(location);
            return true;
        } else if (location.x == 0 || location.x == imgArr[0].length - 1) {
            solve.addExit(location);
            return true;
        }

        int whiteNeighbours = numWhiteNeighbours(location, imgArr);

        //Check if this is a dead end
        if (whiteNeighbours == 1) return true;

        //Check if this is a junction
        if (whiteNeighbours >= 3) return true;

        //Check if this is a corner
        return whiteNeighbours == 2 && !oppositeBlack(location, imgArr);
    }

    /**
     * Check if the location has black squares that are opposite
     * @param location the current location
     * @param imgArr the image array
     * @return
     */
    private boolean oppositeBlack(Location location, Boolean[][] imgArr) {
        if (!imgArr[location.y - 1][location.x] && !imgArr[location.y + 1][location.x]) return true;
        else return !imgArr[location.y][location.x - 1] && !imgArr[location.y][location.x + 1];
    }

    /**
     * Count the number of white tiles surrounding this one
     * @param location the current location
     * @param imgArr the image array
     * @return the count of neighbours
     */
    private int numWhiteNeighbours(Location location, Boolean[][] imgArr) {
        int count = 0;
        if (imgArr[location.y - 1][location.x]) count++;
        if (imgArr[location.y + 1][location.x]) count++;
        if (imgArr[location.y][location.x - 1]) count++;
        if (imgArr[location.y][location.x + 1]) count++;
        return count;
    }
}
