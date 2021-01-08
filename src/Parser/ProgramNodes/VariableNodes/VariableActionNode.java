package Parser.ProgramNodes.VariableNodes;

import Parser.ProgramNodes.Exec;
import Parser.Handler;
import Parser.ProgramNodes.MethodNodes.MethodNode;

/**
 * Perform a given action on a variable
 */
public class VariableActionNode implements Exec {
  String name;
  Exec action;
  private Handler handler;

  public VariableActionNode(String varName, Exec action, Handler handler) {
    this.name = varName.replaceAll(" ", "");
    this.action = action;
    this.handler = handler;
  }

  @Override
  public Object execute() {
    //Get the variable out of the stack
    VariableNode toUpdate = handler.getFromMap(name);

    if (action instanceof MethodNode) return toUpdate.callMethod((MethodNode) action);

    return null;
  }

  @Override
  public void validate() {
    //todo implement me
  }

  @Override
  public String toString() {
    return name + " " + action;
  }
}
