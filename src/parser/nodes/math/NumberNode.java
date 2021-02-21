package parser.nodes.math;

import parser.interfaces.Number;
import parser.interfaces.Value;

/**
 * This class is used to represent a number.
 */
public class NumberNode implements Number, Value {
  final double value;

  /**
   * Create the object.
   * @param number the number to be stored.
   */
  public NumberNode(double number) {
    this.value = number;
  }

  /**
   * Return a string representation of the object.
   */
  @Override
  public String toString() {
    return "" + value;
  }

  /**
   * @return the number that is being stored.
   */
  @Override
  public double calculate(boolean DEBUG) {
    return value;
  }

  /**
   * @return a string representation of the type at execution.
   */
  @Override
  public String getType() {
    return "Number";
  }
}
