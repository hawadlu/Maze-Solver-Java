package Image;

import Utility.Colours;
import Utility.Colours.*;
import Utility.Exceptions.InvalidImage;
import Utility.Location;
import Utility.Node;
import Utility.Segment;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Class used to hold the image in memory
 */
public class ImageFile {
  private colEnum[][] imageArray = null;
  private final String filePath;

  /**
   * Copy constrcutor used to copy an image
   *
   * @param oldImage the old image
   */
  public ImageFile(ImageFile oldImage) {
    //Create a new array based on the dimensions of the old one
    this.imageArray = new colEnum[oldImage.imageArray.length][oldImage.imageArray[0].length];

    //remove any solved reset all the white squares
    for (int height = 0; height < oldImage.imageArray.length - 1; height++) {
      for (int width = 0; width < oldImage.imageArray[0].length - 1; width++) {
        if (oldImage.imageArray[height][width] == colEnum.BLACK) this.imageArray[height][width] = colEnum.BLACK;
        else this.imageArray[height][width] = colEnum.WHITE;
      }
    }
    this.filePath = oldImage.filePath;
  }

  public ImageFile(File toLoad) throws InvalidImage {
    System.out.println("Loading image: " + toLoad);
    BufferedImage tmp;
    try {
      tmp = ImageIO.read(toLoad);
    } catch (IOException e) {
      throw new InvalidImage("File is not an image");
    }

    filePath = toLoad.getPath();

    //Make the 2d array of boolean values
    imageArray = new colEnum[tmp.getHeight()][tmp.getWidth()];

    for (int height = 0; height < tmp.getHeight(); height++) {
      for (int width = 0; width < tmp.getWidth(); width++) {
        Color pixelCol = new Color(tmp.getRGB(width, height));
        int whiteMin = 50;

        //Check if the colour is white
        if (pixelCol.getRed() > whiteMin && pixelCol.getGreen() > whiteMin && pixelCol.getBlue() > whiteMin) {
          imageArray[height][width] = colEnum.WHITE;
        } else {
          imageArray[height][width] = colEnum.BLACK;
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
   *
   * @return the buffered image
   */
  public BufferedImage makeImage() {
    colEnum[][] toUse;

    //Scale the array up if required
    if (imageArray.length * imageArray[0].length > Math.pow(2000, 2)) toUse = imageArray;
    else toUse = scaleArray();

    BufferedImage toReturn = new BufferedImage(toUse[0].length, toUse.length, BufferedImage.TYPE_INT_ARGB);

    //Go through the array and make the image
    for (int height = 0; height < toUse.length; height++) {
      for (int width = 0; width < toUse[0].length; width++) {

        //Set the colours
        if (toUse[height][width] == colEnum.WHITE) toReturn.setRGB(width, height, Colours.white);
        else if (toUse[height][width] == colEnum.RED) toReturn.setRGB(width, height, Colours.red);
        else if (toUse[height][width] == colEnum.BLUE) toReturn.setRGB(width, height, Colours.blue);
        else if (toUse[height][width] == colEnum.GREEN) toReturn.setRGB(width, height, Colours.green);
        else toReturn.setRGB(width, height, Colours.black);
      }
    }

    return toReturn;
  }

  /**
   * Create a temporary scaled up array
   *
   * @return the larger array
   */
  private colEnum[][] scaleArray() {
    //Get the desired with and height
    int imageHeight = imageArray.length;
    int imageWidth = imageArray[0].length;
    double minValue = 500;

    //Calculate the new values until both exceed 500
    if (imageHeight < minValue || imageWidth < minValue) {
      //Get the smaller of the two'
      int smaller;
      if (imageWidth < imageHeight) smaller = imageWidth;
      else smaller = imageHeight;

      double scaleFactor = Math.ceil(minValue / smaller);

      imageHeight *= scaleFactor;
      imageWidth *= scaleFactor;
    } else {
      imageHeight *= 3;
      imageWidth *= 3;
    }

    colEnum[][] toReturn = new colEnum[imageHeight][imageWidth];

    int scale = imageHeight / imageArray.length;
    for (int height = 0; height < imageArray.length; height++) {
      //Repeat for each row
      for (int rowPos = 0; rowPos < scale; rowPos++) {
        //Go through the columns in the row
        for (int width = 0; width < imageArray[0].length; width++) {
          for (int colPos = 0; colPos < scale; colPos++) {
            toReturn[height * scale + rowPos][width * scale + colPos] = imageArray[height][width];
          }
        }
      }
    }

    return toReturn;
  }

  /**
   * Create a cropped version of the image
   *
   * @param calculateParams the specifications to crop to.
   * @return the cropped image
   */
  public BufferedImage makeImage(int[] calculateParams) {
    BufferedImage toReturn = new BufferedImage(calculateParams[0], calculateParams[1], BufferedImage.TYPE_INT_RGB);

    int pixAcross = calculateParams[0];
    int pixDown = calculateParams[1];
    int left = calculateParams[2];
    int top = calculateParams[3];

    for (int height = 0; height < top + pixDown; height++) {
      for (int width = left; width < left + pixAcross; width++) {
        //Set the colours
        System.out.println("Width: " + width + " height: " + height + " arr width: " + imageArray[0].length + " arr height: " + imageArray.length + " img width: " + toReturn.getWidth() + " img height: " + toReturn.getHeight());
        if (imageArray[height][width] == colEnum.WHITE) toReturn.setRGB(width, height, Color.WHITE.getRGB());
        else toReturn.setRGB(width, height, Color.BLACK.getRGB());
      }
    }

    return toReturn;
  }

  /**
   * Get the dimensions of the maze
   *
   * @return maze dimensions
   */
  public Dimension getDimensions() {
    return new Dimension(imageArray[0].length, imageArray.length);
  }

  /**
   * Get a specified piece of information about the image
   *
   * @param info the requested info
   * @return the info
   */
  public String getInfo(String info) {
    if (info.equals("path")) return filePath;
    else if (info.equals("name")) return getName();
    else return "Info not found";
  }

  /**
   * Get the name of the file from the file path
   *
   * @return the name
   */
  private String getName() {
    String[] tmp = filePath.split("/");
    return tmp[tmp.length - 1];
  }

  public colEnum[][] getArray() {
    return imageArray;
  }

  /**
   * Fills a specified path in the maze
   *
   * @param path the path to fill
   * @param fill fill in the spaces between the nodes
   */
  public void fillNodePath(ArrayList<Node> path, boolean fill) {
    while (path.size() > 1) {
      Node currentNode = path.remove(0);

      if (fill) {
        Node nextNode = path.get(0);
        int startX = currentNode.getLocation().x;
        int startY = currentNode.getLocation().y;
        int endX = nextNode.getLocation().x;
        int endY = nextNode.getLocation().y;

        drawBetweenNodes(startX, startY, endX, endY, colEnum.RED);
      } else {
        drawNode(currentNode.getLocation(), colEnum.BLUE);
      }
    }
  }

  public void drawNode(Location drawLocation, colEnum colour) {
    imageArray[drawLocation.y][drawLocation.x] = colour;
  }

  /**
   * Fill a path of segments.
   *
   * @param segments the segments to fill
   */
  public void fillSegmentPath(ArrayList<Segment> segments) {
    while (!segments.isEmpty()) {
      Segment segment = segments.remove(0);

      int startX = segment.entry.getLocation().x;
      int startY = segment.entry.getLocation().y;
      int endX = segment.exit.getLocation().x;
      int endY = segment.exit.getLocation().y;

      drawBetweenNodes(startX, startY, endX, endY, colEnum.GREEN);
    }
  }

  private void drawBetweenNodes(int startX, int startY, int endX, int endY, colEnum colour) {
    int x, y;
    //Set the colours at the start and end
    imageArray[startY][startX] = colour;
    imageArray[endY][endX] = colour;

    //Drawing down
    if (startY < endY) {
      y = startY + 1;
      while (y < endY) {
        imageArray[y][startX] = colour;
        y += 1;
      }

      //Drawing up
    } else if (startY > endY) {
      y = startY;
      while (y > endY) {
        imageArray[y][startX] = colour;
        y -= 1;
      }

      //Drawing right
    } else if (startX < endX) {
      x = startX;
      while (x < endX) {
        imageArray[startY][x] = colour;
        x += 1;
      }

      //Drawing left
    } else if (startX > endX) {
      x = startX;
      while (x > endX) {
        imageArray[startY][x] = colour;
        x -= 1;
      }
    }
  }

  /**
   * Remove all of the colour from the image file
   */
  public void reset() {
    for (int height = 0; height < imageArray.length; height++) {
      for (int width = 0; width < imageArray[0].length; width++) {
        colEnum currentCol = imageArray[height][width];
        if (currentCol == colEnum.BLUE || currentCol == colEnum.GREEN || currentCol == colEnum.RED) {
          imageArray[height][width] = colEnum.WHITE;
        }
      }
    }
  }

  /**
   * Save this image
   *
   * @param fileName place to save to
   */
  public void saveImage(String fileName) {
    //Saving the image
    try {
      ImageIO.write(makeImage(), "png", new File(fileName));
      System.out.println("Image saved as " + fileName);
    } catch (Exception e) {
      System.out.println("Unable to save image: " + e);
    }
  }

  /**
   * @return the file path
   */
  private String getFilePath() {
    return filePath;
  }
}
