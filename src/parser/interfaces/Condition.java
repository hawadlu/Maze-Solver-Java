package parser.interfaces;

/**
 * This interface contains methods used for evaluating]
 * and getting the type of any condition used in the parser.
 */
public interface Condition {
  /**
   *
   * @param DEBUG
   * @return
   */
  boolean evaluate(boolean DEBUG);

  /**
   *
   * @return
   */
  String getExecType();
}
