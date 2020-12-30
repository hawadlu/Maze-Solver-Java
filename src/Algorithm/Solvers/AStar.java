package Algorithm.Solvers;

import Algorithm.SolveAlgorithm;
import Utility.Node;

import java.util.Objects;
import java.util.PriorityQueue;
import java.util.concurrent.TimeUnit;

/**
 * solve the maze, breadth first
 */
public class AStar extends SolveRunner {

  /**
   * Do a depth first search.
   * Start at each end to speed up
   *
   * @param solve the solve object
   */
  public void solve(SolveAlgorithm solve, Boolean multiThreading) {
    System.out.println("Solving AStar");

    SolveWorker workerOne = new AStarWorker(solve, solve.entry, solve.exit, this, "t1");
    SolveWorker workerTwo = new AStarWorker(solve, solve.exit, solve.entry, this, "t2");

    solve.startThreads(workerOne, workerTwo, multiThreading);
  }
}

/**
 * Allows DFS to be multi threaded
 */
class AStarWorker extends SolveWorker {
  public AStarWorker(SolveAlgorithm solve, Node start, Node destination, SolveRunner runner, String threadId) {
    super(solve, start, destination, runner, threadId);
  }

  @Override
  public void run() {
    System.out.println("Thread: " + threadId + "\nstart: " + start + "\ndestination: " + destination + "\n");


    PriorityQueue<Node> toProcess = runner.setupPriorityQueue(start, this);

    while (!toProcess.isEmpty() && !runner.done.get()) {

      Node parent = toProcess.poll();
      assert parent != null;
      parent.visit(this);

      if (parent.equals(destination)) {
        System.out.println("Thread " + threadId + " is attempting to exit the loop");
        break;
      }

      if (!solve.scanAll) solve.findNeighbours(parent, runner.multiThreading);

      //Add all the appropriate neighbours to the stack
      for (Node node : parent.getNeighbours()) {
        double costToNode = parent.calculateDistance(node) + Node.calculateEuclideanDistance(node, destination);

        //node is unvisited
        runner.processNode(toProcess, parent, node, costToNode, this, other, solve);
      }

      runner.checkCollection(toProcess, solve, threadId);

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
    //Mark the player as done
    if (solve.player != null) solve.player.markDone();
    System.out.println("Thread " + threadId + " has exited the loop");
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    AStarWorker worker = (AStarWorker) o;
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
