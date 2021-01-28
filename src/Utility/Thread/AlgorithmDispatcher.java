package Utility.Thread;

import Algorithm.SolveAlgorithm;
import Application.Application;
import Game.Player;
import Image.ImageFile;
import Image.ImageProcessor;
import parser.Handler;
import parser.Parser;


/**
 * This thread takes is the one that processes the algorithms.
 * Using multiple threads allows multiple algorithms to run simultaneously.
 */
public class AlgorithmDispatcher extends Thread {
  private String algorithm;
  private String params;
  private String id;
  private boolean multiThreading;
  private Parser parser;
  private SolveAlgorithm solve;
  private Player player; //may be null
  private int delay;
  private ImageProcessor imageProcessor;
  private ImageFile imageFile;

  public AlgorithmDispatcher(String algorithm, String params, String id, Boolean multiThreading, int delay, Player player) {
    this.algorithm = algorithm;
    this.params = params;

    this.id = id;
    this.multiThreading = multiThreading;
    this.delay = delay;
    this.player = player;
  }

  public AlgorithmDispatcher(String solver, int delay, Player player, Parser parser) {
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
      solve = new SolveAlgorithm(delay, player, imageFile, imageProcessor);

      //Check if the nodes have already been scanned
      if (imageProcessor.getNodes().isEmpty()) solve.scan(params);
      solve.solve(algorithm, multiThreading);
    } else {
      System.out.println("Running parsed program");
      parser.setMazeHandler(new Handler(delay, player));
      player.startParserExec(delay);
    }

    System.out.println("THREAD STOPPED");
    if (player != null) System.out.println(player);
  }

  public long getExecTime() {
    return solve.execTime;
  }

  public double getMazeSize() {
    return solve.mazeSize;
  }
}
