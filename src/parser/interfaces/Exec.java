package parser.interfaces;

/**
 * This is used to delineate any classes that are executed
 * instead of evaluated in teh parser.
 *
 * It contains a basic execute method and a method to
 * get the type returned at execution.
 */
public interface Exec extends Validator {
  /**
   * Execute the code block and return any object.
   * @param DEBUG_ON indicate if the program is currently in debug mode
   * @return the object to return , if any.
   */
  Object execute(boolean DEBUG_ON);

  /**
   * @return a string representation of the object.
   */
  String toString();

  /**
   * @return The type that would returned when the code block is executed.
   */
  String getExecType();
}
