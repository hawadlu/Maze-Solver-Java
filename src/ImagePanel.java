import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Class for displaying images
 */
public class ImagePanel extends JPanel {
    private BufferedImage image;
    private BufferedImage modified = null;
    int panelWidth;
    int panelHeight;
    int zoom = 0;
    int currentX = 0;
    int currentY = 0;

    public ImagePanel(BufferedImage image, int width, int height) {
        this.image = image;
        this.panelWidth = width;
        this.panelHeight = height;
    }

    /**
     * @return the modified image
     */
    public BufferedImage getModified() { return modified; }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        //g.drawImage(image, 50, 50, WIDTH - 100, HEIGHT - 100, this);
        BufferedImage toPaint;
        if (modified != null) toPaint = modified;
        else toPaint = image;

        BufferedImage resized = new BufferedImage(panelWidth, panelHeight, toPaint.getType());
        Graphics2D g2 = resized.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2.drawImage(toPaint, 0, 0, panelWidth, panelHeight, 0, 0, toPaint.getWidth(), toPaint.getHeight(), null);
        g2.dispose();

        g.drawImage(resized, 0, 0, this);
    }

    //todo, make these methods more resilient
    /**
     * Zoom in by 10px
     * @return the zoomed image
     */
    public BufferedImage zoomIn() {
        System.out.println("Zooming in");
        BufferedImage toCrop;
        if (modified != null) {
            toCrop = modified;
        } else{
            toCrop = image;
        }
        modified = toCrop.getSubimage(10, 10, toCrop.getWidth() - 20, toCrop.getHeight() - 20);
        zoom +=10;
        currentX += 10;
        currentY += 10;
        return modified;
    }

    /**
     * Zoom out by 10px
     * @return the zoomed out image
     */
    public BufferedImage zoomOut() {
        zoom -= 10;
        currentX -= 10;
        currentY -= 10;
        System.out.println("Img w: " + image.getWidth() + " h: " + image.getHeight());
        System.out.println("Mod w: " + modified.getWidth() + " h: " + modified.getHeight());
        modified = image.getSubimage(currentX, currentY, modified.getWidth() + 20, modified.getHeight() + 20);
        return modified;
    }

    /**
     * Pan right by 10px
     */
    public BufferedImage panRight() {
        currentX += 10;
        modified = image.getSubimage(currentX, currentY, modified.getWidth(), modified.getHeight());
        return modified;
    }

    /**
     * Pan left by 10px
     */
    public BufferedImage panLeft() {
        currentX -= 10;
        modified = image.getSubimage(currentX, currentY, modified.getWidth(), modified.getHeight());
        return modified;
    }

    /**
     * Pan up by 10px
     */
    public BufferedImage panUp(JPanel parentComponent) {
        if (modified == null) {
            GUI.displayError(parentComponent, "You need to zoom in first");
            return null;
        } else if (currentY < 0) {
            GUI.displayError(parentComponent, "Cannot pan up any further");
            return null;
        }
        currentY -= 10;
        modified = image.getSubimage(currentX, currentY, modified.getWidth(), modified.getHeight());
        return modified;
    }

    /**
     * Pan down by 10px. Return null if the operation is impossible
     */
    public BufferedImage panDown(JPanel parentComponent) {
        if (modified == null) {
            GUI.displayError(parentComponent, "You need to zoom in first");
            return null;
        } else if (currentY + modified.getHeight() > image.getHeight()) {
            GUI.displayError(parentComponent, "Cannot pan down any further");
            return null;
        }
        currentY += 10;
        System.out.println("Img w: " + image.getWidth() + " h: " + image.getHeight());
        System.out.println("Mod w: " + modified.getWidth() + " h: " + modified.getHeight());
        modified = image.getSubimage(currentX, currentY, modified.getWidth(), modified.getHeight());
        return modified;
    }
}
