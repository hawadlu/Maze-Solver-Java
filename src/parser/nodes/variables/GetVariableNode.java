package parser.nodes.variables;

import parser.Handler;
import parser.Parser;
import parser.interfaces.Exec;

/**
 * Class for getting a referenced variable at runtime
 */
public class GetVariableNode implements Exec {
  final String varName;
  private final Handler handler;

  /**
   * Create the object.
   * @param varName The name of the variable to be retrieved.
   * @param handler the maze handler.
   */
  public GetVariableNode(String varName, Handler handler) {
    this.varName = varName.replaceAll(" ", "");
    this.handler = handler;
  }

  /**
   * @return the corresponding variable from the variable map.
   */
  public VariableNode extractVariable() {
    return handler.getFromMap(varName);
  }

  /**
   * Get the available information about the variable.
   * Calls the toString method on teh variable which returns the required info.
   * @return a String containing the variable information.
   */
  public String getInfo() {
    return handler.getFromMap(varName).print();
  }

  /**
   * Check that the requested variable actually exists.
   */
  @Override
  public void validate() {
    if (!handler.hasVariable(varName)) Parser.fail("Could not find variable '" + varName + "'", "Execution", null, handler.getPopup());
  }

  /**
   * Retrieve the variable from the variable map.
   * @return the retrieved variable object.
   */
  @Override
  public Object execute(boolean DEBUG) {
    if (DEBUG) System.out.println(handler.getPlayer() + " " + getExecType());

    return handler.getFromMap(varName);
  }

  /**
   * Return a string representation of the object.
   */
  @Override
  public String toString() {
    return "get " + varName;
  }

  /**
   * Get the type that would be returned when the execute method is called.
   */
  @Override
  public String getExecType() {

    return handler.getFromMap(varName).getType();
  }
}
