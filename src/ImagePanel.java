import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Class for displaying images
 */
public class ImagePanel extends JPanel {
    private BufferedImage image;
    int panelWidth;
    int panelHeight;

    public ImagePanel(File file, int width, int height) {
        this.panelWidth = width;
        this.panelHeight = height;
        try {
            image = ImageIO.read(new File(String.valueOf(file)));
        } catch (IOException ex) {
            throw new Error("Failed to load image for display!");
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        //g.drawImage(image, 50, 50, WIDTH - 100, HEIGHT - 100, this);

        //Scale the image
        int newWidth = panelWidth - 100;
        int newHeight = panelHeight - 100;
        BufferedImage resized = new BufferedImage(newWidth, newHeight, image.getType());
        Graphics2D g2 = resized.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2.drawImage(image, 0, 0, newWidth, newHeight, 0, 0, image.getWidth(),
                image.getHeight(), null);
        g2.dispose();

        g.drawImage(resized, 50, 50, this); // see javadoc for more info on the parameters
    }
}
