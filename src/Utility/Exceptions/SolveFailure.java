package Utility.Exceptions;

/**
 * todo comment me
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
