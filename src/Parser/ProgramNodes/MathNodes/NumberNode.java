package Parser.ProgramNodes.MathNodes;

import Parser.Parser;
import Parser.ProgramNodes.Exec;
import Parser.ProgramNodes.MathNodes.Number;

public class NumberNode implements Number, Exec {
  double value;

  public NumberNode(double number) {
    this.value = number;
  }

  @Override
  public Object execute(Parser parser) {

    return null;
  }

  @Override
  public String toString() {
    return "" + value;
  }

  @Override
  public double calculate() {
    return value;
  }
}
