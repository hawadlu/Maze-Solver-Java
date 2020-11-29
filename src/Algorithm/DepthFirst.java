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
     * @param twoThread indicate if the maze is large enough for a single thread.
     */
    public void solve(SolveAlgorithm solve, Boolean twoThread) {
        System.out.println("Solving depth first");

        Node start = solve.entry;
        Node destination = solve.exit;

        DFSWorker workerOne = new DFSWorker(solve, start, destination, "t1");
        DFSWorker workerTwo = null;

        //Only create a second worker if the maze is big enough
        System.out.println("Two thread: " + twoThread);
        if (twoThread) {
            workerTwo = new DFSWorker(solve, destination, start, "t2");
            workerOne.setOther(workerTwo);
            workerTwo.setOther(workerOne);
            workerTwo.start();
        }

        workerOne.start();

        //Wait for the threads to finish
        try {
            workerOne.join();
            if (workerTwo != null && workerTwo.isAlive()) workerTwo.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

/**
 * Allows DFS to be multi threaded
 */
class DFSWorker extends SolveWorker {
    Boolean multithreading = false;

    public DFSWorker(SolveAlgorithm solve, Node start, Node destination, String threadName) {
        super(solve, start, destination, threadName);
    }

    public void setOther(SolveWorker other) {
        this.other = other;
        multithreading = true;
    }

    @Override
    public void runSolve() {
        this.running.set(true);

        Node parent = null;
        Stack<Node> toProcess = new Stack<>();
        start.visit(this);
        toProcess.push(start);

        while (running.get()) {
            parent = toProcess.pop();
            parent.visit(this);
            addNode(parent);

            //Stop both threads
            if (parent.equals(destination)) {
                System.out.println(threadName + " has reached the destination");
                if (multithreading) other.running.set(false);
                running.set(false);
            }

            //Scan for neighbours if required
            if (!solve.scanAll) solve.findNeighbours(parent);

            //Add the neighbours to the stack
            for (Node node: parent.getNeighbours()) {
                if (node.isVisited() == null) {
                    node.setParent(parent);
                    toProcess.push(node);
                } else if (node.isVisited().equals(other)) {
                    if (multithreading) {
                        other.running.set(false);
                        System.out.println("Node: " + node + " has already been visited by thread " + other.threadName);
                    }
                    running.set(false);
                }
            }

            if (toProcess.isEmpty()) {
                try {
                    other.running.set(false);
                    throw new SolveFailure(threadName + " the stack is empty");
                } catch (SolveFailure e) {
                    e.printStackTrace();
                }
            }
        }

        System.out.println("Thread " + threadName + " has exited the while loop");

//        while (!toProcess.isEmpty() || !running.get()) {
//            System.out.println(threadName + " " + running + " " + other.threadName + " " + other.running);
//            System.out.println(threadName + ": process size: " + toProcess.size() + " " + toProcess);
//
//            parent = toProcess.pop();
//            parent.visit(this);
//            addNode(parent);
//
//            if (parent.equals(destination)) {
//                running.set(false);
//            }
//
//            //If the neighbours are not already loaded, load them.
//            if (!solve.scanAll) solve.findNeighbours(parent);
//
//            //Add all the appropriate neighbours to the stack
//            for (Node node: parent.getNeighbours()) {
//                if (!other.running.get()) {
//                    running.set(false);
//                    System.out.println(threadName + " left the for loop");
//                    break;
//                } else if (node.isVisited() == null) {
//                    node.setParent(parent);
//                    toProcess.push(node);
//                } else if (node.isVisited().equals(other)) {
//                    running.set(false);
//                    System.out.println("Thread " + threadName + " stopped because " + other.threadName + " has visited this node");
//                }
//
//
//                //Add the node to the queue
////                if (!node.isVisited()) {
////                    node.setParent(parent);
////                    toProcess.push(node);
////                } else if (other.visited.contains(node)) { node.setParent(parent);
////                    this.isDone = true;
////
////                    System.out.println(threadName + " Solved, the other thread (" + other.threadName + ") has this node. halting thread execution.");
////                    other.interrupt();
////                    this.interrupt();
////                }
//            }
//
//            if (other.running.get()) System.out.println(threadName + " stopped because " + other.threadName + " is done");
//
//            //If the stack is empty at this point, solving failed
//            if (toProcess.isEmpty()) {
//                try {
//                    throw new SolveFailure(threadName + " the stack is empty");
//                } catch (SolveFailure e) {
//                    e.printStackTrace();
//                }
//            }
//        }
    }

    @Override
    public String toString() {
        return "Thread: DFSWorker " + threadName;
    }
}
