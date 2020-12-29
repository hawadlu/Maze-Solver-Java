package Parser.ProgramNodes.MathNodes;

import Parser.Parser;

import java.util.ArrayList;

/**
 * This class deals with adding values together
 */
public class MinusNode implements Number {
  ArrayList<Number> values = new ArrayList<>();

  public void add(Number number) {
    values.add(number);
  }


  @Override
  public String toString() {
    return "minus(" + values + ")";
  }

  @Override
  public double calculate(Parser parser) {
    ArrayList<Number> copyVals = new ArrayList<>(values);
    double initialValue = copyVals.remove(0).calculate(parser);

    for (Number num : copyVals) {
      if (num instanceof NumberNode) initialValue -= ((NumberNode) num).value;
      else initialValue -= num.calculate(parser);
    }

    return initialValue;
  }
}
