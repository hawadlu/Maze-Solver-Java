package Parser.ProgramNodes.MathNodes;

import Parser.Parser;
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
  public Object execute(Parser parser) {
    return calculate();
  }

  @Override
  public String toString() {
    return "divide(" + values + ")";
  }

  @Override
  public double calculate() {
    ArrayList<Number> copyVals = new ArrayList<>(values);
    double initialValue = copyVals.remove(0).calculate();

    for (Number num : copyVals) {
      if (num instanceof NumberNode) initialValue /= ((NumberNode) num).value;
      else initialValue /= num.calculate();
    }

    return initialValue;
  }
}