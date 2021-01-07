package Parser.ProgramNodes.ConditionNodes;

import Parser.Parser;
import Parser.ProgramNodes.Exec;
import Parser.ProgramNodes.MathNodes.Number;


/**
 * This class is used to check if one var is less than another.
 */
public class GreaterThanNode implements Exec {
  Number valueOne, valueTwo;

  public GreaterThanNode(Number[] conditions) {
    this.valueOne = conditions[0];
    this.valueTwo = conditions[1];
  }

  @Override
  public Object execute(Parser parser) {
    return valueOne.calculate(parser) > valueTwo.calculate(parser);
  }

  @Override
  public String toString() {
    return valueOne + " > " + valueTwo;
  }
}
