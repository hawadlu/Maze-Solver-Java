import customExceptions.InvalidColourException;
import customExceptions.InvalidMazeException;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * Class used to hold the image in memory
 */
public class ImageFile implements Cloneable {
    public boolean[][] imgArray;
    public int width;
    public int height;
    private String path = "";
    byte[][] solved;
    public int leftX = 0;
    public int topY = 0;
    public Coordinates entry;
    public Coordinates exit;
    public HashSet<Segment> segments = null; //Used when drawing the Minimum spanning tree
    public HashSet<APNode> artPoints = null; //Used when drawing the articulation points

    @Override
    public ImageFile clone() throws CloneNotSupportedException {
        ImageFile cloned = null;
        cloned = (ImageFile) super.clone();
        return cloned;
    }

    ImageFile(BufferedImage imageIn, String filePath, JPanel parentComponent) throws InvalidColourException, InvalidMazeException {
        //Check the image colour
        checkImageColour(imageIn, parentComponent);

        //Make the array
        imgArray = makeImgArray(imageIn);

        //Make sure there is only one entry and one exit
        checkEntriesAndExits(parentComponent);

        this.path = filePath;
    }

    /**
     * Check that there is only one entry and one exit
     */
    private void checkEntriesAndExits(JPanel parentComponent) throws InvalidMazeException {
        //ArrayList containing the coordinates of each of the entries and exits
        ArrayList<Coordinates> entries = new ArrayList<>();

        //Look at the top row.
        for (int width = 0; width < getWidth(); width++) {
            if (isWhite(width, 0)) {
                entries.add(new Coordinates(width, 0));
            }
        }

        //Look at the bottom row
        for (int width = 0; width < getWidth(); width++) {
            if (isWhite(width, getHeight() - 1)) {
                entries.add(new Coordinates(width, getHeight() - 1));
            }
        }

        //Look at the left side
        for (int height = 0; height < getHeight(); height++) {
            if (isWhite(0, height)) {
                entries.add(new Coordinates(0, height));
            }
        }

        //Look at the right side
        for (int height = 0; height < getHeight(); height++) {
            if (isWhite(getWidth() - 1, height)) {
                entries.add(new Coordinates(getWidth() - 1, height));
            }
        }

        if (entries.size() != 2) throw new InvalidMazeException("Maze must have one entry and one exit", parentComponent);

        entry = entries.get(0);
        exit = entries.get(1);
    }

    /**
     * Make sure that the luminosity of the each pixel is somewhere between 0 - 20 and 235 - 255
     * @param imgFile the image to check
     */
    private void checkImageColour(BufferedImage imgFile, JPanel parentComponent) throws InvalidColourException {
        for (int height = 0; height < imgFile.getHeight(); height++) {
            for (int width = 0; width < imgFile.getWidth(); width++) {
                int colour = getColour(imgFile, width, height);
                if (colour > 50 && colour < 715) {
                    throw new InvalidColourException(parentComponent, "Invalid colour at x " + width + " y " + height);
                }
            }
        }
    }

    /**
     * Take the image and make a 2d boolean array that represents it.
     * True for white, false for black
     * @param imgFile the png/jpg/etc file
     * @return a 2d array representation of the image
     */
    private boolean[][] makeImgArray(BufferedImage imgFile) {
        boolean[][] toReturn = new boolean[imgFile.getHeight()][imgFile.getWidth()];
        for (int height = 0; height < imgFile.getHeight(); height++) {
            for (int width = 0; width < imgFile.getWidth(); width++) {
                //Allow any combination of rgb values < 100 to be counted as black
                toReturn[height][width] = getColour(imgFile, width, height) > 100;
            }
        }
        height = toReturn.length;
        width = toReturn[0].length;
        return toReturn;
    }

    /**
     * Return the combined colour value of the specified square
     */
    private int getColour(BufferedImage imgFile, int width, int height) {
        Color col = new Color(imgFile.getRGB(width, height));
        return col.getRed() + col.getBlue() + col.getGreen();
    }

    /**
     * Check if this pixel is white
     * @param x location
     * @param y location
     * @return true/false
     */
    public boolean isWhite(int x, int y) {
        return imgArray[y][x];
    }

