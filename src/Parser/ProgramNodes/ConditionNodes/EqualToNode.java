package Parser.ProgramNodes.ConditionNodes;

import Parser.ProgramNodes.Exec;
import Parser.ProgramNodes.MathNodes.Number;

/**
 * This class is used to check if one var is less than another.
 */
public class EqualToNode implements Exec {
  Number valueOne, valueTwo;

  public EqualToNode(Number[] conditions) {
    this.valueOne = conditions[0];
    this.valueTwo = conditions[1];
  }

  @Override
  public void validate() {
    //todo implement me
  }

  @Override
  public Object execute() {
    return valueOne.calculate() == valueTwo.calculate();
  }

  @Override
  public String toString() {
    return valueOne + " == " + valueTwo;
  }

  @Override
  public String getExecType() {
    //todo implement me.
    return null;
  }
}
