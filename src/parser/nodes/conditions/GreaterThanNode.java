package parser.nodes.conditions;

import parser.Handler;
import parser.Parser;
import parser.interfaces.Exec;
import parser.interfaces.Number;


/**
 * This class is used to check if one var is less than another.
 */
public class GreaterThanNode implements Exec {
  final Number valueOne;
  final Number valueTwo;
  final Handler handler;

  /**
   * Create the object.
   * @param conditions A Number array containing the conditions to evaluate.
   */
  public GreaterThanNode(Number[] conditions, Handler handler) {
    this.valueOne = conditions[0];
    this.valueTwo = conditions[1];
    this.handler = handler;
  }

  /**
   * Validate the contents of the object.
   * Ensure that none of the values are null.
   */
  @Override
  public void validate() {
    if (valueOne == null || valueTwo == null) Parser.fail("Equality condition cannot be null", "Execution", null, handler.getPopup());
  }

  /**
   * Call the calculate method on each of the values and check that value one is larger than value two.
   */
  @Override
  public Object execute() {
    return valueOne.calculate() > valueTwo.calculate();
  }

  /**
   * Return a string representation of the object.
   */
  @Override
  public String toString() {
    return valueOne + " > " + valueTwo;
  }

  /**
   * Get the type that would be returned when the execute method is called.
   */
  @Override
  public String getExecType() {
    return "GreaterThanNode";
  }
}
