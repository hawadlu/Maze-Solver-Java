package GUI.CustomPanels;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Class for displaying images
 */
public class ImagePanel extends JPanel {
  private BufferedImage image;
  private Dimension imageDimensions;

  public ImagePanel(BufferedImage image, Dimension imageDimensions) {
    this.image = image;
    this.imageDimensions = imageDimensions;
  }

  private void drawImage(Graphics g) {
    //Scale the image
    Dimension imgSize = new Dimension(image.getWidth(), image.getHeight());
    Dimension bounds = new Dimension((int) imageDimensions.getWidth(), (int) imageDimensions.getHeight());
    Dimension scaled = scale(imgSize, bounds);

    BufferedImage resized = new BufferedImage(scaled.width, scaled.height, image.getType());
    Graphics2D g2 = resized.createGraphics();
    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);

    g2.drawImage(image, 0, 0, (int) imageDimensions.getWidth(), (int) imageDimensions.getHeight(), 0, 0, image.getWidth(), image.getHeight(), null);
    g2.dispose();

    g.drawImage(resized, 0, 0, null);
  }

  Dimension scale(Dimension imgSize, Dimension boundary) {
    double widthRatio = boundary.getWidth() / imgSize.getWidth();
    double heightRatio = boundary.getHeight() / imgSize.getHeight();
    double ratio = Math.min(widthRatio, heightRatio);

    return new Dimension((int) (imgSize.width * ratio), (int) (imgSize.height * ratio));
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    drawImage(g);
  }

  /**
   * Updated the image to be displayed
   * @param newImage the new image
   */
  public void updateImage(BufferedImage newImage) {
    this.image = newImage;

    GUI.GUI.refresh();
  }
}
