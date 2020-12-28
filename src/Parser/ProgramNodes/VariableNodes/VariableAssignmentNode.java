package Parser.ProgramNodes.VariableNodes;

import Parser.Parser;
import Parser.ProgramNodes.Exec;

/**
 * Class used to assign values to variables during execution
 */
public class VariableAssignmentNode implements Exec {
  String varName;
  Exec newVal;

  public VariableAssignmentNode(String varName, Exec newVal) {
    this.varName = varName.replaceAll(" ", "");
    this.newVal = newVal;
  }

  @Override
  public Object execute(Parser parser) {
    if (!parser.variables.containsKey(varName)) parser.executionError("Could not find variable " + varName);
    parser.variables.get(varName).update(newVal, parser);
    return null;
  }

  @Override
  public String toString() {
    return varName + " equals " + newVal;
  }
}
