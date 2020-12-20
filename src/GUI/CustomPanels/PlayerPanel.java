package GUI.CustomPanels;

import Application.Application;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Objects;

import Image.ImageFile;

/**
 * This class holds all of the interactions used when playing the game
 */
public class PlayerPanel extends JPanel {
  Application application;
  String playerLabel;
  JComboBox<String> algorithm = null;
  JButton parser = null;
  ImagePanel imagePanel;
  Dimension imageSize;

  public PlayerPanel(Application application, String playerLabel, Dimension maxSize) {
    this.application = application;
    this.playerLabel = playerLabel;
    this.setBackground(Color.CYAN);
    this.setBorder(BorderFactory.createLineBorder(Color.GREEN));
    this.setPreferredSize(maxSize);
    this.imageSize = new Dimension(500, 500);

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
   * Update this panel to display the image to be solved
   */
  public void initSolvePanel() {
    this.removeAll();
    this.imagePanel = new ImagePanel(application.getImage(), imageSize);
    this.add(imagePanel);

    //refresh the gui
    GUI.GUI.refresh();
  }

  /**
   * Update the image
   */
  public void updateImage(ImageFile displayImage) {
    imagePanel.updateImage(displayImage.makeImage());
  }

  /**
   * @return the selected algorithm
   */
  public String getAlgorithm() {
    return Objects.requireNonNull(algorithm.getSelectedItem()).toString();
  }

  /**
   * @param message to display in the panel
   */
  public void displayMessage(String message) {
    this.add(new JLabel("Game message: " + message));

    GUI.GUI.refresh();
  }

  /**
   * Mark this panel as done.
   * @param message the place in which this player finished
   */
  public void markDone(String message, ImageFile completeImage) {
    this.removeAll();
    this.displayMessage(message);
    this.add(new ImagePanel(completeImage.makeImage(), imageSize));
    GUI.GUI.refresh();
  }
}
