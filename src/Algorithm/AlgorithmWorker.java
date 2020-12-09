package Algorithm;

import Utility.Node;
import Utility.Segment;

import java.util.ArrayList;

/**
 * This class contains variables and methods used by the various solve algorithms
 */
public class AlgorithmWorker extends Thread{
  public final SolveAlgorithm solve;
  public final Node destination;
  public final Node start;
  public final String threadId;
  public final AlgorithmRunner runner;
  public AlgorithmWorker other;

  public AlgorithmWorker(SolveAlgorithm solve, Node start, Node destination, AlgorithmRunner runner, String threadId) {
    this.solve = solve;
    this.start = start;
    this.destination = destination;
    this.threadId = threadId;
    this.runner = runner;
  }
}
