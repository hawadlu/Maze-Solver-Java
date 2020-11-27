package Algorithm;

import Utility.Exceptions.SolveFailure;
import Utility.Exceptions.ThreadFailure;
import Utility.Node;

import java.util.Objects;
import java.util.Stack;

/**
 * Solve the maze, Depth first
 */
public class DepthFirst {

    /**
     * Do a depth first search.
     * Start at each end to speed up
     * @param solve the solve object
     */
    public void solve(SolveAlgorithm solve) {
        System.out.println("Solving depth first");

        Node start = solve.entry;
        Node destination = solve.exit;

        DFSWorker workerOne = new DFSWorker(solve, start, destination, "t1");
        DFSWorker workerTwo = new DFSWorker(solve, destination, start, "t2");
        workerOne.setOther(workerTwo);
        workerTwo.setOther(workerOne);

        workerOne.start();

        //Only create a second worker if the maze is big enough
        if (solve.application.getMazeDimensions().width * solve.application.getMazeDimensions().height > 100 * 100) workerTwo.start();
    }
}

/**
 * Allows DFS to be multi threaded
 */
class DFSWorker extends SolveWorker {
    public DFSWorker(SolveAlgorithm solve, Node start, Node destination, String threadName) {
        super(solve, start, destination, threadName);
    }

    public void setOther(SolveWorker other) {
        this.other = other;
    }

    @Override
    public void runSolve() {
        Node parent = null;
        Stack<Node> toProcess = new Stack<>();
        start.visit();
        toProcess.push(start);

        while (!toProcess.isEmpty()) {
            System.out.println(threadName + ": process size: " + toProcess.size() + " " + toProcess);

            parent = toProcess.pop();
            parent.visit();
            this.addNode(parent);

            if (parent.equals(destination)) break;

            //If the neighbours are not already loaded, load them.
            if (!solve.scanAll) solve.findNeighbours(parent);

            //Add all the appropriate neighbours to the stack
            for (Node node: parent.getNeighbours()) {
                //Add the node to the queue
                if (!node.isVisited()) {
                    node.setParent(parent);
                    toProcess.push(node);
                } else if (other.visited.contains(node)) {
                    node.setParent(parent);

                    System.out.println("Solved. halting thread execution.");
                    other.stop();
                    this.stop();
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

    @Override
    public String toString() {
        return "Thread: DFSWorker " + threadName;
    }
}
