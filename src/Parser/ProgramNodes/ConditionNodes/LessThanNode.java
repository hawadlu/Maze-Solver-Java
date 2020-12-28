package Parser.ProgramNodes.ConditionNodes;

import Parser.Parser;
import Parser.ProgramNodes.Exec;
import Parser.ProgramNodes.MathNodes.Number;

/**
 * This class is used to check if one var is less than another.
 */
public class LessThanNode implements Exec {
  Number valueOne, valueTwo;

  public LessThanNode(Number[] conditions) {
    this.valueOne = conditions[0];
    this.valueTwo = conditions[1];
  }

  @Override
  public Object execute(Parser parser) {
    return null;
  }

  @Override
  public String toString() {
    return valueOne + " < " + valueTwo;
  }
}
