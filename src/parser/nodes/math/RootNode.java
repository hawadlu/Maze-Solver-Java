package parser.nodes.math;

import parser.interfaces.Number;

import java.util.ArrayList;

/**
 * This class deals with adding values together
 */
public class RootNode implements Number {
  final ArrayList<Number> values = new ArrayList<>();

  public void add(Number number) {
    values.add(number);
  }

  @Override
  public String toString() {
    return "root(" + values + ")";
  }

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
