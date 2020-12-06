package Utility.Thread;

import Algorithm.SolveAlgorithm;
import Application.Application;

/**
 * This thread takes is the one that processes the algorithms.
 * Using multiple threads allows multiple algorithms to run simultaneously.
 */
public class AlgorithmDispatcher extends Thread {
  private final String algorithm;
  private final String params;
  private final Application currentApplication;
  private final String id;
  private final boolean multiThreading;
  private SolveAlgorithm solve;

  public AlgorithmDispatcher(String algorithm, String params, Application currentApplication, String id, Boolean multiThreading) {
    this.algorithm = algorithm;
    this.params = params;
    this.currentApplication = currentApplication;
    this.id = id;
    this.multiThreading = multiThreading;
  }

  @Override
  public synchronized void run() {
    System.out.println("Worker id: " + id);
    solve = new SolveAlgorithm(currentApplication);
    solve.Scan(params);
    solve.Solve(algorithm, multiThreading);
  }

  public long getExecTime() {
    return solve.execTime;
  }

  public double getMazeSize() {
    return solve.mazeSize;
  }
}
