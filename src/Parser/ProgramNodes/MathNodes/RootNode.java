package Parser.ProgramNodes.MathNodes;

import java.util.ArrayList;

/**
 * This class deals with adding values together
 */
public class RootNode implements Number {
  ArrayList<Number> values = new ArrayList<>();

  public void add(Number number) {
    values.add(number);
  }

  @Override
  public String toString() {
    return "root(" + values + ")";
  }

  @Override
  public double calculate() {
    return Math.sqrt(values.get(0).calculate());
  }
}
