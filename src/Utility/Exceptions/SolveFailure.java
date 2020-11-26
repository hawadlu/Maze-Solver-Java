package Utility.Exceptions;

public class SolveFailure extends GenericError {
  public SolveFailure(String specifics) {
    System.out.println("Solve failed: " + specifics);
  }

}
