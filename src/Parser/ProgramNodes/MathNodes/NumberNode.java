package Parser.ProgramNodes.MathNodes;

import Parser.Parser;
import Parser.ProgramNodes.Exec;

public class NumberNode implements Number, Exec {
  double value;

  public NumberNode(double number) {
    this.value = number;
  }

  @Override
  public Object execute() {

    return null;
  }

  @Override
  public void validate() {
    //todo implement me
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
