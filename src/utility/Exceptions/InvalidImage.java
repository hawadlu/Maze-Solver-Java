package Utility.Exceptions;

/**
 * This class is used to represent an error thrown
 * by the user trying to use an image that is not
 * a valid maze.
 */
public class InvalidImage extends GenericError {
  /**
   * @param specifics a string containing specific information about the maze.
   */
  public InvalidImage(String specifics) {
    System.out.println("Invalid image: " + specifics);
  }
}
