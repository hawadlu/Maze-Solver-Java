package Game;

import Application.Application;
import GUI.CustomPanels.PlayerPanel;
import Parser.Parser;
import Utility.Node;

import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

import Image.ImageFile;
import Utility.PathMaker;

public class Player {
  Application application;
  Node currentNode;
  PlayerPanel panel;
  String playerName;
  final ImageFile originalImage;
  Game currentGame;
  AtomicBoolean done = new AtomicBoolean(false);


  /**
   * @param maxSize the max size that any panels in the game can be displayed at
   */
  public Player(Dimension maxSize, String playerName, Application application, Game game) {
    this.originalImage = new ImageFile(application.getImageFile());

    //Create a new application
    this.application = new Application(application);
    this.playerName = playerName;
    this.currentGame = game;

    this.panel = new PlayerPanel(application, playerName, maxSize);
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

    updateSolveImage(newImage);
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
    createSolveImagePanel();

    String algorithm = panel.getAlgorithm();

    //Start the solve algorithm
    Thread solveThread;
    Parser customAlgo = panel.getCustomAlgo();

    if (customAlgo != null) {
      Player currentPlayer = this;
      //Start a new thread using the custom algorithm
      solveThread = new Thread() {
        @Override
        public void run() {
          customAlgo.handler.setPlayer(currentPlayer);
          customAlgo.handler.setDelay(delay);
          customAlgo.execute();
        }
      };
    } else {
      //Start using one of the prebuilt algorithms
      solveThread = application.solve(algorithm, "Loading", false, delay, this);
    }
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
}
