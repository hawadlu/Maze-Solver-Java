package Algorithm.Solvers;

import Algorithm.SolveAlgorithm;
import Utility.Node;

import java.util.ArrayList;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.concurrent.TimeUnit;

/**
 * solve the maze, breadth first
 */
public class AStar extends SolveRunner {

  /**
   * Do an AStar first search.
   * Start at each end to speed up
   *
   * @param solve the solve object
   */
  public void solve(SolveAlgorithm solve, Boolean multiThreading, String threadId) {
    //solve.logger.add(solve.player.getName() + " Entered AStar solve");
    System.out.println("Solving AStar");

    SolveWorker workerOne = new AStarWorker(solve, solve.entry, solve.exit, this, threadId + "t1");
    SolveWorker workerTwo = new AStarWorker(solve, solve.exit, solve.entry, this, threadId + "t2");

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

      //solve.logger.add(solve.player.getName() + " reached the top of the while loop. Process size: " + toProcess.size()+ " Runner done: " + runner.done.get());

      Node parent = toProcess.poll();
      parent.visit(this);

      //solve.logger.add(solve.player.getName() + " polled the queue and visited the parent. Process size: " + toProcess.size()+ " Runner done: " + runner.done.get());

      //Update the display and stop
      if (parent.equals(destination)) {
        System.out.println("Thread " + threadId + " for player " + solve.player.getName() + " is attempting to exit the loop. Process size: " + toProcess.size()+ " Runner done: " + runner.done.get());
        solve.updatePlayer(parent);
        break;
      }

      //solve.logger.add(solve.player.getName() + " checked if the destination was reached. Process size: " + toProcess.size()+ " Runner done: " + runner.done.get());

      if (!solve.scanAll) {
        solve.findNeighbours(parent, runner.multiThreading);
      }

      for (Node node : parent.getNeighbours()) {
        //solve.logger.add(solve.player.getName() + " checking node " + node.toString() + ". Process size: " + toProcess.size()+ " Runner done: " + runner.done.get());
        double costToNode = parent.calculateDistance(node) + Node.calculateEuclideanDistance(node, destination);

        //solve.logger.add(solve.player.getName() + " about to process node " + node.toString() + ". Process size: " + toProcess.size()+ " Runner done: " + runner.done.get());

        //node is unvisited
        runner.processNode(toProcess, parent, node, costToNode, this, other, solve);

        //solve.logger.add(solve.player.getName() + " completed processing node " + node + ". Process size: " + toProcess.size()+ " Runner done: " + runner.done.get());
      }

      if (solve.isLive()) {
        //solve.logger.add(solve.player.getName() + " updating player screen. Process size: " + toProcess.size() + " Runner done: " + runner.done.get());
        solve.updatePlayer(parent);

        //Pause this thead
        try {
          TimeUnit.MILLISECONDS.sleep(solve.getDelay());
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
      //solve.logger.add(solve.player.getName() + " reached the bottom of the while loop. Process size: " + toProcess.size()+ " Runner done: " + runner.done.get());
    }
    //Mark the player as done
    if (solve.player != null) solve.player.markDone();
    System.out.println("Thread " + threadId + " for player " + solve.player.getName() + " has exited the loop");
    //solve.logger.save();
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
