package Algorithm;

import Utility.Exceptions.SolveFailure;
import Utility.Node;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Solve the maze, breadth first
 */
public class BreadthFirst {
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

    BFSWorker workerOne = new BFSWorker(solve, start, destination, this, "t1");
    BFSWorker workerTwo = new BFSWorker(solve, destination, start, this, "t2");
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
class BFSWorker extends Thread {
  SolveAlgorithm solve;
  Node destination, start;
  String threadId;
  BreadthFirst bfs;
  BFSWorker other;

  public BFSWorker(SolveAlgorithm solve, Node start, Node destination, BreadthFirst bfs, String threadId) {
    this.solve = solve;
    this.start = start;
    this.destination = destination;
    this.threadId = threadId;
    this.bfs = bfs;
  }

  @Override
  public void run() {
    System.out.println("Thread: " + threadId + "\nstart: " + start + "\ndestination: " + destination + "\n");


    Node parent = null;
    Queue<Node> toProcess = new ArrayDeque<>();
    start.visit(this);
    toProcess.add(start);

    while (!toProcess.isEmpty() && !bfs.done.get()) {

      parent = toProcess.poll();
      parent.visit(this);

      if (parent.equals(destination)) {
        System.out.println("Thread " + threadId + " is attempting to exit the loop");
        break;
      }

      if (!solve.scanAll) solve.findNeighbours(parent, bfs.multiThreading);

      //Add all the appropriate neighbours to the stack
      for (Node node : parent.getNeighbours(true)) {
        if (node.isVisited() == null) {
          node.setParent(parent);
          toProcess.add(node);
          node.visit(this);
        } else if (node.isVisited().equals(other)) {
          solve.addJoinerNodes(parent, node);
          bfs.done.set(true);
        }
      }

      //If the stack is empty at this point, solving failed
      if (toProcess.isEmpty() && !bfs.done.get()) {
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
    BFSWorker worker = (BFSWorker) o;
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
