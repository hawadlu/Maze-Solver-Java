package Utility.Exceptions;

public class ThreadFailure extends Throwable {
  public ThreadFailure(String specifics) {
      System.out.println("Thread failure: " + specifics);
    }
}
