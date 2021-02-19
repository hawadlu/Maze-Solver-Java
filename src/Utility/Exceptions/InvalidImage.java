package Utility.Exceptions;

/**
 * todo comment me
 */
public class InvalidImage extends GenericError {
  /**
   *
   * @param specifics
   */
  public InvalidImage(String specifics) {
    System.out.println("Invalid image: " + specifics);
  }
}
