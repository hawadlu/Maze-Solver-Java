package Parser.ProgramNodes.VariableNodes;

import Parser.Handler;
import Parser.ProgramNodes.Exec;

/**
 * Class used to assign values to variables during execution
 */
public class VariableAssignmentNode implements Exec {
  String varName;
  Exec newVal;
  private Handler handler;

  public VariableAssignmentNode(String varName, Exec newVal, Handler handler) {
    this.varName = varName.replaceAll(" ", "");
    this.newVal = newVal;
    this.handler = handler;
  }

  @Override
  public void validate() {
    newVal.validate();
  }

  @Override
  public Object execute() {
    handler.getFromMap(varName).update(newVal);
    return null;
  }

  @Override
  public String toString() {
    return varName + " equals " + newVal;
  }
}
