package utility.Exceptions;

/**
 * Class that is used to represent an exception
 * in the event of a solve failure.
 */
public class SolveFailure extends GenericError {
  /**
   * @param specifics String containing information specific to the exception.
   */
  public SolveFailure(String specifics) {
    System.out.println("solve failed: " + specifics);
  }

}
