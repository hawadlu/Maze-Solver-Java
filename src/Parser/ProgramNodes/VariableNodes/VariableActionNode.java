package Parser.ProgramNodes.VariableNodes;

import Parser.Parser;
import Parser.ProgramNodes.Exec;
import Parser.ProgramNodes.MethodNodes.MethodNode;

/**
 * Perform a given action on a variable
 */
public class VariableActionNode implements Exec {
  String name;
  Exec action;
  public VariableActionNode(String varName, Exec action) {
    this.name = varName.replaceAll(" ", "");
    this.action = action;
  }

  @Override
  public Object execute(Parser parser) {
    //Get the variable out of the stack
    VariableNode toUpdate = parser.variables.get(name);

    if (action instanceof MethodNode) return toUpdate.callMethod((MethodNode) action, parser);

    return null;
  }

  @Override
  public String toString() {
    return name + " " + action;
  }
}
