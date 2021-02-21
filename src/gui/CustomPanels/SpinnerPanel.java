package gui.CustomPanels;

import javax.swing.*;

/**
 * This class deals with drawing the spinner panel;
 */
public class SpinnerPanel extends JPanel {

  /**
   * This class creates a spinning arrows gif that
   * is used to show that the program is currently
   * working on the problem.
   */
  public SpinnerPanel() {
    ImageIcon spinner = new ImageIcon("Animations/Spinning Arrows.gif");

    this.setBackground(null);
    this.add(new JLabel("working...", spinner, JLabel.CENTER));
  }
}
