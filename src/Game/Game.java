package Game;

import Application.Application;
import GUI.CustomPanels.PlayerPanel;
import GUI.GUI;
import Parser.Parser;

import javax.swing.*;
import java.awt.*;

public class Game {
  Player playerOne, playerTwo;
  JPanel controlPanel;
  GUI gui;


  /**
   * @param maxSize the max size that any panels in the game can be displayed at
   */
  public Game(Dimension maxSize, GUI gui, JPanel controlPanel, Application application) {
    playerOne = new Player(maxSize, "Player One", application, this);
    playerTwo = new Player(maxSize, "Player Two", application, this);
    this.gui = gui;
    this.controlPanel = controlPanel;
  }

  public Game (Parser algoOne, Parser algoTwo, Application application) {
    playerOne = new Player("Player One", this, application);
    playerTwo = new Player("Player Two", this, application);

    playerOne.customAlgo = algoOne;
    playerTwo.customAlgo = algoTwo;
  }

  /**
   * @param playerNum the number of the requested player
   * @return the panel representing this player
   */
  public PlayerPanel getPlayerPanel(int playerNum) {
    if (playerNum == 1) return playerOne.getPanel();
    else return playerTwo.getPanel();
  }


  /**
   * Start a new thread and load all of the nodes
   * @param delayTextArea     the text area that contains the requested delay;
   */
  public void loadNodes(JTextArea delayTextArea, Application application) {

    Thread load = new Thread() {
      @Override
      public void run() {

        System.out.println("Starting node scan");
        application.scanEntireMaze();

        //Update the component
        JButton solve = new JButton("solve");
        solve.addActionListener(e -> {

          //Extract the requested time delay
          int delay = 25;
          if (isInt(delayTextArea.getText()) && Integer.parseInt(delayTextArea.getText()) > 0) delay = Integer.parseInt(delayTextArea.getText());
          else System.out.println(delayTextArea.getText() + " is not valid. Delay has been set to 25.");

          //Create a new thread for the purpose of updating the control panel
          Thread controlUpdate = new Thread() {
            @Override
            public void run() {
              controlPanel.removeAll();
            }
          };

          controlUpdate.start();

          startPlayers(delay);
        });


        controlPanel.remove(3);
        controlPanel.add(solve, 3);

        System.out.println("Complete node loading");
        GUI.refresh();
      }
    };

    load.start();
  }

  /**
   * Start the players
   * @param delay
   */
  public void startPlayers(int delay) {
    playerOne.solve(delay);
    playerTwo.solve(delay);
  }

  /**
   * Check if a given string is an int.
   * @param text the text to check.
   * @return boolean, true/false
   */
  private boolean isInt(String text) {
    try {
      Integer.parseInt(text);
    } catch(NumberFormatException | NullPointerException e) {
      return false;
    }
    // only got here if we didn't return false
    return true;
  }

  /**
   * Mark that this player has completed solving
   * @param player the player who has finished.
   */
  public void markDone(Player player) {
    //Determine if the other player is finished
    if (player.equals(playerOne) && !playerTwo.done.get()) playerOne.makeDoneDisplay("1st");
    else if (player.equals(playerTwo) && !playerOne.done.get()) playerTwo.makeDoneDisplay("1st");
    else if (player.equals(playerOne) && playerTwo.done.get()) {
      playerOne.makeDoneDisplay("2nd");
      makeResetButton();
    } else if (player.equals(playerTwo) && playerOne.done.get()) {
      playerTwo.makeDoneDisplay("2nd");
      makeResetButton();
    }
  }

  /**
   * Make a button, that will be used to reset the game
   */
  private void makeResetButton() {
    System.out.println("making reset button");
    JButton reset = new JButton("Reset Game");
    controlPanel.add(reset);

    reset.addActionListener(e -> {
      gui.makeGameStartScreen();
    });

    GUI.refresh();
  }

}
