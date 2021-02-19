package Algorithm.Solvers;

import Algorithm.SolveAlgorithm;
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
   *
   * @param solve
   * @param start
   * @param destination
   * @param runner
   * @param threadId
   */
  public SolveWorker(SolveAlgorithm solve, Node start, Node destination, SolveRunner runner, String threadId) {
    this.solve = solve;
    this.start = start;
    this.destination = destination;
    this.threadId = threadId;
    this.runner = runner;
  }
}
