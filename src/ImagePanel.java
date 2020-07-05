import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

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
        System.out.println("Zooming in");
        if (modified != null) {
            if (modified.getWidth() < 40) {
                GUI.displayMessage(parentComponent, "Cannot zoom in any further");
            } else {
                int endY = modified.imgArray.length - currentX - 10;
                int endX = modified.imgArray[0].length - currentX - 10;
                modified = new ImageFile(modified, currentX + 10, currentY + 10, modified.getWidth() - 20, modified.getWidth() - 20, endX, endY);
            }
        } else{
            if (image.getWidth() < 40) {
                GUI.displayMessage(parentComponent, "Cannot zoom in any further");
            } else {
                int endY = image.imgArray.length - currentX - 10;
                int endX = image.imgArray[0].length - currentX - 10;
                modified = new ImageFile(image, currentX + 10, currentY + 10, image.getWidth() - 20, image.getWidth() - 20, endX, endY);
            }
        }

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
            GUI.displayMessage(parentComponent, "Cannot zoom out any further");
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

        int endY = modified.imgArray.length + 20;
        int endX = modified.imgArray[0].length + 20;

        modified = new ImageFile(image, currentX, currentY, modified.getWidth() + 20, modified.getHeight() + 20, endX, endY);
    }

    /**
     * Pan right by 10px
     */
    public void panRight() {
        if (modified == null || currentX + modified.getWidth() == image.getWidth()) {
            GUI.displayMessage(parentComponent, "Cannot pan right any further");
            return;
        }
        currentX += 10;

        int endY = modified.imgArray.length + currentY;
        int endX = currentX + modified.imgArray[0].length;
        modified = new ImageFile(image, currentX, currentY, modified.getWidth(), modified.getHeight(), endX, endY);
    }

    /**
     * Pan left by 10px
     */
    public void panLeft() {
        if (modified == null || currentX == 0) {
            GUI.displayMessage(parentComponent, "Cannot pan left any further");
            return;
        }
        currentX -= 10;
        int endY = modified.imgArray.length + currentY;
        int endX = modified.imgArray[0].length + currentX;
        modified = new ImageFile(image, currentX, currentY, modified.getWidth(), modified.getHeight(), endX, endY);
    }

    /**
     * Pan up by 10px
     */
    public void panUp() {
        if (modified == null) {
            GUI.displayMessage(parentComponent, "You need to zoom in first");
            return;
        } else if (currentY == 0) {
            GUI.displayMessage(parentComponent, "Cannot pan up any further");
            return;
        }
        currentY -= 10;
        int endY = modified.imgArray.length - currentY;
        int endX = modified.imgArray[0].length;
        modified = new ImageFile(image, currentX, currentY, modified.getWidth(), modified.getHeight(), endX, endY);
    }

    /**
     * Pan down by 10px. Return null if the operation is impossible
     */
    public void panDown() {
        if (modified == null) {
            GUI.displayMessage(parentComponent, "You need to zoom in first");
            return;
        } else if (currentY + modified.getHeight() == image.getHeight()) {
            GUI.displayMessage(parentComponent, "Cannot pan down any further");
            return;
        }
        currentY += 10;
        System.out.println("Img w: " + image.getWidth() + " h: " + image.getHeight());
        System.out.println("Mod w: " + modified.getWidth() + " h: " + modified.getHeight());
        int endY = modified.imgArray.length + currentY;
        int endX = modified.imgArray[0].length;
        modified = new ImageFile(image, currentX, currentY, modified.getWidth(), modified.getHeight(), endX, endY);
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
