package Algorithm;

import Utility.Exceptions.SolveFailure;
import Utility.Location;
import Utility.Node;
import org.w3c.dom.css.CSSStyleDeclaration;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Solve the maze, Depth first
 */
public class DepthFirst {
    int threadCount = 0;
    AtomicBoolean done = new AtomicBoolean(false);
    Boolean multiThreading = false;

    /**
     * Do a depth first search.
     * Start at each end to speed up
     * @param solve the solve object
     */
    public void solve(SolveAlgorithm solve, Boolean multiThreading) {
        System.out.println("Solving depth first");

        Node start = solve.entry;
        Node destination = solve.exit;

        DFSWorker workerOne = new DFSWorker(solve, start, destination, this, "t1");
        DFSWorker workerTwo = new DFSWorker(solve, destination, start, this, "t2");
        workerOne.other = workerTwo;
        workerTwo.other = workerOne;
        workerOne.start();

        //Only start the second worker in the case that the maze is large enough
        if (multiThreading) {
            workerTwo.start();
        }

        //Wait for the worker to finish
        try {
            workerOne.join();
            workerTwo.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Threads have finished execution. There were a total of " + threadCount);
    }
}

/**
 * Allows DFS to be multi threaded
 */
class DFSWorker extends Thread {
    SolveAlgorithm solve;
    Node destination, start;
    String threadId;
    DepthFirst dfs;
    DFSWorker other;
    ArrayList<Stack<Node>> stacks = new ArrayList<>(); //todo delete me

    public DFSWorker(SolveAlgorithm solve, Node start, Node destination, DepthFirst dfs, String threadId) {
        this.solve = solve;
        this.start = start;
        this.destination = destination;
        this.threadId = threadId;
        this.dfs = dfs;
    }

    @Override
    public void run() {
        System.out.println("Thread: " + threadId + "\nstart: " + start + "\ndestination: " + destination + "\n");


        Node parent = null;
        Stack<Node> toProcess = new Stack<>();
        start.visit(this);
        toProcess.push(start);

        while (!toProcess.isEmpty() && !dfs.done.get()) {
//            System.out.println(threadId + " 1");

            parent = toProcess.pop();
            parent.visit(this);

            if (parent.equals(destination)) {
                System.out.println("Thread " + threadId + " is attempting to exit the loop");
                break;
            }

//            System.out.println(parent);

//            if (parent.getLocation().equals(new Location(3, 1))) {
//                System.out.println("Location");
//            }

            //todo make for on solves only
            //If the neighbours are not already loaded, load them.
            if (!solve.scanAll) solve.findNeighbours(parent, dfs.multiThreading);

            //Add all the appropriate neighbours to the stack
            for (Node node : parent.getNeighbours()) {
                if (node.isVisited() == null) {
                    node.setParent(parent);
                    toProcess.push(node);
                    node.visit(this);
                } else if (node.isVisited().equals(other)) {
                    solve.addJoinerNodes(parent, node);
                    dfs.done.set(true);
                }
            }

            //If the stack is empty at this point, solving failed
            if (toProcess.isEmpty() && !dfs.done.get()) {
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
