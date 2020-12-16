package Game;

import Application.Application;
import GUI.CustomPanels.PlayerPanel;
import Utility.Node;

import java.awt.*;

import Image.ImageFile;
import Utility.PathMaker;

public class Player {
  Application application;
  Node currentNode;
  PlayerPanel panel;
  String playerName;
  Boolean done = false;
  ImageFile originalImage;


  /**
   * @param maxSize the max size that any panels in the game can be displayed at
   */
  public Player(Dimension maxSize, String playerName, Application application) {
    this.originalImage = new ImageFile(application.getImageFile());

    //Create a new application
    this.application = new Application(application);
    this.playerName = playerName;

    this.panel = new PlayerPanel(application, playerName, maxSize);
  }

  public void createSolveImagePanel(ImageFile displayImage) {
    panel.initSolvePanel(displayImage);
  }

  private void updateSolveImage(ImageFile displayImage) {
    panel.updateImage(displayImage);
  }

  /**
   * @param node the node to draw the image from
   */
  public void update(Node node) {
    this.currentNode = node;

    //Create a duplicate image file
    ImageFile newImage = new ImageFile(originalImage);

    //Create a path from the current node
    newImage.fillNodePath(PathMaker.generatePathArraylist(currentNode), true);

    createSolveImagePanel(newImage);

//    GUI.GUI.refresh();
  }

  /**
   * @return the JPanel that is the visual representation of this players progress
   */
  public PlayerPanel getPanel() {
    return panel;
  }

  /**
   * Solve the maze
   *
   * @param delay the requested delay
   */
  public void solve(int delay) {
    System.out.println(playerName + " has started the solve");

    //Make this display an image panel
    createSolveImagePanel(application.getImageFile());

    String algorithm = panel.getAlgorithm();

    //todo refactor to allow the players own algorithm
    //Start the solve algorithm
    Thread solveThread = application.solve(algorithm, "Loading", false, delay, this);
    solveThread.start();
  }
}
