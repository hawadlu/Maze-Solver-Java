import java.awt.image.BufferedImage;

/**
 * Class used to hold the image in memory
 */
public class ImageFile {
    private boolean[][] imgArray;
    private int width;
    private int height;

    ImageFile(BufferedImage imageIn) {
        imgArray = makeImgArray(imageIn);
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
                toReturn[height][width] = getColour(imgFile, width, height) != 0;
            }
        }
        height = toReturn.length;
        width = toReturn[0].length;
        imgFile = null;
        return toReturn;
    }

    /**
     * Gets the colour of the specified square (red)
     */
    private int getColour(BufferedImage imgFile, int width, int height) {
        return imgFile.getRGB(width, height) & 0x00ff0000 >> 16;
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
}
