package Algorithm;

import Utility.Exceptions.SolveFailure;
import Utility.Node;

import java.util.Objects;
import java.util.Stack;

/**
 * Solve the maze, depth first
 */
public class DepthFirst extends AlgorithmRunner {

    /**
     * Do a depth first search.
     * Start at each end to speed up
     * @param solve the solve object
     */
    public void solve(SolveAlgorithm solve, Boolean multiThreading) {
        System.out.println("Solving depth first");

        AlgorithmWorker workerOne = new DFSWorker(solve, solve.entry, solve.exit, this, "t1");
        AlgorithmWorker workerTwo = new DFSWorker(solve, solve.exit, solve.entry, this, "t2");

        solve.startThreads(workerOne, workerTwo, multiThreading);
    }
}

/**
 * Allows DFS to be multi threaded
 */
class DFSWorker extends AlgorithmWorker {
    public DFSWorker(SolveAlgorithm solve, Node start, Node destination, AlgorithmRunner runner, String threadId) {
        super(solve, start, destination, runner, threadId);
    }

    @Override
    public void run() {
        System.out.println("Thread: " + threadId + "\nstart: " + start + "\ndestination: " + destination + "\n");


        Node parent;
        Stack<Node> toProcess = new Stack<>();
        start.visit(this);
        toProcess.push(start);

        while (!toProcess.isEmpty() && !runner.done.get()) {

            parent = toProcess.pop();
            parent.visit(this);

            if (parent.equals(destination)) {
                System.out.println("Thread " + threadId + " is attempting to exit the loop");
                break;
            }

            //If the neighbours are not already loaded, load them.
            if (!solve.scanAll) solve.findNeighbours(parent, runner.multiThreading);

            //Add all the appropriate neighbours to the stack
            for (Node node : parent.getNeighbours()) {
                if (node.isVisited() == null) {
                    node.setParent(parent);
                    toProcess.push(node);
                    node.visit(this);
                } else if (node.isVisited().equals(other)) {
                    solve.addJoinerNodes(parent, node);
                    runner.done.set(true);
                }
            }

            //If the stack is empty at this point, solving failed
            if (toProcess.isEmpty() && !runner.done.get()) {
                try {
                    throw new SolveFailure("The stack is empty on thread " + threadId);
                } catch (SolveFailure e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("Thread " + threadId + " has exited the loop");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DFSWorker worker = (DFSWorker) o;
        return destination.equals(worker.destination) &&
                start.equals(worker.start) &&
                threadId.equals(worker.threadId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(destination, start, threadId);
    }

    @Override
    public String toString() {
        return threadId;
    }
}
