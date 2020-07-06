import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;

public class MSP {
    final HashSet<MSTNode> unvisited = new HashSet<>();
    final HashSet<Segment> mspEdges = new HashSet<>(); //the edges in the minimum spanning tree
    final HashSet<MSTNode> forest = new HashSet<>();
    /**
     * Constructor used to initialise all of the nodes and edges
     * @param imgObj
     */
    MSP(ImageFile imgObj) {
        HashMap<Coordinates, MSTNode> nodes = new HashMap<>();

        //Performing a one time pass over the maze to find all the nodes. Only look for the neighbours if it has been specified
        for (int height = 0; height < imgObj.getHeight(); height++) {
            for (int width = 0; width < imgObj.getWidth(); width++) {
                //Don't make a node unless the square is white
                if (imgObj.isWhite(width, height)) {
                    if (height == 0 || height == imgObj.getHeight() - 1 || width == 0 || width == imgObj.getWidth() - 1) {
                        nodes.put(new Coordinates(width, height), new MSTNode());

                        //Find the neighbours
                        findNeighboursForSingleOnLoad(nodes, new Coordinates(width, height), imgObj);

                        //marking dead end nodes
                    } else if (ImageManipulation.isDeadEnd(imgObj, width, height)) {
                        nodes.put(new Coordinates(width, height), new MSTNode());

                        //Find the neighbours
                        findNeighboursForSingleOnLoad(nodes, new Coordinates(width, height), imgObj);

                        //Marking nodes at junctions
                    } else if (ImageManipulation.isJunction(imgObj, width, height)) {
                        nodes.put(new Coordinates(width, height), new MSTNode());

                        //Find the neighbours
                        findNeighboursForSingleOnLoad(nodes, new Coordinates(width, height), imgObj);

                        //Marking pixels on corner junctions
                    } else if (ImageManipulation.getAdjacentWhite(imgObj, width, height) == 2 && !ImageManipulation.directOpposite(imgObj, width, height)) {
                        nodes.put(new Coordinates(width, height), new MSTNode());

                        //Find the neighbours
                        findNeighboursForSingleOnLoad(nodes, new Coordinates(width, height), imgObj);
                    }
                }
            }
            //For debugging
            if (height % 2000 == 0) {
                System.out.println("Scanned " + height * imgObj.getWidth() + " of " + imgObj.getHeight() * imgObj.getWidth() + " pixels");
            }
        }
    }

    /**
     * Find a minimum spanning tree using kruskals algorithm
     */
    public boolean kruskalsAlgorithm() {


        //Make an arraylist of edges
        ArrayList<Segment> edges = new ArrayList<>();

        //Create the forest
        for (MSTNode node: unvisited) {
            forest.add(node);
            node.parent = node;
            node.depth = 0;

            //Add the edges to the queue
            edges.addAll(node.segments);
        }

        //Sort the arraylist
        edges.sort(Comparator.comparingDouble(segment -> segment.length));

        System.out.println("Sorted edges");

        while (!edges.isEmpty() || forest.size() == 1) {
            Segment currentSegment = edges.remove(0);

            if (union(currentSegment.start, currentSegment.end)) {
                mspEdges.add(currentSegment);
            }

        }
        System.out.println("Found MSP with " + mspEdges.size() + " edges");

        return true;
    }

    /**
     * Merge two nodes into the same tree
     * @param start the first node
     * @param end the second node
     * @return true/false was the union successful?
     */
    private boolean union(Node start, Node end) {
        Node startRoot = findRoot(start);
        Node endRoot = findRoot(end);

        //x and y are in the same tree
        if (startRoot == endRoot) {
            return false;
        } else {
            //Merge the trees
            if (startRoot.nodeDepth < endRoot.nodeDepth) {
                startRoot.parent = end;
                forest.remove(start);
            } else {
                endRoot.parent = start;
                forest.remove(end);

                if (startRoot.nodeDepth == endRoot.nodeDepth) {
                    startRoot.nodeDepth++;
                }
            }
            return true;
        }
    }

    /**
     * Locate the root of a given tree
     * @param start the tree
     * @return the root node
     */
    private Node findRoot(Node start) {
        if (start.parent == start) {
            return start;
        } else {
            return findRoot(start.parent);
        }
    }


    /**
     * Check if the node at the other end of the segment is unvisted.
     * @param node the node to check
     * @param segment the associated segment
     * @return True if the node is not visited
     */
    public boolean connectsUnvisted(Node node, Segment segment) {
        if (segment.start == node) return unvisited.contains(segment.end);
        else return unvisited.contains(segment.start);
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
}
