package parser.nodes.math;

import parser.interfaces.Number;
import parser.interfaces.Value;

public class NumberNode implements Number, Value {
  final double value;

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
