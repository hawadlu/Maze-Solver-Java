package Utility.Exceptions;

/**
 * This class represents any exception thrown as the result of
 * an invalid maze.
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
