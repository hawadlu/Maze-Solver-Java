package Parser.ProgramNodes.MathNodes;

import Parser.Parser;

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
  public double calculate(Parser parser) {
    return Math.sqrt(values.get(0).calculate(parser));
  }

  /**
   * @return the number of arguments that have been provided for this condition.
   */
  public ArrayList<Number> getArguments() {
    return values;
  }
}
