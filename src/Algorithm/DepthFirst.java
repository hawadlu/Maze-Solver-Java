package Algorithm;

import Utility.Exceptions.SolveFailure;
import Utility.Node;

import java.util.Stack;

/**
 * Solve the maze, Depth first
 */
public class DepthFirst {

    public void solve(SolveAlgorithm solve) {
        System.out.println("Solving depth first");

        Node start = solve.entry;
        Node destination = solve.exit;

        Node parent = null;
        Stack<Node> toProcess = new Stack<>();
        start.visit();
        toProcess.push(start);

        while (!toProcess.isEmpty()) {

            parent = toProcess.pop();
            parent.visit();

            if (parent.equals(destination)) break;

            //If the neighbours are not already loaded, load them.
            if (!solve.scanAll) solve.findNeighbours(parent);

            //Add all the appropriate neighbours to the stack
            for (Node node: parent.getNeighbours()) {
                //Add the node to the queue
                if (!node.isVisited()) {
                    node.setParent(parent);
                    toProcess.push(node);
                }
            }

            //If the stack is empty at this point, solving failed
            if (toProcess.isEmpty()) try {
                throw new SolveFailure("The stack is empty");
            } catch (SolveFailure e) {
                e.printStackTrace();
            }
        }
    }
}
