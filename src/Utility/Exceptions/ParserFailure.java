package Utility.Exceptions;

public class ParserFailure extends RuntimeException {
  public ParserFailure(String msg) {
    super(msg);
  }
}