package parser.nodes;

import parser.interfaces.Exec;

/**
 * Literally an empty statement.
 */
public class EmptyNode implements Exec {

  /**
   * @return empty statement, return null.
   */
  @Override
  public Object execute(boolean DEBUG) {
    return null;
  }

  /**
   * Return a string representation of the object.
   */
  @Override
  public String toString() {
    return "Empty statement";
  }

  /**
   * Literally nothing to validate.
   */
  @Override
  public void validate() {
    //No validation required. This statement will always be empty (there are no variables).
  }

  /**
   * Get the type that would be returned when the execute method is called.
   */
  @Override
  public String getExecType() {
    return "EmptyNode";
  }
}
