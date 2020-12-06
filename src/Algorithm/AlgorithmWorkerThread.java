package Algorithm;

import Utility.Node;

/**
 * This class contains variables and methods used by the various solve algorithms
 */
public class AlgorithmWorkerThread extends Thread{
  final SolveAlgorithm solve;
  final Node destination;
  final Node start;
  final String threadId;
  final AlgorithmRunner runner;
  AlgorithmWorkerThread other;

  public AlgorithmWorkerThread(SolveAlgorithm solve, Node start, Node destination, AlgorithmRunner runner, String threadId) {
    this.solve = solve;
    this.start = start;
    this.destination = destination;
    this.threadId = threadId;
    this.runner = runner;
  }
}