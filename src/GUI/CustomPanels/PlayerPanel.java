package GUI.CustomPanels;

import Application.Application;
import GUI.GUI;

import javax.swing.*;
import java.awt.*;

/**
 * This class holds all of the interactions used when playing the game
 */
public class PlayerPanel extends JPanel {
  Application application;
  String playerLabel;
  JComponent algorithm = null, parser = null;
  GUI gui;

  public PlayerPanel(Application application, String playerLabel, Dimension maxSize, GUI gui) {
    this.application = application;
    this.playerLabel = playerLabel;
    this.setBackground(Color.CYAN);
    this.setBorder(BorderFactory.createLineBorder(Color.GREEN));
    this.setPreferredSize(maxSize);
    this.gui = gui;

    makeSolveLayout();
  }

  /**
   * Make the solve layout for the panel
   */
  private void makeSolveLayout() {
    this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

    //The player title
    JLabel title = new JLabel(playerLabel);
    title.setAlignmentX(CENTER_ALIGNMENT);
    this.add(title);

    //The buttons to choose an algorithm
    if (algorithm == null) {
      //Create a button for loading the algorithm
      String[] algorithms = {"AStar", "Dijkstra", "Depth First", "Breadth First"};
      algorithm = new JComboBox<>(algorithms);
    }
    this.add(algorithm);

    //The button to parse an algorithm
    if (parser == null) {
      parser = new JButton("Load Custom Algorithm");
    }
    this.add(parser);
  }

  /**
   * Start the process of solving this maze
   */
  public void solve() {
    //Create a new application for this thread
    Application newApplication = new Application(application);

    //Make a new application class that copies the orignal
    gui.makeAlgoWorkingScreen(this, "AStar", "Loading", false, application);
  }
}
