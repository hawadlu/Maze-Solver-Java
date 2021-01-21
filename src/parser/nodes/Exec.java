package parser.programNodes;

public interface Exec extends Validator {
  /**
   * Execute the code block and return any object.
   * @return the object to return , if any.
   */
  Object execute();

  /**
   * @return a string representation of the object.
   */
  String toString();

  /**
   * @return The type that would returned when the code block is executed.
   */
  String getExecType();
}
