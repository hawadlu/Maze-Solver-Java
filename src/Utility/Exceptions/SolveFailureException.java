package Utility.Exceptions;

public class SolveFailureException extends GenericError {
  public SolveFailureException(String specifics) {
    System.out.println("Solve failed: " + specifics);
  }

}
