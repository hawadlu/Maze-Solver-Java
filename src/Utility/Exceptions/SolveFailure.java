package Utility.Exceptions;

/**
 * Class that is used to represent an exception
 * in the event of a solve failure.
 */
public class SolveFailure extends GenericError {
  /**
   *
   * @param specifics
   */
  public SolveFailure(String specifics) {
    System.out.println("solve failed: " + specifics);
  }

}
