package parser.interfaces;

/**
 * This interface contains methods used for evaluating]
 * and getting the type of any condition used in the parser.
 */
public interface Condition {
  /**
   * Evaluate the condition.
   * @param DEBUG should the program print debug statements as it goes.
   * @return a boolean indicating the result of the evaluation.
   */
  boolean evaluate(boolean DEBUG);

  /**
   * Get the object type returned when run.
   * @return a string indicating the exec type
   */
  String getExecType();
}
