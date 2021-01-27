package parser.nodes.variables;

import parser.Parser;
import parser.interfaces.Exec;
import parser.Handler;
import parser.interfaces.Number;
import parser.nodes.methods.MethodNode;

/**
 * Perform a given action on a variable
 */
public class VariableActionNode implements Exec {
  final String name;
  Exec action;
  Number number;
  private final Handler handler;

  /**
   * Create the object.
   * @param varName The name of the variable.
   * @param action The action to perform.
   * @param handler The maze handler.
   */
  public VariableActionNode(String varName, Exec action, Handler handler) {
    this.name = varName.replaceAll(" ", "");
    this.action = action;
    this.handler = handler;
  }

  /**
   * Create the object.
   * @param varName The name of the variable.
   * @param number A number which may be used to update the variable contents.
   * @param handler The maze handler.
   */
  public VariableActionNode(String varName, Number number, Handler handler) {
    this.name = varName;
    this.number = number;
    this.handler = handler;
  }

  /**
   * Get the variable out of the map.
   *
   * If applicable use the action variable to update the variable object.
   * Otherwise use the number variable.
   */
  @Override
  public Object execute(boolean DEBUG) {
    //Get the variable out of the stack
    VariableNode toUpdate = handler.getFromMap(name);

    if (action != null && action instanceof MethodNode) return toUpdate.callMethod((MethodNode) action, DEBUG);
    else if (number != null) toUpdate.update(number.calculate(DEBUG), DEBUG);

    return null;
  }

  /**
   * Validate the update method.
   *
   * Make sure that only certain methods are being used.
   */
  @Override
  public void validate() {
    //Check to see if the method is compatible with the variable type
    if (action instanceof MethodNode) {
      VariableNode variable = handler.getFromMap(name);
      String methodName = ((MethodNode) action).getName();
      String variableType = variable.getType();

      //The assign comparator method can only be called on a priority queue
      if (methodName.equals("assignComparator") && !variableType.equals("PriorityQueue")) {
        Parser.fail("assignComparator cannot be used with type " + variable.getType() + " for variable " + variable.getName(), "Execution", null, handler.getPopup());

        //The add method can only be called on a queue, priority queue or stack.
      } else if ((methodName.equals("add") || methodName.equals("isEmpty") || methodName.equals("getNext") || methodName.equals("getSize")) && !variable.isCollection()) {
        Parser.fail("add cannot be used with type " + variable.getType() + " for variable " + variable.getName(), "Execution", null, handler.getPopup());
      }
    }

    action.validate();
  }

  /**
   * Return a string representation of the object.
   */
  @Override
  public String toString() {
    return name + " " + action;
  }

  /**
   * Get the type that would be returned when the execute method is called.
   */
  @Override
  public String getExecType() {
    return "VariableActionNode";
  }
}
