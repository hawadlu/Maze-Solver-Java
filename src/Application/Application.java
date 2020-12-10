package Application;

//todo tidy code up so that most interfaces through this class

import Utility.Location;
import Utility.Node;
import Utility.Thread.AlgorithmDispatcher;
import GUI.GUI;
import Utility.Exceptions.GenericError;
import Image.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This class is the base of the program.
 * Most inter package communication happens through here.
 */
public class Application {
  GUI gui;
  static Application currentApplication;
  ImageFile currentImage;
  ImageProcessor imageProcessor;

  /**
   * Take the image file and parse it into the appropriate format
   * @param imageToParse
   */
  //todo, implement me, use the image classes to do the parsing
  public void parseImageFile(File imageToParse) throws GenericError {
    currentImage = new ImageFile(imageToParse);
  }

  /**
   * Setup the gui
   */
  private void setUpGui() {
    this.gui = new GUI(currentApplication);
  }

  /**
   * Gets a buffered image of the current maze.
   * @return an image of the maze
   */
  public BufferedImage getImage() {return currentImage.makeImage(); }

  public ImageFile getImageFile() {
    return currentImage;
  }

  public static void main(String[] args) {
    //Create the GUI
    currentApplication = new Application();
    currentApplication.setUpGui();
  }

  /**
   * Get the dimensions of the maze
   * @return the dimensions
   */
  public Dimension getMazeDimensions() {
    return currentImage.getDimensions();
  }

  /**
   * Get a specified piece of information about the image
   * @param info the requested info
   * @return the info
   */
  public String getImageInfo(String info) {
    return currentImage.getInfo(info);
  }

  /**
   * Start solving the maze
   * @param algorithm the algorithm to use
   * @param params the parameters to use
   */
  public AlgorithmDispatcher solve(String algorithm, String params, Boolean multiThreading) {
    this.imageProcessor = new ImageProcessor(this);
    AlgorithmDispatcher worker = new AlgorithmDispatcher(algorithm, params, this, "solver", multiThreading);
    return worker;
  }

  /**
   * Reset the current image to remove any marks from solving etc
   */
  public void resetImage() {
    currentImage.reset();
  }

  /**
   * Save the current image to a file
   * @param path the place to save the image
   */
  public void saveImage(String path) {
    currentImage.saveImage(path);
  }

  /**
   * @return the nodes from the image processor
   */
  public ConcurrentHashMap<Location, Node> getNodes() {
    return imageProcessor.getNodes();
  }

  /**
   * Scan the entire maze
   */
  public void scanEntireMaze() {
    imageProcessor.scanAll();
  }

  /**
   * Find the exits in the maze
   */
  public void findMazeExits() {
    imageProcessor.findExits();
  }

  /**
   * @return the maze exits
   */
  public ArrayList<Location> getMazeExits() {
    return imageProcessor.getExits();
  }

  /**
   * Scan only a part of the maze
   * @param parent the node to start at
   * @param multiThreading is the program currently multi threading?
   */
  public void scanPart(Node parent, Boolean multiThreading) {
    imageProcessor.scanPart(parent, multiThreading);
  }
}
