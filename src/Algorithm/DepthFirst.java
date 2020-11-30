package Algorithm;

import Utility.Exceptions.SolveFailure;
import Utility.Node;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Solve the maze, Depth first
 */
public class DepthFirst {
    int threadCount = 0;
    boolean done = false;

    /**
     * Do a depth first search.
     * Start at each end to speed up
     * @param solve the solve object
     */
    public void solve(SolveAlgorithm solve) {
        System.out.println("Solving depth first");

        Node start = solve.entry;
        Node destination = solve.exit;

        DFSWorker worker = new DFSWorker(solve, start, destination, null, this);
        worker.start();

        //Wait for the worker to finish
        try {
            worker.join();
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
    DFSWorker parent;
    int threadId;
    DepthFirst dfs;
    ArrayList<DFSWorker> toProcess = new ArrayList<>();
    boolean stopped;
    final AtomicBoolean running = new AtomicBoolean(true);

    public DFSWorker(SolveAlgorithm solve, Node start, Node destination, DFSWorker parent, DepthFirst dfs) {
        this.solve = solve;
        this.start = start;
        this.destination = destination;
        this.parent = parent;
        this.dfs = dfs;
        dfs.threadCount++;
        this.threadId = dfs.threadCount;
    }

    @Override
    public void run() {
        boolean isDone = false;
        while(running.get()) {
            if (!isDone) {
                super.run();

                if (!start.isVisited()) start.visit();

                if (start.equals(destination)) {
                    System.out.println(threadId + " reached the destination");
                    dfs.done = true;

                    //Stop this thread
                    running.set(false);
                }


                if (!solve.scanAll) solve.findNeighbours(start);

                for (Node node : start.getNeighbours()) {
                    //If the node has not been added, add it
                    if (!node.isVisited()) {
                        node.visit();
                        node.setParent(start);
                        toProcess.add(new DFSWorker(solve, node, destination, this, dfs));
                    }
                }

                //Start all the threads
                for (DFSWorker thread : toProcess) thread.start();
                isDone = true;
            } else if (threadId != 1) {
                //Close this thread
                running.set(false);
            } else if (dfs.done) {
                //Keep thead 1 running until the maze is marked as solved
                running.set(false);
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DFSWorker worker = (DFSWorker) o;
        return threadId == worker.threadId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(threadId);
    }
}
