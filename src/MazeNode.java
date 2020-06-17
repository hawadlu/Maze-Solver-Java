import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * This class contains each node in the maze. It has fields to store the position and neighbours.
 * The maximum size of this object is somewhere in the region of 114 bytes
 */

public class MazeNode implements Comparable<MazeNode>{
    private final int xPos;
    private final int yPos;
    private double pathCost = Double.POSITIVE_INFINITY;
    private double heuristicCost = 0.0; //Estimated cost from this node to the end
    private final Set<Coordinates> neighbours = new HashSet<>();//Set that stores the x any y of neighbours
    private Boolean isVisited = false;
    private MazeNode parent = null; //Used to track which node this was visited from

    /**
     * Make a new node
     */
    public MazeNode(int x, int y){
        xPos = x;
        yPos = y;
    }

    /**
     * Get the x and y values
     */
    public int getX(){
        return xPos;
    }

    public int getY() {
        return yPos;
    }

    /**
     * Get and set the neighbours
     */
    public void addNeighbour(Coordinates newCoords) {
        neighbours.add(newCoords);
    }

    public Set<Coordinates> getNeighbours() {
        return neighbours;
    }

    /**
     * Find the neighbours without looking at every node in the list
     * @param imgObj the array
     * @param nodes set of nodes already in the maze
     * @return set of nodes
     */
    public Set<Coordinates> getNeighbours(ImageFile imgObj, HashMap<Coordinates, MazeNode> nodes) {
        Set<Coordinates> neighbours = new HashSet<>();
        //Make a node at the current position
        if (!nodes.containsKey(new Coordinates(xPos, yPos))) nodes.put(new Coordinates(xPos, yPos), new MazeNode(xPos, yPos));

        int numNodes = 0;

        //Look left
        if (imgObj.isWhite(xPos - 1, yPos)) {
            for (int width = xPos - 1; width > -1; width--) {
                //marking dead end nodes
                if (isDeadEnd(imgObj, width, yPos)) {
                    neighbours.add(new Coordinates(width, yPos));
                    break;

                    //Marking nodes at junctions
                } else if (isJunction(imgObj, width, yPos)) {
                    neighbours.add(new Coordinates(width, yPos));
                    break;

                    //Marking pixels on corner junctions
                } else if (getAdjacentWhite(imgObj, width, yPos) == 2 && !directOpposite(imgObj, width, yPos)) {
                    neighbours.add(new Coordinates(width, yPos));
                    break;
                }
            }
        }

        //Look Right
        if (imgObj.isWhite(xPos + 1, yPos)) {
            for (int width = xPos + 1; width < imgObj.getWidth(); width++) {
                //marking dead end nodes
                if (isDeadEnd(imgObj, width, yPos)) {
                    neighbours.add(new Coordinates(width, yPos));
                    break;

                    //Marking nodes at junctions
                } else if (isJunction(imgObj, width, yPos)) {
                    neighbours.add(new Coordinates(width, yPos));
                    break;

                    //Marking pixels on corner junctions
                } else if (getAdjacentWhite(imgObj, width, yPos) == 2 && !directOpposite(imgObj, width, yPos)) {
                    neighbours.add(new Coordinates(width, yPos));
                    break;
                }
            }
        }

        //Look up
        if (yPos > 0 && imgObj.isWhite(xPos, yPos - 1)) {
            for (int height = yPos - 1; height > -1; height--) {
                //marking dead end nodes
                if (isDeadEnd(imgObj, xPos, height)) {
                    neighbours.add(new Coordinates(xPos, height));
                    break;

                    //Marking nodes at junctions
                } else if (isJunction(imgObj, xPos, height)) {
                    neighbours.add(new Coordinates(xPos, height));
                    break;

                    //Marking pixels on corner junctions
                } else if (getAdjacentWhite(imgObj, xPos, height) == 2 && !directOpposite(imgObj, xPos, height)) {
                    neighbours.add(new Coordinates(xPos, height));
                    break;
                }
            }
        }

        //Look down
        if (yPos < imgObj.getHeight() && imgObj.isWhite(xPos, yPos + 1)) {
            for (int height = yPos + 1; height < imgObj.getHeight(); height++) {
                //The destination node
                if (height == imgObj.getHeight() - 1) {
                    neighbours.add(new Coordinates(xPos, height));
                    break;

                    //marking dead end nodes
                } else if (isDeadEnd(imgObj, xPos, height)) {
                    neighbours.add(new Coordinates(xPos, height));
                    break;

                    //Marking nodes at junctions
                } else if (isJunction(imgObj, xPos, height)) {
                    neighbours.add(new Coordinates(xPos, height));
                    break;

                    //Marking pixels on corner junctions
                } else if (getAdjacentWhite(imgObj, xPos, height) == 2 && !directOpposite(imgObj, xPos, height)) {
                    neighbours.add(new Coordinates(xPos, height));
                    break;
                }
            }
        }


        return neighbours;
    }

    /**
     * Visit and un-visit
     */
    public boolean isVisited() {return isVisited; }
    public void visit() { isVisited = true; }
    //public void unVisit() { isVisited = false; }

    /**
     * Get and set the parent
     */
    public void setParent(MazeNode node) {
        parent = node;
    }

    public MazeNode getParent() {
        return parent;
    }

    /**
     * Get and set the cost
     */
    public double getPathCost() { return pathCost; }

    public double getHeuristicCost() {
        return heuristicCost;
    }

    public void setPathCost(double newCost) { pathCost = newCost; }
    public void setHeuristicCost(double newCost) { heuristicCost = newCost; }



    /**
     * toString
     */
    public String toString() {
        return "x: " + getX() + " y:" + getY() + " num neighbours: ";
    }

    /**
     * Comparator
     */
    public int compareTo(MazeNode nextNode) {
        return Double.compare(this.pathCost + this.heuristicCost, nextNode.pathCost + nextNode.heuristicCost);
    }


    /**
     * Returns true if two opposite squares are white
     */

    private boolean directOpposite(ImageFile imjObj, int width, int height) {
        if (width > 0 && width < imjObj.getWidth() && height > 0 && height < imjObj.getHeight()) {
            return (imjObj.isWhite(width - 1, height) && imjObj.isWhite(width + 1, height)) ||
                    (imjObj.isWhite(width, height - 1) && imjObj.isWhite(width, height + 1));
        }
        return false;


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



}