package Algorithm.Solvers;

import Algorithm.SolveAlgorithm;
import Utility.Exceptions.SolveFailure;
import Utility.Node;

import java.util.ArrayDeque;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

/**
 * Solve the maze, breadth first
 */
public class BreadthFirst extends SolveRunner {

  /**
   * Do a depth first search.
   * Start at each end to speed up
   *
   * @param solve the solve object
   */
  public void solve(SolveAlgorithm solve, Boolean multiThreading) {
    System.out.println("Solving breadth first");


    SolveWorker workerOne = new BFSWorker(solve, solve.entry, solve.exit, this, "t1");
    SolveWorker workerTwo = new BFSWorker(solve, solve.exit, solve.entry, this, "t2");

    solve.startThreads(workerOne, workerTwo, multiThreading);
  }
}

/**
 * Allows DFS to be multi threaded
 */
class BFSWorker extends SolveWorker {

  public BFSWorker(SolveAlgorithm solve, Node start, Node destination, SolveRunner runner, String threadId) {
    super(solve, start, destination, runner, threadId);
  }

  @Override
  public void run() {
    System.out.println("Thread: " + threadId + "\nstart: " + start + "\ndestination: " + destination + "\n");


    Node parent;
    Queue<Node> toProcess = new ArrayDeque<>();
    start.visit(this);
    toProcess.add(start);

    while (!toProcess.isEmpty() && !runner.done.get()) {

      parent = toProcess.poll();
      assert parent != null;
      parent.visit(this);

      if (parent.equals(destination)) {
        System.out.println("Thread " + threadId + " is attempting to exit the loop");
        break;
      }

      if (!solve.scanAll) solve.findNeighbours(parent, runner.multiThreading);

      //Add all the appropriate neighbours to the stack
      for (Node node : parent.getNeighbours()) {
        if (node.isVisited() == null) {
          node.setParent(parent);
          toProcess.add(node);
          node.visit(this);
        } else if (node.isVisited().equals(other)) {
          solve.addJoinerNodes(parent, node);
          runner.done.set(true);
        }
      }

      //If the queue is empty at this point, solving failed
      if (toProcess.isEmpty() && !runner.done.get()) {
        try {
          throw new SolveFailure("The stack is empty on thread " + threadId);
        } catch (SolveFailure e) {
          e.printStackTrace();
        }
      }

      if (solve.player != null) {
        solve.updatePlayer(parent);

        //Pause this thead
        try {
          TimeUnit.MILLISECONDS.sleep(solve.delay);
        } catch (InterruptedException e) {
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
