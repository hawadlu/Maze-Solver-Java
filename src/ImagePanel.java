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
    JPanel parentComponent;

    public ImagePanel(BufferedImage image, int width, int height, JPanel parentComponent) {
        this.image = image;
        this.panelWidth = width;
        this.panelHeight = height;
        this.parentComponent = parentComponent;
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

        //todo, scale images proportionally so that they are not always square
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
     */
    public void zoomIn() {
        System.out.println("Zooming in");
        BufferedImage toCrop;
        if (modified != null) {
            toCrop = modified;
        } else{
            toCrop = image;
        }

        //Only crop if the image is large enough
        if (toCrop.getWidth() < 40) {
            GUI.displayError(parentComponent, "Cannot zoom in any further");
            return;
        }
        modified = toCrop.getSubimage(10, 10, toCrop.getWidth() - 20, toCrop.getHeight() - 20);
        zoom +=10;
        currentX += 10;
        currentY += 10;
    }

    /**
     * Zoom out by 10px
     * @return the zoomed out image
     */
    public void zoomOut() {
        if (modified == null || modified.getHeight() == image.getHeight() && modified.getWidth() == image.getWidth()) {
            GUI.displayError(parentComponent, "Cannot zoom out any further");
            return;
        }
        //Deal with the cases where the image is at the far left or right
        if (currentX == 0) {
            //Do nothing todo remove
        } else if (currentX + modified.getWidth() == image.getWidth()) {
            currentX -= 20;
        } else {
            currentX -= 10;
        }

        //Deal with the cases where y is at the top or bottom
        if (currentY == 0) {
            //Do nothing todo remove
        } else if (currentY + modified.getHeight() == image.getHeight()) {
            currentY -= 20;
        } else {
            currentY -= 10;
        }

        System.out.println("Img w: " + image.getWidth() + " h: " + image.getHeight());
        System.out.println("Mod w: " + modified.getWidth() + " h: " + modified.getHeight());

        modified = image.getSubimage(currentX, currentY, modified.getWidth() + 20, modified.getHeight() + 20);
    }

    /**
     * Pan right by 10px
     */
    public void panRight() {
        if (modified == null || currentX + modified.getWidth() == image.getWidth()) {
            GUI.displayError(parentComponent, "Cannot pan right any further");
            return;
        }
        currentX += 10;
        modified = image.getSubimage(currentX, currentY, modified.getWidth(), modified.getHeight());
    }

    /**
     * Pan left by 10px
     */
    public void panLeft() {
        if (modified == null || currentX == 0) {
            GUI.displayError(parentComponent, "Cannot pan left any further");
            return;
        }
        currentX -= 10;
        modified = image.getSubimage(currentX, currentY, modified.getWidth(), modified.getHeight());
    }

    /**
     * Pan up by 10px
     */
    public void panUp() {
        if (modified == null) {
            GUI.displayError(parentComponent, "You need to zoom in first");
            return;
        } else if (currentY == 0) {
            GUI.displayError(parentComponent, "Cannot pan up any further");
            return;
        }
        currentY -= 10;
        modified = image.getSubimage(currentX, currentY, modified.getWidth(), modified.getHeight());
    }

    /**
     * Pan down by 10px. Return null if the operation is impossible
     */
    public void panDown() {
        if (modified == null) {
            GUI.displayError(parentComponent, "You need to zoom in first");
            return;
        } else if (currentY + modified.getHeight() == image.getHeight()) {
            GUI.displayError(parentComponent, "Cannot pan down any further");
            return;
        }
        currentY += 10;
        System.out.println("Img w: " + image.getWidth() + " h: " + image.getHeight());
        System.out.println("Mod w: " + modified.getWidth() + " h: " + modified.getHeight());
        modified = image.getSubimage(currentX, currentY, modified.getWidth(), modified.getHeight());
    }

    /**
     * @return the current state of the image (modified)
     */
    public BufferedImage getImage() {
        if (modified != null) return modified;
        return image;
    }

    /**
     * @return the original image
     */
    public BufferedImage getOrignalImage() {
        return image;
    }
}
