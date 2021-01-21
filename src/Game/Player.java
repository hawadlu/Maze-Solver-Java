package Game;

import Application.Application;
import GUI.CustomPanels.PlayerPanel;
import Image.ImageFile;
import parser.Parser;
import Utility.Node;
import Utility.PathMaker;

import java.awt.*;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

public class Player {
  public Application application;
  Node currentNode;
  PlayerPanel panel;
  String playerName;
  final ImageFile originalImage;
  Game currentGame;
  AtomicBoolean done = new AtomicBoolean(false);
  Player other;
  Parser customAlgo;
  AtomicBoolean isDone = new AtomicBoolean(false);


  /**
   * @param maxSize the max size that any panels in the game can be displayed at
   */
  public Player(Dimension maxSize, String playerName, Application application, Game game) {
    this.application = new Application(application);
    this.originalImage = new ImageFile(application.getImageFile());

    //Create a new application
    this.playerName = playerName;
    this.currentGame = game;

    this.panel = new PlayerPanel(application, playerName, maxSize);
  }


  public Player(String playerName, Game game, Application application) {
    this.application = new Application(application);
    this.originalImage = new ImageFile(application.getImageFile());

    this.playerName = playerName;
    this.currentGame = game;
  }

  public void setOther(Player other) {
    this.other = other;
  }

  public void createSolveImagePanel() {
    panel.initSolvePanel();
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
    if (panel != null) updateSolveImage(newImage);
  }

  /**
   * @return the JPanel that is the visual representation of this players progress
   */
  public PlayerPanel getPanel() {
    return panel;
  }

  /**
   * solve the maze
   *
   * @param delay the requested delay
   */
  public void solve(int delay) {
    System.out.println(playerName + " has started the solve");

    //Make this display an image panel
    String algorithm = "";
    if (panel != null) {
      createSolveImagePanel();
      algorithm = panel.getAlgorithm();
    }

    //Start the solve algorithm
    Thread solveThread;
    if (customAlgo == null) customAlgo = panel.getCustomAlgo();

    //Start using one of the prebuilt algorithms
    solveThread = application.solve(algorithm, "Loading", false, delay, this, customAlgo);
    solveThread.start();
  }

  /**
   * Make the panel display a done message.
   * @param message, the message to display
   */
  public void makeDoneDisplay(String message) {
    //Make the completed image
    ImageFile newImage = new ImageFile(originalImage);

    //Create a path from the current node
    newImage.fillNodePath(PathMaker.generatePathArraylist(currentNode), true);

    panel.markDone(message, newImage);
  }

  /**
   * Mark this player as done
   */
  public void markDone() {
    done.set(true);
    currentGame.markDone(this);
  }

  @Override
  public String toString() {
    return playerName + " Done: " + done;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Player player = (Player) o;
    return panel.equals(player.panel) && playerName.equals(player.playerName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(panel, playerName);
  }

  /**
   * Start executing the parser
   */
  public void startParserExec(int delay) {
    //Create the new maze handler object if necessary
    customAlgo.setPlayer(this);
    customAlgo.execute(application, delay);
  }
}
