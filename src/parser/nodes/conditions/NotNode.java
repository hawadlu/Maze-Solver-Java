package parser.nodes.conditions;

import parser.interfaces.Condition;

/**
 * This class checks that a condition is not true.
 */
public class NotNode implements Condition {
  final Condition condition;

  /**
   * Create the object.
   * @param condition the condition to evaluate.
   */
  public NotNode(Condition condition) {
    this.condition = condition;
  }

  /**
   * Evaluate the condition and check that it is not true.
   */
  @Override
  public boolean evaluate(boolean DEBUG) {
    return !condition.evaluate(DEBUG);
  }

  /**
   * Return a string representation of the object.
   */
  @Override
  public String toString() {
    return "not(" + condition + ")";
  }

  /**
   *
   * @return
   */
  @Override
  public String getExecType() {
    return "NotNode";
  }
}
