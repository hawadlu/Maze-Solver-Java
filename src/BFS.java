import java.util.*;

/**
 * This class solves the maze breadth first.
 * It takes a start node, end node and and returns a path between them
 */
//fixme make this work with finding neighbours on the fly
class BFS extends Algorithms{
    /**
     * Constructor
     */
    public BFS() {}

    /**
     * Solves the maze node.
     * Uses iteration to avoid stack overflow error
     */
    public void solve(ImageFile imgObj, MazeNode start, MazeNode destination, HashMap<Coordinates, MazeNode> nodes) {
        MazeNode parent = null;
        Queue<MazeNode> toProcess = new ArrayDeque<>();
        start.visit();
        toProcess.offer(start);


        if (nodes.size() > 2) {
            while (!toProcess.isEmpty()) {
                parent = toProcess.peek();
                parent.visit(); //set visited
                if (parent.equals(destination)) {
                    break;
                } else {

                    //Add all children
                    for (Coordinates location : Objects.requireNonNull(toProcess.poll()).getNeighbours()) {
                        //Get the node

                        MazeNode node = nodes.get(new Coordinates(location.x, location.y));
                        if (!node.isVisited()) {
                            node.setParent(parent);
                            toProcess.offer(node);
                        }
                    }
                }
            }
        } else {
            while (!toProcess.isEmpty()) {
                parent = toProcess.peek();
                parent.visit(); //set visited
                if (parent.equals(destination)) {
                    break;
                } else {

                    //Add all children
                    MazeNode current = toProcess.poll();
                    for (Coordinates location : ImageManipulation.findNeighboursForSingleSolveTime(imgObj, nodes, current.getX(), current.getY())) {                        //Get the node

                        MazeNode node = nodes.get(new Coordinates(location.x, location.y));

                        //If there is no node here, make one
                        if (node == null) {
                            node = new MazeNode(location.x, location.y);
                            nodes.put(new Coordinates(location.x, location.y), node);
                        }

                        if (!node.isVisited()) {
                            node.setParent(parent);
                            toProcess.offer(node);
                        }
                    }
                }
            }
        }

        //retrace from the parent to the start
        backtrack(parent);
    }
}
