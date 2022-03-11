package Utility;

import java.awt.*;

/**
 * Simple class to deal with colouring the maze.
 */
public class Colours {
  public enum colEnum {
    RED,
    WHITE,
    BLACK,
    BLUE,
    GREEN
  }

  public static int white = Color.WHITE.getRGB();
  public static int black = Color.BLACK.getRGB();
  public static int red = Color.RED.getRGB();
  public static int orange = new Color(231, 102, 43).getRGB();
  public static int blue = Color.BLUE.getRGB();
}
