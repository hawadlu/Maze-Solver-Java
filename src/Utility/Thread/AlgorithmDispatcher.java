package Utility.Thread;

import Algorithm.SolveAlgorithm;
import Application.Application;
import Game.Player;
import parser.Handler;
import parser.Parser;


/**
 * This thread takes is the one that processes the algorithms.
 * Using multiple threads allows multiple algorithms to run simultaneously.
 */
public class AlgorithmDispatcher extends Thread {
  private String algorithm;
  private String params;
  private Application currentApplication;
  private String id;
  private boolean multiThreading;
  private Parser parser;
  private SolveAlgorithm solve;
  private Player player; //may be null
  private int delay;

  public AlgorithmDispatcher(String algorithm, String params, Application currentApplication, String id, Boolean multiThreading, int delay, Player player) {
    this.algorithm = algorithm;
    this.params = params;

    if (player != null && player.application != null) this.currentApplication = player.application;
    else this.currentApplication = new Application(currentApplication);

    this.id = id;
    this.multiThreading = multiThreading;
    this.delay = delay;
    this.player = player;
  }

  public AlgorithmDispatcher(Application currentApplication, String solver, int delay, Player player, Parser parser) {
    if (player.application != null) this.currentApplication = player.application;
    else this.currentApplication = new Application(currentApplication);

    this.id = solver;
    this.delay = delay;
    this.player = player;
    this.parser = parser;
  }

  /**
   * Used by the parser. Empty because the parser only needs a thread to say that
   * a node has been visited. None of the other fields are required.
   */
  public AlgorithmDispatcher(){};

  @Override
  public synchronized void run() {
    if (parser == null) {
      System.out.println("Worker id: " + id);
      solve = new SolveAlgorithm(currentApplication, delay, player);

      //Check if the nodes have already been scanned
      if (currentApplication.getNodes().isEmpty()) solve.scan(params);
      solve.solve(algorithm, multiThreading);
    } else {
      System.out.println("Running parsed program");
      parser.setPlayer(player);
      parser.setMazeHandler(new Handler(currentApplication, delay));
      player.startParserExec(delay);
    }
  }

  public long getExecTime() {
    return solve.execTime;
  }

  public double getMazeSize() {
    return solve.mazeSize;
  }

  /**
   * Get the application object.
   * Used for testing.
   * @return the application.
   */
  public Application getApplication() {
    return currentApplication;
  }
}
