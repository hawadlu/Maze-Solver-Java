package parser.nodes.conditions;

import parser.Handler;
import parser.interfaces.Condition;
import parser.interfaces.Exec;

/**
 * This class is used to check whether or not s specific condition is true.
 */
public class ConditionNode implements Condition {
  final Exec toEvaluate;
  Handler handler;

  /**
   * Create the ConditionNode object.
   * @param toEvaluate the Exec node that will be evaluated.
   */
  public ConditionNode(Exec toEvaluate, Handler handler) {
    this.toEvaluate = toEvaluate;
    this.handler = handler;
  }

  /**
   * Execute the toEvaluate method and return the result.
   */
  @Override
  public boolean evaluate(boolean DEBUG) {
    if (DEBUG) System.out.println(handler.getPlayer() + " " + getExecType());

    return (boolean) toEvaluate.execute(DEBUG);
  }

  /**
   * Return a string representation of the object.
   */
  @Override
  public String toString() {
    return toEvaluate.toString();
  }

  /**
   *
   * @return
   */
  @Override
  public String getExecType() {
    return "ConditionNode";
  }
}
