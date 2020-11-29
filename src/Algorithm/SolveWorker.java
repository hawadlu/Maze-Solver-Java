package Algorithm;

import Utility.Node;

import java.util.HashSet;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Clasee used to check if a node has already been added to a thread.
 */
public class SolveWorker extends Thread {
  HashSet<Node> visited = new HashSet<>();

  SolveWorker other;
  Node start;
  Node destination;
  SolveAlgorithm solve;
  String threadName;
  AtomicBoolean running = new AtomicBoolean(false);

  public SolveWorker(SolveAlgorithm solve, Node start, Node destination, String threadName) {
    super();
    this.solve = solve;
    this.start = start;
    this.destination = destination;
    this.threadName = threadName;
  }

  /**
   * @param node add to the visited list
   */
  public void addNode(Node node) {
    visited.add(node);
  }

  public void addOther(SolveWorker other) {
    this.other = other;
  }

  @Override
  public synchronized void run() {
    System.out.println("DFS Worker '" + threadName + "' has started ");
    runSolve();
  }

  //Implemented by the subclasses
  public void runSolve(){};

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    SolveWorker that = (SolveWorker) o;
    return Objects.equals(threadName, that.threadName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(threadName);
  }
}
