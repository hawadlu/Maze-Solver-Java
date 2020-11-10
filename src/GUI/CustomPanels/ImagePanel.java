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

  boolean hoverState = false;

  public ImagePanel() {
    this.setBackground(Color.red);
  }

  /**
   * This is used to set the image that will be displayed on each of the buttons
   */
  public void setActiveImageDisplay(String activeLink, String inactiveLink) {
    try {
      activeImageDisplay = ImageIO.read(new File(activeLink));
      inactiveImageDisplay = ImageIO.read(new File(inactiveLink));
    } catch (IOException e) {
      //todo throw a custom error
      e.printStackTrace();
    }
  }

  /**
   * Add hover functionality to the panel
   */
  public void bindHover() {

    //Add the hover effects
    this.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseEntered(MouseEvent e) {
        hoverState = true;
        GUI.refresh();
      }

      @Override
      public void mouseExited(MouseEvent e) {
        hoverState = false;
        GUI.refresh();
      }
    });
  }

  /**
   * Make this particular panel into a control panel
   * @param activeImageLink the image that should be displayed (the hover state of the control buttons)
   * @param inactiveImageLink the inactive state of the control buttons
   * @param action the action that the button will perform.
   */
  public void setControlButton(String activeImageLink, String inactiveImageLink, String action) {
    this.bindHover();
    this.setActiveImageDisplay(activeImageLink, inactiveImageLink);
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    if (hoverState) g.drawImage(activeImageDisplay, 0, 0, null);
    else g.drawImage(inactiveImageDisplay, 0, 0, null);
  }
}