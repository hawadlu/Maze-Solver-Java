package algorithm.Solvers;

import algorithm.SolveAlgorithm;
import Utility.Node;

/**
 * This class contains variables and methods used by the various solve algorithms
 */
public class SolveWorker extends Thread {
  public final SolveAlgorithm solve;
  public final Node destination;
  public final Node start;
  public final String threadId;
  public final SolveRunner runner;
  public SolveWorker other;

  /**
   * Create a new SolveWorker.
   *
   * @param solve the SolveAlgorithm object
   * @param start Node representing the start
   * @param destination Node representing the destination.
   * @param runner the runner object
   * @param threadId unique identifier for this thread.
   */
  public SolveWorker(SolveAlgorithm solve, Node start, Node destination, SolveRunner runner, String threadId) {
    this.solve = solve;
    this.start = start;
    this.destination = destination;
    this.threadId = threadId;
    this.runner = runner;
  }
}
