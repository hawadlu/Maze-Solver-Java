package parser.nodes;

import parser.Handler;
import parser.interfaces.Exec;
import parser.nodes.conditions.ConditionNode;
import parser.interfaces.Number;
import parser.nodes.math.NumberNode;
import parser.nodes.methods.MethodNode;
import parser.nodes.variables.GetVariableNode;
import parser.nodes.variables.VariableNode;

/**
 * Evaluate and return a number
 */
public class EvaluateNode implements Exec, Number {
  Exec toEvaluate;
  GetVariableNode variableNode;
  Number number;
  ConditionNode conditionNode;
  Handler handler;

  /**
   * Create the object.
   * @param toEvaluate the Exec that should be evaluated.
   */
  public EvaluateNode(Exec toEvaluate, Handler handler) {
    this.toEvaluate = toEvaluate;
    this.handler = handler;
  }

  /**
   * Create the object.
   * @param varNode The variable to use.
   * @param toEvaluate the Exec that should be evaluated.
   */
  public EvaluateNode(GetVariableNode varNode, Exec toEvaluate, Handler handler) {
    this.variableNode = varNode;
    this.toEvaluate = toEvaluate;
    this.handler = handler;
  }

  /**
   * Create the object.
   * @param number The number to evaluate.
   */
  public EvaluateNode(Number number, Handler handler) {
    this.number = number;
    this.handler = handler;
  }

  /**
   * Create the object.
   * @param variableNode The variable to use.
   * @param method The method that should be executed.
   */
  public EvaluateNode(GetVariableNode variableNode, MethodNode method, Handler handler) {
    this.variableNode = variableNode;
    this.toEvaluate = method;
    this.handler = handler;
  }

  /**
   * Create the object.
   * @param conditionNode The condition that should be checked.
   */
  public EvaluateNode(ConditionNode conditionNode, Handler handler) {
    this.conditionNode = conditionNode;
    this.handler = handler;
  }

  /**
   * If variableNode != null get the variable and call toEvaluate on it and return.
   * If toEvaluate != null execute it.
   * If Number != null calculate it and return.
   * If condition != null evaluate it.
   *
   * @return a Variable node or a Number.
   */
  @Override
  public Object execute(boolean DEBUG) {
    if (DEBUG) System.out.println(handler.getPlayer() + " " + getExecType());

    if (variableNode != null) return variableNode.extractVariable().callMethod((MethodNode) toEvaluate, DEBUG);
    else if (toEvaluate != null) return toEvaluate.execute(DEBUG);
    else if (number != null) return new NumberNode(number.calculate(DEBUG));
    else if (conditionNode != null) conditionNode.evaluate(DEBUG);
    return null;
  }

  /**
   * Calculate the number if it != null.
   * @return the result of the calculation.
   */
  @Override
  public double calculate(boolean DEBUG) {
    if (number == null) {
      if (toEvaluate instanceof GetVariableNode) {
        VariableNode var = (VariableNode) toEvaluate.execute(DEBUG);
        return ((Number) var.getValue()).calculate(DEBUG);
      } else {
        return ((Number) toEvaluate.execute(DEBUG)).calculate(DEBUG);
      }
    }
    return number.calculate(DEBUG);
  }

  /**
   * Validate the applicable parameters.
   */
  @Override
  public void validate() {
    if (variableNode != null) variableNode.validate();
    if (toEvaluate != null) toEvaluate.validate();
  }

  /**
   * Return a string representation of the object.
   */
  @Override
  public String toString() {
    if (variableNode != null) return "Variable: " + variableNode + " Evaluate: " + toEvaluate;
    else return "Evaluate: " + toEvaluate;
  }

  /**
   * Get the type that would be returned when the execute method is called.
   */
  @Override
  public String getExecType() {

    if (toEvaluate != null) return toEvaluate.getExecType();
    else if (number != null) return "Number";
    else if (conditionNode != null) return "Condition";
    return null;
  }
}
