package Application;

import Utility.Thread.AlgorithmWorkerThread;
import GUI.GUI;
import Utility.Exceptions.GenericError;
import Utility.Image.ImageFile;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * This class is the base of the program.
 * Most inter package communication happens through here.
 */
public class Application {
  GUI gui;
  static Application currentApplication;
  ImageFile currentImage;
  
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
  public AlgorithmWorkerThread solve(String algorithm, String params) {
    AlgorithmWorkerThread worker = new AlgorithmWorkerThread(algorithm, params, this);
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
}
