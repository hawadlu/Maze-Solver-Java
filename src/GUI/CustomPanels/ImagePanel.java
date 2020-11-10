package GUI.CustomPanels;

import GUI.GUI;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.nio.Buffer;

//todo add the img control buttons to this panel
public class ImagePanel extends JPanel {
  BufferedImage activeImageDisplay; //This is the image that will be displayed on the screen (on hover for the control buttons)
  BufferedImage inactiveImageDisplay; //Dimmer version of the control buttons when they are not being hovered over
  Dimension panelDimensions;
  GUI gui;

  boolean hoverState = false;
  boolean isMaze = false;

  /**
   * Create the image panel
   * @param panelDimensions the requested dimensions of the panel
   */
  public ImagePanel(Dimension panelDimensions, boolean isMaze, GUI gui) {
    this.panelDimensions = panelDimensions;
    this.setPreferredSize(panelDimensions);
    this.setBackground(new Color(255, 255, 255, 0));
    this.isMaze = isMaze;
    this.gui = gui;
  }

  /**
   * This is used to set the image that will be displayed on each of the buttons
   * @param activeLink link to the image that should be displayed on hover
   * @param inactiveLink link to the image that should be displayed when not on hover
   */
  public void setImageDisplay(String activeLink, String inactiveLink) {
    try {
      activeImageDisplay = ImageIO.read(new File(activeLink));
      inactiveImageDisplay = ImageIO.read(new File(inactiveLink));
    } catch (IOException e) {
      //todo throw a custom error
      e.printStackTrace();
    }
  }

  /**
   * Scale the image
   * @param imgSize the current image size.
   * @param boundary the max image size.
   * @return the new dimensions.
   */
  Dimension calculateDimensions(Dimension imgSize, Dimension boundary) {
    double widthRatio = boundary.getWidth() / imgSize.getWidth();
    double heightRatio = boundary.getHeight() / imgSize.getHeight();
    double ratio = Math.min(widthRatio, heightRatio);

    return new Dimension((int) (imgSize.width * ratio), (int) (imgSize.height * ratio));
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
    this.setImageDisplay(activeImageLink, inactiveImageLink);
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    if (hoverState) {
      g.drawImage(activeImageDisplay, 0, 0, null);
    } else {
      if (isMaze) {
        //First make an image
        BufferedImage toPaint = gui.getImage();

        //Scale the image
        Dimension imgSize = new Dimension(toPaint.getWidth(), toPaint.getHeight());
        Dimension bounds = new Dimension(panelDimensions.width, panelDimensions.height);
        Dimension scaled = calculateDimensions(imgSize, bounds);

        BufferedImage resized = new BufferedImage(scaled.width, scaled.height, toPaint.getType());
        Graphics2D g2 = resized.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2.drawImage(toPaint, 0, 0, panelDimensions.width, panelDimensions.height, 0, 0, toPaint.getWidth(), toPaint.getHeight(), null);
        g2.dispose();

        g.drawImage(resized, 0, 0, this);
      } else {
        g.drawImage(inactiveImageDisplay, 0, 0, null);
      }
    }
  }
}