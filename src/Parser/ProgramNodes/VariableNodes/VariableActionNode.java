package Parser.ProgramNodes.VariableNodes;

import Parser.Parser;
import Parser.ProgramNodes.Exec;
import Parser.Handler;
import Parser.ProgramNodes.MathNodes.Number;
import Parser.ProgramNodes.MethodNodes.MethodNode;

/**
 * Perform a given action on a variable
 */
public class VariableActionNode implements Exec {
  String name;
  Exec action;
  Number number;
  private Handler handler;

  public VariableActionNode(String varName, Exec action, Handler handler) {
    this.name = varName.replaceAll(" ", "");
    this.action = action;
    this.handler = handler;
  }

  public VariableActionNode(String varName, Number nunber, Handler handler) {
    this.name = varName;
    this.number = nunber;
    this.handler = handler;
  }

  @Override
  public Object execute() {
    //Get the variable out of the stack
    VariableNode toUpdate = handler.getFromMap(name);

    if (action != null && action instanceof MethodNode) return toUpdate.callMethod((MethodNode) action);
    else if (number != null) toUpdate.update(number.calculate());

    return null;
  }

  @Override
  public void validate() {
    //Check to see if the method is compatible with the variable type
    if (action instanceof MethodNode) {
      VariableNode variable = handler.getFromMap(name);
      String methodName = ((MethodNode) action).getName();
      String variableType = variable.getType();

      //The assign comparator method can only be called on a priority queue
      if (methodName.equals("assignComparator") && !variableType.equals("PriorityQueue")) {
        Parser.fail("assignComparator cannot be used with type " + variable.getType() + " for variable " + variable.getName(), null);

        //The add method can only be called on a queue, priority queue or stack.
      } else if ((methodName.equals("add") || methodName.equals("isEmpty") || methodName.equals("getNext") || methodName.equals("getSize")) && !variable.isCollection()) {
        Parser.fail("add cannot be used with type " + variable.getType() + " for variable " + variable.getName(), null);
      }
    }

    action.validate();
  }

  @Override
  public String toString() {
    return name + " " + action;
  }
}
