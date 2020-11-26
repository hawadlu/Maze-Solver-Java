package Utility.Thread;

import Algorithm.SolveAlgorithm;
import Application.Application;
import Utility.Image.ImageFile;

/**
 * This thread takes is the one that processes the algorithms.
 * Using multiple threads allows multiple algorithms to run simultaneously.
 */
public class AlgorithmWorkerThread extends Thread {
  private final String algorithm;
  private final String params;
  private final Application currentApplication;
  private ImageFile solvedImage = null;

  public AlgorithmWorkerThread(String algorithm, String params, Application currentApplication) {
    this.algorithm = algorithm;
    this.params = params;
    this.currentApplication = currentApplication;
  }

  @Override
  public synchronized void start() {
    solvedImage = currentApplication.getImageFile();
    SolveAlgorithm solve = new SolveAlgorithm(currentApplication);
    solve.Scan(params);
    solve.Solve(algorithm);

    //Create the solved image
    solvedImage.createSolvedImage(solve.getPath());
  }
}
