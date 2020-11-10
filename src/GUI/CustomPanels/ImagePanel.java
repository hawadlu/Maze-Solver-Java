package GUI.CustomPanels;

import GUI.GUI;
import Utility.Exceptions.GenericError;

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
  BufferedImage imageDisplay; //This is the image that will be displayed on the screen

  public ImagePanel() {
    this.setBackground(Color.red);
  }

  /**
   * This is used to set the image that will be displayed on each of the buttons
   */
  public void setImageDisplay(String link) {
    try {
      imageDisplay = ImageIO.read(new File(link));
    } catch (IOException e) {
      //todo throw a custom error
      e.printStackTrace();
    }
  }

  /**
   * Add hover functionality to the panel
   */
  public void bindHover(Color hoverColour) {
    ImagePanel currentPanel = this;

    //Add the hover effects
    this.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseEntered(MouseEvent e) {
        currentPanel.setBackground(hoverColour);
      }

      @Override
      public void mouseExited(MouseEvent e) {
        currentPanel.setBackground(null);
      }
    });
  }

  /**
   * Make this particular panel into a control panel
   * @param imageLink the image that should be displayed
   * @param action the action that the button will perform.
   * @param hoverColour the colour to be used on hover
   */
  public void setControlButton(String imageLink, String action, Color hoverColour) {
    this.bindHover(hoverColour);
  }
}