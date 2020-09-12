package GUI;

import javax.swing.*;
import java.awt.*;
import Image.*;

/**
 * Class for displaying images
 */
public class ImagePanel extends JPanel {
    private ImageFile image;
    final int panelWidth;
    final int panelHeight;
    final JPanel parentComponent;

    public ImagePanel(ImageFile image, int width, int height, JPanel parentComponent) {
        this.image = image;
        this.panelWidth = width + 10;
        this.panelHeight = height + 10;
        this.parentComponent = parentComponent;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        image.draw(panelWidth, panelHeight, g, image, this);
    }

    /**
     * Zoom in by 10px
     */
    public void zoomIn() {
        if (image.getTrueWidth() - image.getLeftX() < 40 || image.getTrueHeight() - image.getTopY() < 40) {
            GUI.displayMessage(parentComponent, "Cannot zoom in any further");
            return;
        }

        image.setWidth(image.getTrueWidth() - 10);
        image.setHeight(image.getTrueHeight() - 10);
        image.setTopY(image.getTopY() + 10);
        image.setLeftX(image.getLeftX() + 10);
    }

    /**
     * Zoom out by 10px
     */
    public void zoomOut() {
        if (image.getTrueWidth() == image.getCurrentWidth() && image.getTrueHeight() == image.getCurrentHeight() || image.getCurrentWidth() > image.getTrueWidth()) {
            GUI.displayMessage(parentComponent, "Cannot zoom out any further");
            return;
        }

        //On the right edge
        if (image.getLeftX() + image.getCurrentWidth() > image.getTrueWidth()) {
            image.setLeftX(image.getLeftX() - 10);
            image.setWidth(image.getCurrentWidth() - 10);
        }

        //On the left edge
        if (image.getLeftX() == 0) {
            image.setLeftX(image.getLeftX() + 10);
            image.setWidth(image.getCurrentWidth() + 10);
        }

        //On the bottom edge
        if (image.getTopY() + image.getCurrentHeight() > image.getTrueHeight()) {
            image.setTopY(image.getTopY() - 10);
            image.setHeight(image.getCurrentHeight() - 10);
        }

        //On thr top edge
        if (image.getTopY() == 0) {
            image.setTopY(image.getTopY() + 10);
            image.setHeight(image.getCurrentHeight() + 10);
        }

        image.setWidth(image.getCurrentWidth() + 10);
        image.setHeight(image.getCurrentHeight() + 10);
        image.setTopY(image.getTopY() - 10);
        image.setLeftX(image.getLeftX() - 10);
    }

    /**
     * Pan right by 10px
     */
    public void panRight() {
        if (image.getLeftX() == image.getTrueWidth() - (image.getCurrentWidth() - image.getLeftX())) {
            GUI.displayMessage(parentComponent, "Cannot pan right any further");
            return;
        }
        image.setLeftX(image.getLeftX() + 10);
        image.setWidth(image.getCurrentWidth() + 10);
    }

    /**
     * Pan left by 10px
     */
    public void panLeft() {
        if (image.getLeftX() == 0) {
            GUI.displayMessage(parentComponent, "Cannot pan left any further");
            return;
        }
        image.setLeftX(image.getLeftX() - 10);
        image.setWidth(image.getCurrentWidth() - 10);
    }

    /**
     * Pan up by 10px
     */
    public void panUp() {
        if (image.getTopY() == 0) {
            GUI.displayMessage(parentComponent, "Cannot pan up any further");
            return;
        }
        image.setTopY(image.getTopY() - 10);
        image.setHeight(image.getCurrentHeight() - 10);
    }

    /**
     * Pan down by 10px. Return null if the operation is impossible
     */
    public void panDown() {
        if (image.getTopY() == image.getTrueHeight() - (image.getCurrentHeight() - image.getTopY())) {
            GUI.displayMessage(parentComponent, "Cannot pan down any further");
            return;
        }
        image.setTopY(image.getTopY() + 10);
        image.setHeight(image.getCurrentHeight() + 10);
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
