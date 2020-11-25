package Application.Solve;

import Utility.Node;

import java.util.Stack;

/**
 * Solve the maze, Depth first
 */
public class DepthFirst {

    public void solve(SolveAlgorithm solve) {
        System.out.println("Solving depth first");

        Stack<Node> queue = new Stack<>();

        //Add the first node to the stack
        queue.push(solve.entry);

        Node parent = null;

        while (!queue.isEmpty()) {
            Node currentNode = queue.pop();

            //Add all of the child nodes to the queue
            for (Node neighbour: currentNode.getNeighbours()) {
                if (!neighbour.isVisited()) {
                    queue.push(neighbour);
                }
            }

            currentNode.visit();

            //Check if this node is the end
            if (currentNode.equals(solve.exit)) break;

            if (parent == null) parent = currentNode;
            else currentNode.setParent(parent);

            parent = currentNode;

        }
    }
}
