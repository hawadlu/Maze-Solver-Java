import java.util.*;

public class MST {
    final HashSet<MSTNode> unvisited = new HashSet<>();
    final HashSet<Segment> mspEdges = new HashSet<>(); //the edges in the minimum spanning tree
    final HashSet<MSTNode> forest = new HashSet<>();
    HashMap<Coordinates, MSTNode> nodes = new HashMap<>();

    /**
     * Constructor used to initialise all of the nodes and edges
     * @param imgObj
     */
    MST(ImageFile imgObj) {
        //Performing a one time pass over the maze to find all the nodes.
        for (int height = 0; height < imgObj.getHeight(); height++) {
            for (int width = 0; width < imgObj.getWidth(); width++) {
                //Don't make a node unless the square is white
                if (imgObj.isWhite(width, height)) {
                    if (height == 0 || height == imgObj.getHeight() - 1 || width == 0 || width == imgObj.getWidth() - 1) {
                        nodes.put(new Coordinates(width, height), new MSTNode(new Coordinates(width, height)));
                        connectSegments(nodes, new Coordinates(width, height), imgObj);

                        //marking dead end nodes
                    } else if (ImageManipulation.isDeadEnd(imgObj, width, height)) {
                        nodes.put(new Coordinates(width, height), new MSTNode(new Coordinates(width, height)));
                        connectSegments(nodes, new Coordinates(width, height), imgObj);

                        //Marking nodes at junctions
                    } else if (ImageManipulation.isJunction(imgObj, width, height)) {
                        nodes.put(new Coordinates(width, height), new MSTNode(new Coordinates(width, height)));
                        connectSegments(nodes, new Coordinates(width, height), imgObj);

                        //Marking pixels on corner junctions
                    } else if (ImageManipulation.getAdjacentWhite(imgObj, width, height) == 2 && !ImageManipulation.directOpposite(imgObj, width, height)) {
                        nodes.put(new Coordinates(width, height), new MSTNode(new Coordinates(width, height)));
                        connectSegments(nodes, new Coordinates(width, height), imgObj);
                    }
                }
            }
        }
        System.out.println("Node scanning complete");
    }

    /**
     * Find a minimum spanning tree using kruskals algorithm
     */
    public HashSet<Segment> kruskalsAlgorithm() {
        //Make an arraylist of edges
        ArrayList<Segment> edges = new ArrayList<>(mspEdges);
        mspEdges.clear();
        forest.addAll(nodes.values());

        //Create the forest
//        for (MSTNode node: unvisited) {
//            forest.add(node);
//            node.parent = node;
//            node.depth = 0;
//
//            //Add the edges to the queue
//            edges.addAll(node.segments);
//        }

        //Sort the arraylist
        edges.sort(Comparator.comparingInt(segment -> segment.cost));

        System.out.println("Sorted edges");

        while (!edges.isEmpty() || forest.size() == 1) {
            Segment currentSegment = edges.remove(0);

            if (union(currentSegment.nodeStart, currentSegment.nodeEnd)) {
                mspEdges.add(currentSegment);
            }

        }
        System.out.println("Found MSP with " + mspEdges.size() + " edges");

        return mspEdges;
    }

    /**
     * Merge two nodes into the same tree
     * @param start the first node
     * @param end the second node
     * @return true/false was the union successful?
     */
    private boolean union(MSTNode start, MSTNode end) {
        MSTNode startRoot = findRoot(start);
        MSTNode endRoot = findRoot(end);

        //x and y are in the same tree
        if (startRoot == endRoot) {
            return false;
        } else {
            //Merge the trees
            if (startRoot.depth < endRoot.depth) {
                startRoot.parent = end;
                forest.remove(start);
            } else {
                endRoot.parent = start;
                forest.remove(end);

                if (startRoot.depth == endRoot.depth) {
                    startRoot.depth++;
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
    private MSTNode findRoot(MSTNode start) {
        if (start.parent == start) {
            return start;
        } else {
            return findRoot(start.parent);
        }
    }

    /**
     * Look above and to the left of this node for connecting segments
     */
    public void connectSegments(HashMap<Coordinates, MSTNode> nodes, Coordinates currentLocation, ImageFile imgObj) {
        //Look up until a node or a black square is encountered
        for (int y = currentLocation.y - 1; y > -1; y--) {
            //Break if a black pixel is detected
            if (!imgObj.isWhite(currentLocation.x, y)) {
                break;
                //Break if a node is located
            } else if (nodes.containsKey(new Coordinates(currentLocation.x, y))) {
                //Add the edge if possible
                mspEdges.add(new Segment(nodes.get(currentLocation), nodes.get(new Coordinates(currentLocation.x, y)), currentLocation.y - y - 1));
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
                //Add the edge if possible
                mspEdges.add(new Segment(nodes.get(currentLocation), nodes.get(new Coordinates(x, currentLocation.y)), currentLocation.x - x - 1));
                break;
            }
        }
    }
}
