package Parser.ProgramNodes.MathNodes;

import Parser.ProgramNodes.Value;

public class NumberNode implements Number, Value {
  double value;

  public NumberNode(double number) {
    this.value = number;
  }


  @Override
  public String toString() {
    return "" + value;
  }

  @Override
  public double calculate() {
    return value;
  }

  @Override
  public String getType() {
    return "Number";
  }
}
