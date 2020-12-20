package Utility.Thread;

import Algorithm.SolveAlgorithm;
import Application.Application;
import Game.Player;

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
  private Player player; //may be null
  private int delay;

  public AlgorithmDispatcher(String algorithm, String params, Application currentApplication, String id, Boolean multiThreading, int delay, Player player) {
    this.algorithm = algorithm;
    this.params = params;
    this.currentApplication = currentApplication;
    this.id = id;
    this.multiThreading = multiThreading;
    this.delay = delay;
    this.player = player;
  }

  @Override
  public synchronized void run() {
    System.out.println("Worker id: " + id);
    solve = new SolveAlgorithm(currentApplication, delay, player);

    //Check if the nodes have already been scanned
    if (currentApplication.getNodes().isEmpty()) solve.scan(params);
    solve.Solve(algorithm, multiThreading);
  }

  public long getExecTime() {
    return solve.execTime;
  }

  public double getMazeSize() {
    return solve.mazeSize;
  }
}
