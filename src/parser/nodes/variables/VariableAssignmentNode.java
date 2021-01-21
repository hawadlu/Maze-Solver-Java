package parser.nodes.variables;

import parser.Handler;
import parser.Parser;
import parser.interfaces.Exec;
import parser.interfaces.Number;

/**
 * Class used to assign values to variables during execution
 */
public class VariableAssignmentNode implements Exec {
  private final String varName;
  private Exec execVal;
  private Number number;
  private final Handler handler;

  /**
   * Create the object.
   * @param varName The name of the variable.
   * @param execVal The value to be assigned.
   * @param handler The maze handler.
   */
  public VariableAssignmentNode(String varName, Exec execVal, Handler handler) {
    this.varName = varName.replaceAll(" ", "");
    this.execVal = execVal;
    this.handler = handler;
  }

  /**
   * Create the object.
   * @param varName The name of the variable.
   * @param number The value to be assigned.
   * @param handler The maze handler.
   */
  public VariableAssignmentNode(String varName, Number number, Handler handler) {
    this.varName = varName.replaceAll(" ", "");
    this.number = number;
    this.handler = handler;
  }

  /**
   * validate the value to assigned.
   *
   * Note this only checks the execVal because the Number class does not
   * have a validate method.
   */
  @Override
  public void validate() {
    if (execVal instanceof GetVariableNode) {
      //Check that the two types are the same.
      String expectedType = handler.getFromMap(varName).getType();
      String suppliedType = ((GetVariableNode) execVal).extractVariable().getType();

      if (!expectedType.equals(suppliedType)) Parser.fail(varName + " expected type " + expectedType + " but found " + suppliedType, "Execution", null, handler.getPopup());
    } else if (execVal != null) {
      execVal.validate();
    }
  }

  /**
   * Revalidate the object in case anything has changed.
   *
   * If the execVal is not null get the variable from the and assign the execVal to it.
   * Otherwise get the variable from the map and assign the number to it.
   */
  @Override
  public Object execute() {
    //revalidate
    validate();

    if (execVal != null) handler.getFromMap(varName).update(execVal);
    else if (number != null) handler.getFromMap(varName).update(number);

    return null;
  }

  /**
   * Return a string representation of the object.
   */
  @Override
  public String toString() {
    return varName + " equals " + execVal;
  }

  /**
   * Get the type that would be returned when the execute method is called.
   */
  @Override
  public String getExecType() {
    return "VariableAssignmentNode";
  }
}
