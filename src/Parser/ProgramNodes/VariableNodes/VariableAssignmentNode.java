package Parser.ProgramNodes.VariableNodes;

import Parser.Handler;
import Parser.Parser;
import Parser.ProgramNodes.Exec;
import Parser.ProgramNodes.MathNodes.Number;

/**
 * Class used to assign values to variables during execution
 */
public class VariableAssignmentNode implements Exec {
  private String varName;
  private Exec execVal;
  private Number number;
  private Handler handler;

  public VariableAssignmentNode(String varName, Exec execVal, Handler handler) {
    this.varName = varName.replaceAll(" ", "");
    this.execVal = execVal;
    this.handler = handler;
  }

  public VariableAssignmentNode(String varName, Number number, Handler handler) {
    this.varName = varName.replaceAll(" ", "");
    this.number = number;
    this.handler = handler;
  }

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

  @Override
  public Object execute() {
    //revalidate
    validate();

    if (execVal != null) handler.getFromMap(varName).update(execVal);
    else if (number != null) handler.getFromMap(varName).update(number);

    return null;
  }

  @Override
  public String toString() {
    return varName + " equals " + execVal;
  }

  @Override
  public String getExecType() {
    //todo implement me.
    return null;
  }
}
