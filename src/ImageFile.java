import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.IOException;

/**
 * Class used to hold the image in memory
 */
public class ImageFile {
    public boolean[][] imgArray;
    public int width;
    public int height;
    private String path;
    byte[][] solved;
    public int leftX = 0;
    public int topY = 0;

    ImageFile(BufferedImage imageIn, String filePath) {
        imgArray = makeImgArray(imageIn);
        this.path = filePath;
    }

    /**
     * Make this a subset of another image
     * @param imageIn the image to crop
     * @param startX start pos
     * @param startY start pos
     * @param width new width
     * @param height new height
     */
    ImageFile(ImageFile imageIn, int startX, int startY, int width, int height, int endX, int endY) {
        //imageIn.imgArray.length - startY
        //imageIn.imgArray[0].length - startX
       this.width = width;
       this.height = height;
       this.imgArray = imageIn.getSubset(startX, startY, endX, endY);

        //Apply to the solved image if it is not null
        if (imageIn.solved != null) {
            this.solved = imageIn.getSolvedSubset(startX, startY, endX, endX);
        }
        this.path = imageIn.path;
    }

    /**
     * Check that the maze is valid. I.E. there is only one entry and one exit.
     * White pixels do not meet diagonally unless there is anther white pixel between them
     * @param imgFile
     */
    //todo implement me
    private void checkMazeValidity(BufferedImage imgFile) {
        throw new Error("Invalid maze");
    }

    /**
     * Make sure that the luminosity of the each pixel is somewhere between 0 - 20 and 235 - 255
     * @param imgFile the image to check
     */
    private void checkImageColour(BufferedImage imgFile) {
        for (int height = 0; height < imgFile.getHeight(); height++) {
            for (int width = 0; width < imgFile.getWidth(); width++) {
                int colour = getColour(imgFile, width, height);
                if (colour > 20 && colour < 235) {
                    throw new Error("Invalid colour at x " + width + " y " + height);
                }
            }
        }
    }

    /**
     * Checks if the user wants to find all the neighbours before or during solving.
     * @param neighbourArg Used when the program is invoked via the command line
     * @param imgFile the image file to process
     * @return the search that the user wants to use
     */
    private boolean getNeighbourMethod(String neighbourArg, BufferedImage imgFile) {
        while (true) {
            if (imgFile.getHeight() * imgFile.getWidth() > Math.pow(6, 2)) {
                //look for command line input
                String answer = "";
                //todo make this into a jpanel
                if (answer.equals("1")) {
                    break;
                } else if (answer.equals("2")) {
                    return false;
                } else {
                    System.out.println("Invalid input. Try again!.");
                }
            } else {
                break;
            }
        }
        return true;
    }

    /**
     * Check for large images and ask the user how they want to proceede.
     * @param largeArg Used when the program is invoked via the command line
     * @param imgFile the image file to process
     */
    private void checkForLargeImage(String largeArg,  ImageFile imgFile, GUI gui) throws IOException {
        //Checking for large images
        while (true) {
            //should be 4000
            if (imgFile.getHeight() * imgFile.getWidth() > Math.pow(1999, 2)) {
                //Look for command line input
                String answer = "";
                //todo make this into a jpanel
                if (answer.equals("y")) {
                    break;
                } else if (answer.equals("n")) {
                    System.out.println("Returning to image selection");
                    gui.loadSolveOptionsGui(imgFile);
                    break;
                } else {
                    System.out.println("Invalid input. Try again!.");
                }
            } else {
                break;
            }
        }
    }

    /**
     * Take the image and make a 2d boolean array that represents it.
     * True for white, false for black
     * @param imgFile
     * @return
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
        imgFile = null;
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

    public boolean[][] getImgArray() {
        return this.imgArray;
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
    //todo make sure this works
    public BufferedImage makeImage() {
        BufferedImage toRet = new BufferedImage(width - leftX, height - topY, BufferedImage.TYPE_INT_ARGB);
        for (int newHeight = 0; newHeight + topY < height; newHeight ++) {
            for (int newWidth = 0; newWidth + leftX < width; newWidth++) {
                //Check if the maze has been solved
                if (solved != null) {
                    if (solved[newHeight + topY][newWidth + leftX] == 0) toRet.setRGB(newWidth, newHeight, Color.BLACK.getRGB());
                    else if (solved[newHeight + topY][newWidth + leftX] == 1) toRet.setRGB(newWidth, newHeight, Color.white.getRGB());
                    else toRet.setRGB(newWidth, newHeight, Color.red.getRGB());
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
    //todo test me
    public void draw(int panelWidth, int panelHeight, Graphics g, ImageFile imageFile, ImageObserver imgObserver) {
        //First make an image
        BufferedImage toPaint = imageFile.makeImage();

        //todo, scale images proportionally so that they are not always square
        BufferedImage resized = new BufferedImage(panelWidth, panelHeight, toPaint.getType());
        Graphics2D g2 = resized.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2.drawImage(toPaint, 0, 0, panelWidth, panelHeight, 0, 0, toPaint.getWidth(), toPaint.getHeight(), null);
        g2.dispose();

        g.drawImage(resized, 0, 0, imgObserver);
    }

    public void initSolvedArr() {
         solved = new byte[imgArray.length][imgArray[0].length];
    }

    /**
     * Change the colour of image
     * 0 is black, 1 is white, 2 is red
     * @param x the xPos
     * @param y the yPos
     * @param col the colour
     */
    public void setRGB(int x, int y, byte col) {
        solved[y][x] = col;
    }

    /**
     * Get and return a subset of the image array
     * @param startY yPos
     * @param startX xPos
     * @return new array
     */
    private boolean[][] getSubset(int startX, int startY, int endX, int endY) {
        boolean[][] toReturn = new boolean[endY - startY][endX - startX];
        for (int i = startY; i < endY; i++) {
            for (int j = startX; j < endX; j++) {
                try {
                    toReturn[i - startY][j - startX] = imgArray[i][j];
                } catch (IndexOutOfBoundsException e) {
                    System.out.println(e);
                }
            }
        }
        return toReturn;
    }

    /**
     * Get and return a subset of the image array
     * @param startY yPos
     * @param startX xPos
     * @return new array
     */
    private byte[][] getSolvedSubset(int startX, int startY, int endX, int endY) {
        byte[][] toReturn = new byte[endY - startY][endX - startX];
        for (int i = startY; i < endY; i++) {
            for (int j = startX; j < endX; j++) {
                try {
                    toReturn[i - startY][j - startX] = solved[i][j];
                } catch (IndexOutOfBoundsException e) {
                    System.out.println(e);
                }
            }
        }
        return toReturn;
    }
}
