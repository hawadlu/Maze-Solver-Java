package algorithm.Solvers;

import algorithm.SolveAlgorithm;
import utility.Node;

import java.util.ArrayDeque;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

/**
 * solve the maze, breadth first
 */
public class BreadthFirst extends SolveRunner {

  /**
   * Do a breadth first search.
   * Start at each end to speed up
   *
   * @param solve the solve object
   */
  public void solve(SolveAlgorithm solve, Boolean multiThreading, String threadId) {
    System.out.println("Solving breadth first");


    SolveWorker workerOne = new BFSWorker(solve, solve.entry, solve.exit, this, threadId + "t1");
    SolveWorker workerTwo = new BFSWorker(solve, solve.exit, solve.entry, this, threadId + "t2");

    solve.startThreads(workerOne, workerTwo, multiThreading);

    waitComplete(workerOne, workerTwo);
  }
}

/**
 * Allows DFS to be multi threaded
 */
class BFSWorker extends SolveWorker {

  /**
   * Create a new Breadth First object.
   * @param solve the solve object.
   * @param start Node that represents the start of the maze.
   * @param destination Node that represents the end of the maze.
   * @param runner the runner object.
   * @param threadId unique identifier for this thread
   */
  public BFSWorker(SolveAlgorithm solve, Node start, Node destination, SolveRunner runner, String threadId) {
    super(solve, start, destination, runner, threadId);
  }

  /**
   * Run the Breadth First algorithm on a new thread.
   */
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
        System.out.println("Thread " + threadId + " for player " + solve.player.getName() + " is attempting to exit the loop");
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

      ;

      if (solve.player != null) {
        solve.updatePlayer(parent);

        //Pause this thead
        try {
          TimeUnit.MILLISECONDS.sleep(solve.getDelay());
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    }
    //Mark the player as done
    if (solve.player != null) solve.player.markDone();
    System.out.println("Thread " + threadId + " for player " + solve.player.getName() + " has exited the loop");
  }

  /**
   * Check object equality.
   * @param o another Breadth First object.
   * @return a boolean to indicate the equality of the two Breadth First objects.
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    BFSWorker worker = (BFSWorker) o;
    return destination.equals(worker.destination) &&
            start.equals(worker.start) &&
            threadId.equals(worker.threadId);
  }

  /**
   * @return A hashcode representing a Breadth First object.
   */
  @Override
  public int hashCode() {
    return Objects.hash(destination, start, threadId);
  }

  /**
   *
   * @return a string representation of the object.
   */
  @Override
  public String toString() {
    return threadId;
  }
}
