package Utility.Exceptions;

/**
 * This class is used to represent any error thrown in the program.
 */
public class GenericError extends Throwable {
  /**
   * Create a new GenericError and print a warning message.
   */
  public GenericError() {
    System.out.println("The program encountered a problem");
  }
}