    public int getHeight() {return this.imgArray.length;}
    public int getWidth() {return this.imgArray[0].length;}

    public String getAbsolutePath() {
        return this.path;
    }

    /**
     * Make an image using the inout array
     * @return a buffered image
     */
    public BufferedImage makeImage() {
        BufferedImage toRet = new BufferedImage(width - leftX, height - topY, BufferedImage.TYPE_INT_ARGB);
        for (int newHeight = 0; newHeight + topY < height; newHeight ++) {
            for (int newWidth = 0; newWidth + leftX < width; newWidth++) {
                //Check if the maze has been solved
                if (solved != null) {
                    if (solved[newHeight + topY][newWidth + leftX] == 0) toRet.setRGB(newWidth, newHeight, Color.BLACK.getRGB());
                    else if (solved[newHeight + topY][newWidth + leftX] == 1) toRet.setRGB(newWidth, newHeight, Color.white.getRGB());
                    else if (solved[newHeight + topY][newWidth + leftX] == 2) toRet.setRGB(newWidth, newHeight, Color.red.getRGB());
                    else if (solved[newHeight + topY][newWidth + leftX] == 3) toRet.setRGB(newWidth, newHeight, Color.green.getRGB());
                    else if (solved[newHeight + topY][newWidth + leftX] == 4) toRet.setRGB(newWidth, newHeight, Color.blue.getRGB());
                    else if (solved[newHeight + topY][newWidth + leftX] == 5) toRet.setRGB(newWidth, newHeight, Color.yellow.getRGB());
                } else {
                    if (!imgArray[newHeight + topY][newWidth + leftX]) toRet.setRGB(newWidth, newHeight, Color.BLACK.getRGB());
                    else toRet.setRGB(newWidth, newHeight, Color.WHITE.getRGB());
                }
            }
        }
        return toRet;
    }

    public void resetZoom() {
        width = imgArray[0].length;
        height = imgArray.length;
        topY = 0;
        leftX = 0;
    }

    /**
     * Draw the image
     */
    public void draw(int panelWidth, int panelHeight, Graphics g, ImageFile imageFile, ImageObserver imgObserver) {
        //First make an image
        BufferedImage toPaint = imageFile.makeImage();

        //Scale the image
        Dimension imgSize = new Dimension(imgArray[0].length, imgArray.length);
        Dimension bounds = new Dimension(panelWidth, panelHeight);
        Dimension scaled = scale(imgSize, bounds);

        BufferedImage resized = new BufferedImage(scaled.width, scaled.height, toPaint.getType());
        Graphics2D g2 = resized.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2.drawImage(toPaint, 0, 0, panelWidth, panelHeight, 0, 0, toPaint.getWidth(), toPaint.getHeight(), null);
        g2.dispose();

        g.drawImage(resized, 0, 0, imgObserver);
    }

    Dimension scale(Dimension imgSize, Dimension boundary) {
        double widthRatio = boundary.getWidth() / imgSize.getWidth();
        double heightRatio = boundary.getHeight() / imgSize.getHeight();
        double ratio = Math.min(widthRatio, heightRatio);

        return new Dimension((int) (imgSize.width * ratio), (int) (imgSize.height * ratio));
    }

    public void initSolvedArr() {
         solved = new byte[imgArray.length][imgArray[0].length];
    }

    /**
     * Change the colour of image
     * 0 is black, 1 is white, 2 is red, 3 is green, 4 is blue, 5 is yellow
     * @param x the xPos
     * @param y the yPos
     * @param col the colour
     */
    public void setRGB(int x, int y, byte col) {
        solved[y][x] = col;
    }

    /**
     * Reset removing the solved array, segments and art points
     */
    public void reset() {
        solved = null;
        segments = null;
        artPoints = null;
    }

    /**
     * Paints white squares in the solved array
     */
    public void initWhiteSquares() {
        for (int i = 0; i < imgArray.length; i++) {
            for (int j = 0; j < imgArray[0].length; j++) {
                if (imgArray[i][j]) setRGB(j, i, (byte) 1);
            }
        }
    }
}
