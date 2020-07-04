import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Class used to hold the image in memory
 */
public class ImageFile {
    private boolean[][] imgArray;
    private int width;
    private int height;
    private String path;

    ImageFile(BufferedImage imageIn) {
        imgArray = makeImgArray(imageIn);
    }

    /**
     * Loads the image file. This can take command line arguments for the filepath, solve method, neighbour method and continuing for large imgs.
     * The order is file, large img (y/n), neighbour method(1/2), solve method (1 - 4)
     */
    public ImageFile loadImage(File fileArg, String largeArg, String neighbourArg, String solveArg) {
        BufferedImage in;

        //Checking if the file exists
        try {
            in = ImageIO.read(fileArg);

            path = fileArg.getAbsolutePath();

            BufferedImage imgFile = new BufferedImage(in.getWidth(), in.getHeight(), BufferedImage.TYPE_INT_RGB);

            //Check that the image colour is acceptable
            checkImageColour(imgFile);

            //Check that the maze is valid
            //todo implement this
            //checkMazeValidity(imgFile);

            Graphics2D g = imgFile.createGraphics();
            g.drawImage(in, 0, 0, null);
            g.dispose();
            checkForLargeImage(largeArg, imgFile);

            //Ask the user how they might want to process a large image
            boolean neighbourLoad = getNeighbourMethod(neighbourArg, imgFile);

            //Load the image into a 2d array to save space
            ImageFile imgObj = new ImageFile(imgFile);
            System.out.println("Img height: " + imgObj.getHeight());
            System.out.println("Img width: " + imgObj.getWidth());
            System.out.println("Approximately " + imgObj.getHeight() * imgObj.getWidth() * 0.15 + " nodes");
            return this;
        } catch (Exception e) {
            System.out.println("Program aborted: " + e);
        }
        //todo deal with this
        return null;
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
    private void checkForLargeImage(String largeArg,  BufferedImage imgFile) {
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
                    loadImage(null, null, null, null);
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

    public int getHeight() {return this.height;}
    public int getWidth() {return this.width;}

    public String getAbsolutePath() {
        return this.path;
    }

    /**
     * Make an image using the inout array
     * @return a buffered image
     */
    //todo make sure this works
    public BufferedImage makeImage() {
        BufferedImage toRet = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        for (int height = 0; height < toRet.getHeight(); height ++) {
            for (int width = 0; width < toRet.getWidth(); width++) {
                if (!imgArray[height][width]) toRet.setRGB(width, height, Color.BLACK.getRGB());
                else toRet.setRGB(width, height, Color.WHITE.getRGB());
            }
        }
        return toRet;
    }
}
