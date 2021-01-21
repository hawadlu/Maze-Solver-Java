package parser.nodes.math;

import parser.interfaces.Number;

import java.util.ArrayList;

/**
 * This class deals with adding values together
 */
public class PlusNode implements Number {
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
    return "plus(" + values + ")";
  }

  /**
   * Run through each of the numbers and add them.
   *
   * add the first to the second and then the third etc.
   */
  @Override
  public double calculate() {
    ArrayList<Number> copyValues = new ArrayList<>(values);
    double initialValue = copyValues.remove(0).calculate();

    for (Number num : copyValues) {
      if (num instanceof NumberNode) initialValue += ((NumberNode) num).value;
      else initialValue += num.calculate();
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
