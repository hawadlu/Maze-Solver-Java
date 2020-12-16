package GUI.CustomPanels;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.nio.Buffer;

/**
 * Class for displaying images
 */
public class ImagePanel extends JPanel {
  private BufferedImage image;
  private Dimension imageDimensions;

  public ImagePanel(BufferedImage image, Dimension imageDimensions) {
    this.image = image;
    this.imageDimensions = imageDimensions;
    
    //Scale the image
    scaleImage();
  }

  private void scaleImage() {
    BufferedImage before = image;
    int w = before.getWidth();
    int h = before.getHeight();
    BufferedImage after = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
    AffineTransform transform = new AffineTransform();

    //Calculate the scale
    double xScale = imageDimensions.getWidth() / image.getWidth();
    double yScale = imageDimensions.getHeight() / image.getHeight();

    transform.scale(xScale, yScale);
    AffineTransformOp scaleOp = new AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR);
    image = scaleOp.filter(before, after);
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    g.drawImage(image, 0, 0, null);
  }

  /**
   * Updated the image to be displayed
   * @param newImage the new image
   */
  public void updateImage(BufferedImage newImage) {
    this.image = newImage;

    scaleImage();

    this.revalidate();
    this.repaint();
  }
}
