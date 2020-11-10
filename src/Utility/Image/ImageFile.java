package Utility.Image;

import Utility.Exceptions.InvalidImage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Class used to hold the image in memory
 * todo expand this to deal with the colours needed to show solved mazes. Probably just a method to make a buffered image.
 */
public class ImageFile {
  Boolean[][] imageArray;
  
  public ImageFile(File toLoad) throws InvalidImage {
    System.out.println("Loading image: " + toLoad);
    BufferedImage tmp;
    try {
      tmp = ImageIO.read(toLoad);
    } catch (IOException e) {
      throw new InvalidImage("File is not an image");
    }
    
    //Make the 2d array of boolean values
    imageArray = new Boolean[tmp.getHeight()][tmp.getWidth()];
    
    for (int height = 0; height < tmp.getHeight(); height++) {
      for (int width = 0; width < tmp.getWidth(); width++) {
        Color pixelCol = new Color(tmp.getRGB(width, height));
        
        //Check if the colour is white
        if (pixelCol.getRed() == 255 && pixelCol.getGreen() == 255 && pixelCol.getBlue() == 255) {
          imageArray[height][width] = true;
        } else {
          imageArray[height][width] = false;
        }
      }
    }
    
    System.out.println("Constructed image array");
    //printImageArray();
    
    //Discard the original buffered image and call the garbage collector
    tmp = null;
    System.gc();
  }
  
  /**
   * Utility method used for testing
   */
  private void printImageArray() {
    for (int height = 0; height < imageArray.length; height++) {
      for (int width = 0; width < imageArray[0].length; width++) {
        System.out.print(imageArray[height][width] + ", ");
      }
      System.out.print("\n");
    }
  }

  /**
   * Make a buffered image
   * @return the buffered image
   * todo refactor to deal with colour
   */
  public BufferedImage makeImage() {
    BufferedImage toReturn = new BufferedImage(imageArray[0].length, imageArray.length, BufferedImage.TYPE_INT_RGB);

    //Go through the array and make the image
    for (int height = 0; height < imageArray.length; height++) {
      for (int width = 0; width < imageArray[0].length; width++) {
        //Set the colours
        if (imageArray[height][width]) toReturn.setRGB(width, height, Color.WHITE.getRGB());
        else toReturn.setRGB(width, height, Color.BLACK.getRGB());
      }
    }

    return toReturn;
  }

  /**
   * Make image
   */
}
