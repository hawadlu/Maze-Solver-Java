package GUI.CustomPanels;

import GUI.GUI;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

//todo add the img control buttons to this panel
public class ImagePanel extends JPanel {
  BufferedImage activeImageDisplay; //This is the image that will be displayed on the screen (on hover for the control buttons)
  BufferedImage inactiveImageDisplay; //Dimmer version of the control buttons when they are not being hovered over
  Dimension panelDimensions;
  GUI gui;
  JScrollPane scrollPane;

  /**
   * Create the image panel
   * @param panelDimensions the requested dimensions of the panel
   */
  public ImagePanel(Dimension panelDimensions, GUI gui) {
    this.panelDimensions = panelDimensions;
    this.gui = gui;
  }

  @Override
  protected void paintComponent(Graphics g) {
    System.out.println("Painting the image");
    g.drawImage(gui.getImage(), 0, 0, this);
  }
}