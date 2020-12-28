package Parser.ProgramNodes.MathNodes;

import java.util.ArrayList;

/**
 * This class deals with adding values together
 */
public class PowerNode implements Number {
  ArrayList<Number> values = new ArrayList<>();

  public void add(Number number) {
    values.add(number);
  }

  @Override
  public String toString() {
    return "power(" + values + ")";
  }

  @Override
  public double calculate() {
    ArrayList<Number> copyVals = new ArrayList<>(values);
    double initialValue = copyVals.remove(0).calculate();

    for (Number num : copyVals) {
      if (num instanceof NumberNode) initialValue = Math.pow(initialValue, ((NumberNode) num).value);
      else initialValue = Math.pow(initialValue, num.calculate());
    }

    return initialValue;
  }
}