package parser.nodes.math;

import parser.interfaces.Number;

import java.util.ArrayList;

/**
 * This class deals with adding values together
 */
public class RootNode implements Number {
  final ArrayList<Number> values = new ArrayList<>();

  /**
   * Create the object.
   * @param number a number object that will be used later.
   */
  public void add(Number number) {
    values.add(number);
  }

  /**
   * Return a string representation of the object.
   */
  @Override
  public String toString() {
    return "root(" + values + ")";
  }

  /**
   * @return the square root of the supplied value.
   */
  @Override
  public double calculate() {
    return Math.sqrt(values.get(0).calculate());
  }

  /**
   * @return the number of arguments that have been provided for this condition.
   */
  public ArrayList<Number> getArguments() {
    return values;
  }
}
