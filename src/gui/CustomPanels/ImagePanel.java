package gui.CustomPanels;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * This class is used to display an image as a background
 */
public class ImagePanel extends JFrame {
  BufferedImage toPaint;

  /**
   * Create the window and initialise the bg image
   * @param title the window title.
   */
  public ImagePanel(String title) {
    super(title);

    try {
      toPaint = ImageIO.read(new File("Assets/Backgrounds/Faded Maze.png"));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Draw the image as the background of the window.
   * @param g the graphics object
   */
  @Override
  public void paint(Graphics g) {
    super.paint(g);
    g.drawImage(toPaint, 0, 0, null);
  }
}
