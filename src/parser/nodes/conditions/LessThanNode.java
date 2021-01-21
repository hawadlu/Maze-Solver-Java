package parser.nodes.conditions;

import parser.interfaces.Exec;
import parser.interfaces.Number;


/**
 * This class is used to check if one var is less than another.
 */
public class LessThanNode implements Exec{
  final Number valueOne;
  final Number valueTwo;

  public LessThanNode(Number[] conditions) {
    this.valueOne = conditions[0];
    this.valueTwo = conditions[1];
  }

  @Override
  public void validate() {
    //todo implement me
  }

  @Override
  public Object execute() {
    return valueOne.calculate() < valueTwo.calculate();
  }

  @Override
  public String toString() {
    return valueOne + " < " + valueTwo;
  }

  @Override
  public String getExecType() {
    //todo implement me.
    return null;
  }
}
