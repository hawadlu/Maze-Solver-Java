package parser.nodes.math;

import parser.interfaces.Number;

import java.util.ArrayList;

/**
 * This class deals with adding values together
 */
public class MinusNode implements Number {
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
    return "minus(" + values + ")";
  }

  /**
   * Run through each of the numbers and subtract them.
   *
   * Subtract the first from the second and then the third etc.
   */
  @Override
  public double calculate(boolean DEBUG) {
    ArrayList<Number> copyValues = new ArrayList<>(values);
    double initialValue = copyValues.remove(0).calculate(DEBUG);

    for (Number num : copyValues) {
      if (num instanceof NumberNode) initialValue -= ((NumberNode) num).value;
      else initialValue -= num.calculate(DEBUG);
    }

    return initialValue;
  }

  /**
   * @return the number of arguments that have been provided for this condition.
   */
  public ArrayList<Number> getArguments() {
    return values;
  }
}
