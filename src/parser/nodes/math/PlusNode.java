package parser.nodes.MathNodes;

import java.util.ArrayList;

/**
 * This class deals with adding values together
 */
public class PlusNode implements Number {
  ArrayList<Number> values = new ArrayList<>();

  public void add(Number number) {
    values.add(number);
  }

  @Override
  public String toString() {
    return "plus(" + values + ")";
  }

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
