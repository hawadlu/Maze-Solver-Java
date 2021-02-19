package parser.interfaces;

/**
 * todo comment me
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
