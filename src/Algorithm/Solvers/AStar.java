package Algorithm.Solvers;

import Algorithm.AlgorithmRunner;
import Algorithm.AlgorithmWorker;
import Algorithm.SolveAlgorithm;
import Utility.Exceptions.SolveFailure;
import Utility.Location;
import Utility.Node;

import java.util.Objects;
import java.util.PriorityQueue;

/**
 * Solve the maze, breadth first
 */
public class AStar extends AlgorithmRunner {

  /**
   * Do a depth first search.
   * Start at each end to speed up
   * @param solve the solve object
   */
  public void solve(SolveAlgorithm solve, Boolean multiThreading) {
    System.out.println("Solving Dijkstra");

    AlgorithmWorker workerOne = new AStarWorker(solve, solve.entry, solve.exit, this, "t1");
    AlgorithmWorker workerTwo = new AStarWorker(solve, solve.exit, solve.entry, this, "t2");

    solve.startThreads(workerOne, workerTwo, multiThreading);
  }
}

/**
 * Allows DFS to be multi threaded
 */
class AStarWorker extends AlgorithmWorker {
  public AStarWorker(SolveAlgorithm solve, Node start, Node destination, AlgorithmRunner runner, String threadId) {
    super(solve, start, destination, runner, threadId);
  }

  @Override
  public void run() {
    System.out.println("Thread: " + threadId + "\nstart: " + start + "\ndestination: " + destination + "\n");


    Node parent;
    PriorityQueue<Node> toProcess = new PriorityQueue<Node>(Node.getComparator());
    start.visit(this);
    start.setCost(0);
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
      for (Location nodeLocation : parent.getNeighbours()) {
        Node node = solve.getNodeMap().get(nodeLocation);
        double costToNode = parent.calculateCost(node) + Node.calculateEuclideanDistance(node, destination);

        //node is unvisited
        if (node.isVisited() == null) {
          node.setParent(parent);
          node.setCost(parent.getCost() + costToNode);
          toProcess.add(node);
          node.visit(this);

          //node has been visited by the other thread
        } else if (node.isVisited().equals(other)) {
          solve.addJoinerNodes(parent, node);
          runner.done.set(true);

          //node has been visited by this thread
        } else {
          //Check if the cost is lower
          if (parent.getCost() + costToNode < node.getCost()) {
            //A lower cost route has been found, re add the node to the queue
            node.setParent(parent);
            node.setCost(parent.getCost() + costToNode);
            toProcess.add(node);
            node.visit(this);
          }
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
    }
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
