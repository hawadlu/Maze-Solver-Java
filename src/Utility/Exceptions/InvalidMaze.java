package Utility.Exceptions;

/**
 * todo comment me
 */
public class InvalidMaze extends GenericError {

  /**
   *
   * @param specifics
   */
  public InvalidMaze(String specifics) {
    System.out.println("Invalid maze: " + specifics);
  }
}
