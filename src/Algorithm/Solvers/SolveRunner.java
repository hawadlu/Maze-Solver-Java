package Algorithm.Solvers;

import Algorithm.SolveAlgorithm;
import Utility.Exceptions.SolveFailure;
import Utility.Node;

import java.util.Collection;
import java.util.PriorityQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Variables and methods used when setting up and running the algorithms
 */
public class SolveRunner {
  public final AtomicBoolean done = new AtomicBoolean(false);
  public final Boolean multiThreading = false;

  /**
   * Setup the priority queues used by astar and dijkstra
   *
   * @param start the start node
   * @return a new priority queue;
   */
  public PriorityQueue<Node> setupPriorityQueue(Node start, Thread currentThread) {
    PriorityQueue<Node> toProcess = new PriorityQueue<>(Node.getComparator());
    start.visit(currentThread);
    start.setCost(0);
    toProcess.add(start);

    return toProcess;
  }

  /**
   * Process the nodes for AStar and Dijkstra
   *
   * @param toProcess     the current queue of nodes
   * @param parent        the parent node
   * @param node          the current node
   * @param costToNode    the cost of getting to the current node
   * @param currentThread the thread that is currently running
   * @param other         the other thread that is running
   * @param solve         the current solveAlgorithm object
   */
  void processNode(PriorityQueue<Node> toProcess, Node parent, Node node, double costToNode, Thread currentThread, Thread other, SolveAlgorithm solve) {
    if (node.isVisited() == null) {
      node.setParent(parent);
      node.setCost(parent.getCost() + costToNode);
      toProcess.add(node);

      solve.logger.add(solve.player.getName() + " executed node.isVisited == null. Process size: " + toProcess.size());
      return;

      //node has been visited by the other thread
    } else if (node.isVisited().equals(other)) {
      solve.addJoinerNodes(parent, node);
      done.set(true);

      solve.logger.add(solve.player.getName() + " executed node.isVisited.equals(other). Process size: " + toProcess.size());
      return;

      //node has been visited by this thread
    } else if (parent.getCost() + costToNode < node.getCost()) {
      //A lower cost route has been found, re add the node to the queue
      node.setParent(parent);
      node.setCost(parent.getCost() + costToNode);
      toProcess.add(node);

      solve.logger.add(solve.player.getName() + " parent.getCost() + costToNode < node.getCost(). Process size: " + toProcess.size());
      return;
    }

    solve.logger.add(solve.player.getName() + " did not execute anything in processNode. Process size: " + toProcess.size());

  }
}
