package Utility.Exceptions;

public class InvalidImage extends GenericError{
  public InvalidImage(String specifics) {
    System.out.println("Invalid image: " + specifics);
  }
}
