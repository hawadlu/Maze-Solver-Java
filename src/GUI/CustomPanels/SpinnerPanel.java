package GUI.CustomPanels;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;

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
