import java.util.*;

public class ArticulationPoints {
    HashMap<Coordinates, APNode > nodes = new HashMap<>();
    final HashSet<APNode> articulationPoints = new HashSet<>();
    final HashSet<APNode> unvisited = new HashSet<>();

    /**
     * Begins the search for the articulation points
     * @return boolean indicating a failed/successful search
     */
    public boolean findAps() {
        System.out.println("Looking for aps");

        unvisited.addAll(nodes.values());
        while (!unvisited.isEmpty()) {

            //Cast the map of nodes to an arraylist then pick a random node
            APNode parent = new ArrayList<>(unvisited).get(new Random().nextInt(unvisited.size()));

            //If this node has only one neighbour mark is as visited and ignore
            parent.nodeDepth = 0;
            parent.numSubtrees = 0;

            unvisited.remove(parent);
            int subtrees = 0;
            //Perform the search
            for (APNode child : parent.getNeighbours()) {
                unvisited.remove(child);
                if (child.nodeDepth == Double.POSITIVE_INFINITY) { //Is the neighbour visited
                    iterativeSearch(child, 1, parent);
                    subtrees++;
                }

                if (subtrees > 1) articulationPoints.add(parent);
            }
        }

        System.out.println("AP search complete. Found " + articulationPoints.size() + " articulation points");

        //Return a successful search
        return true;
    }

    /**
     * Get the articulation points
     * @return arraylist containing all of the articulation points.
     */
    public ArrayList<APNode> getArticulationPoints() {
        return new ArrayList<>(articulationPoints);
    }

    /**
     * Look for articulation points using a iterative algorithm
     * @param firstNode The first node to look at
     * @param depth The current depth
     * @param root essentially the node that came before this one.
     */
    public void iterativeSearch(APNode firstNode, double depth, APNode root) {
        Stack<APNode> toProcess = new Stack<>();
        //Put the first element on the stack
        toProcess.push(new APNode(firstNode, depth, root));

        while (!toProcess.isEmpty()) {
            APNode currentAPNode = toProcess.peek(); //The node currently being process

            //Variable declarations for the sake of simplicity
            APNode currentNode = currentAPNode.firstNode;
            APNode parentNode = currentAPNode.parent;

            unvisited.remove(currentNode);

            if (currentNode.nodeDepth == Double.POSITIVE_INFINITY) {
                //The first time visiting this node
                currentNode.nodeDepth = currentNode.reachBack = currentAPNode.nodeDepth;

                //Get all the neighbours except for the parent
                currentNode.children = new ArrayList<>(currentNode.getNeighbours());
                currentNode.children.remove(currentNode.getParent());
            } else if (!currentNode.children.isEmpty()) {
                APNode child = currentNode.children.remove(0);

                if (child.nodeDepth < Double.POSITIVE_INFINITY) {
                    //Direct alternative path
                    currentNode.reachBack = Math.min(child.nodeDepth, currentNode.reachBack);
                } else {
                    //Add the child to the stack because it is unvisited
                    toProcess.push(new APNode(child, currentAPNode.nodeDepth + 1, currentNode));
                }
            } else {
                if (currentNode != firstNode) {
                    parentNode.reachBack = Math.min(currentNode.reachBack, parentNode.reachBack);

                    //Add this node to the articulation points if it matches the requirements
                    if (currentNode.reachBack >= parentNode.nodeDepth) {
                        articulationPoints.add(parentNode);
                    }
                }
                toProcess.pop();
            }
        }
    }

    /**
     * Perform a one time pass over the image, creating and linking all the nodes
     * @param imgObj the image
     * @return the number of nodes
     */
    public HashMap<Coordinates, APNode> findNeighboursForAll(ImageFile imgObj) {
        //Performing a one time pass over the maze to find all the nodes.
        for (int height = 0; height < imgObj.getHeight(); height++) {
            for (int width = 0; width < imgObj.getWidth(); width++) {
                //Don't make a node unless the square is white
                if (imgObj.isWhite(width, height)) {
                    if (height == 0 || height == imgObj.getHeight() - 1 || width == 0 || width == imgObj.getWidth() - 1) {
                        nodes.put(new Coordinates(width, height), new APNode(new Coordinates(width, height)));

                        //Find the neighbours
                        findNeighboursForSingleOnLoad(nodes, new Coordinates(width, height), imgObj);

                        //marking dead end nodes
                    } else if (ImageManipulation.isDeadEnd(imgObj, width, height)) {
                        nodes.put(new Coordinates(width, height), new APNode(new Coordinates(width, height)));

                        //Find the neighbours
                        findNeighboursForSingleOnLoad(nodes, new Coordinates(width, height), imgObj);

                        //Marking nodes at junctions
                    } else if (ImageManipulation.isJunction(imgObj, width, height)) {
                        nodes.put(new Coordinates(width, height), new APNode(new Coordinates(width, height)));

                        //Find the neighbours
                        findNeighboursForSingleOnLoad(nodes, new Coordinates(width, height), imgObj);

                        //Marking pixels on corner junctions
                    } else if (ImageManipulation.getAdjacentWhite(imgObj, width, height) == 2 && !ImageManipulation.directOpposite(imgObj, width, height)) {
                        nodes.put(new Coordinates(width, height), new APNode(new Coordinates(width, height)));

                        //Find the neighbours
                        findNeighboursForSingleOnLoad(nodes, new Coordinates(width, height), imgObj);
                    }
                }
            }
        }
        System.out.println("Completed node scan");
        return nodes;
    }



    /**
     * Look for the neighbours of one particular node, assuming that all node locations are known
     * This method takes a set of Coordinates of a known node and looks for neighbours.
     * The program goes left to right, top to bottom, so this will search to the left and top of the position
     */
    public static void findNeighboursForSingleOnLoad(HashMap<Coordinates, APNode> nodes, Coordinates currentLocation, ImageFile imgObj) {
        //Look up until a node or a black square is encountered
        for (int y = currentLocation.y - 1; y > -1; y--) {
            //Break if a black pixel is detected
            if (!imgObj.isWhite(currentLocation.x, y)) {
                break;
                //Break if a node is located
            } else if (nodes.containsKey(new Coordinates(currentLocation.x, y))) {
                //If a node is found, mark them as neighbours
                nodes.get(currentLocation).addNeighbour(nodes.get(new Coordinates(currentLocation.x, y)));
                nodes.get(new Coordinates(currentLocation.x, y)).addNeighbour(nodes.get(currentLocation));
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
                nodes.get(currentLocation).addNeighbour(nodes.get(new Coordinates(x, currentLocation.y)));
                nodes.get(new Coordinates(x, currentLocation.y)).addNeighbour(nodes.get(currentLocation));
                break;
            }
        }
    }
}
