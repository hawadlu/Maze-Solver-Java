package Algorithm.Solvers;

import Algorithm.SolveAlgorithm;
import Utility.Node;

import java.util.Objects;
import java.util.Stack;
import java.util.concurrent.TimeUnit;

/**
 * solve the maze, depth first
 */
public class DepthFirst extends SolveRunner {

  /**
   * Do a depth first search.
   * Start at each end to speed up
   *
   * @param solve the solve object
   */
  public void solve(SolveAlgorithm solve, Boolean multiThreading, String threadId) {
    System.out.println("Solving depth first");

    SolveWorker workerOne = new DFSWorker(solve, solve.entry, solve.exit, this, threadId + "t1");
    SolveWorker workerTwo = new DFSWorker(solve, solve.exit, solve.entry, this, threadId + "t2");

    solve.startThreads(workerOne, workerTwo, multiThreading);
  }
}

/**
 * Allows DFS to be multi threaded
 */
class DFSWorker extends SolveWorker {
  public DFSWorker(SolveAlgorithm solve, Node start, Node destination, SolveRunner runner, String threadId) {
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
        System.out.println("Thread " + threadId + " for player " + solve.player.getName() + " is attempting to exit the loop");
        break;
      }

      //If the neighbours are not already loaded, load them.
      if (!solve.scanAll) solve.findNeighbours(parent, runner.multiThreading);

      //Add all the appropriate neighbours to the stack
      for (Node node : parent.getNeighbours()) {
        if (node.isVisited() == null) {
          node.setParent(parent);
          toProcess.push(node);
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
