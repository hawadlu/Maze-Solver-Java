import javax.swing.*;
import java.awt.*;

/**
 * Class for displaying images
 */
public class ImagePanel extends JPanel {
    private ImageFile image;
    private ImageFile modified = null;
    int panelWidth;
    int panelHeight;
    int zoom = 0;
    int currentX = 0;
    int currentY = 0;
    JPanel parentComponent;

    public ImagePanel(ImageFile image, int width, int height, JPanel parentComponent) {
        this.image = image;
        this.panelWidth = width + 10;
        this.panelHeight = height + 10;
        this.parentComponent = parentComponent;
    }

    /**
     * @return the modified image
     */
    public ImageFile getModified() { return modified; }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        ImageFile toPaint;
        if (modified != null) toPaint = modified;
        else toPaint = image;
        toPaint.draw(panelWidth, panelHeight, g, toPaint, this);
    }

    //todo, make these methods more resilient
    /**
     * Zoom in by 10px
     */
    //todo make sure this works
    public void zoomIn() {
        if (image.width < 40 || image.height < 40) {
            GUI.displayMessage(parentComponent, "Cannot zoom in any further");
            return;
        }

        image.width -= 10;
        image.height -= 10;
        image.topY += 10;
        image.leftX += 10;
    }

    /**
     * Zoom out by 10px
     * @return the zoomed out image
     */
    public void zoomOut() {
        if (image.width == image.imgArray[0].length || image.height == image.imgArray.length) {
            GUI.displayMessage(parentComponent, "Cannot zoom out any further");
            return;
        }

        //On the right edge
        if (image.leftX + image.width > image.imgArray[0].length) {
            image.leftX -= 10;
            image.width -= 10;
        }

        //On the left edge
        if (image.leftX == 0) {
            image.leftX += 10;
            image.width += 10;
        }

        //On the bottom edge
        if (image.topY + image.height > image.imgArray.length) {
            image.topY -= 10;
            image.height -= 10;
        }

        //On thr top edge
        if (image.topY == 0) {
            image.topY += 10;
            image.height += 10;
        }

        image.width += 10;
        image.height += 10;
        image.topY -= 10;
        image.leftX -= 10;
    }

    /**
     * Pan right by 10px
     */
    public void panRight() {
        if (image.leftX == image.imgArray[0].length - (image.width - image.leftX)) {
            GUI.displayMessage(parentComponent, "Cannot pan right any further");
            return;
        }
        image.leftX += 10;
        image.width += 10;
    }

    /**
     * Pan left by 10px
     */
    public void panLeft() {
        if (image.leftX == 0) {
            GUI.displayMessage(parentComponent, "Cannot pan left any further");
            return;
        }
        image.leftX -= 10;
        image.width -= 10;
    }

    /**
     * Pan up by 10px
     */
    public void panUp() {
        if (image.topY == 0) {
            GUI.displayMessage(parentComponent, "Cannot pan up any further");
            return;
        }
        image.topY -= 10;
        image.height -= 10;
    }

    /**
     * Pan down by 10px. Return null if the operation is impossible
     */
    public void panDown() {
        if (image.topY == image.imgArray.length - (image.height - image.topY)) {
            GUI.displayMessage(parentComponent, "Cannot pan down any further");
            return;
        }
        image.topY += 10;
        image.height += 10;
    }

    /**
     * @return the current state of the image (modified)
     */
    public ImageFile getImage() {
        if (modified != null) return modified;
        return image;
    }

    /**
     * @return the original image
     */
    public ImageFile getOriginalImage() {
        return image;
    }

    /**
     * If the maze has been solved, put the solved image here
     * @param solvedImg the solved maze
     */
    public void setImage(ImageFile solvedImg) {
        image = solvedImg;
    }
}
