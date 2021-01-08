package Parser.ProgramNodes.MathNodes;

import Parser.ProgramNodes.Exec;

import java.util.ArrayList;

/**
 * This class deals with adding values together
 */
public class DivideNode implements Exec, Number {
  ArrayList<Number> values = new ArrayList<>();

  public void add(Number number) {
    values.add(number);
  }

  @Override
  public void validate() {
    //todo implement me
  }

  @Override
  public Object execute() {
    return calculate();
  }

  @Override
  public String toString() {
    return "divide(" + values + ")";
  }

  @Override
  public double calculate() {
    ArrayList<Number> copyValues = new ArrayList<>(values);
    double initialValue = copyValues.remove(0).calculate();

    for (Number num : copyValues) {
      if (num instanceof NumberNode) initialValue /= ((NumberNode) num).value;
      else initialValue /= num.calculate();
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
