package parser.nodes.conditions;

import parser.interfaces.Condition;
import parser.interfaces.Exec;

/**
 * This class is used to check whether or not s specific condition is true.
 */
public class ConditionNode implements Condition {
  final Exec toEvaluate;

  /**
   * Create the ConditionNode object.
   * @param toEvaluate the Exec node that will be evaluated.
   */
  public ConditionNode(Exec toEvaluate) {
    this.toEvaluate = toEvaluate;
  }

  /**
   * Execute the toEvaluate method and return the result.
   */
  @Override
  public boolean evaluate() {
    return (boolean) toEvaluate.execute();
  }

  /**
   * Return a string representation of the object.
   */
  @Override
  public String toString() {
    return toEvaluate.toString();
  }
}
