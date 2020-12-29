package Parser.ProgramNodes;

import Parser.Parser;
import Parser.ProgramNodes.MathNodes.Number;
import Parser.ProgramNodes.MathNodes.NumberNode;
import Parser.ProgramNodes.VariableNodes.GetVariableNode;

/**
 * Evaluate and return a number
 */
public class EvaluateNode implements Exec, Number {
  Exec toEvaluate;
  GetVariableNode variableNode;
  Number number;

  public EvaluateNode(Exec toEvaluate) {
    this.toEvaluate = toEvaluate;
  }

  public EvaluateNode(GetVariableNode varNode, Exec toEvaluate) {
    this.variableNode = varNode;
    this.toEvaluate = toEvaluate;
  }

  public EvaluateNode(Number number) {
    this.number = number;
  }


  @Override
  public Object execute(Parser parser) {
    if (toEvaluate != null) return toEvaluate.execute(parser);
    else if (number != null) return new NumberNode(number.calculate(parser));
    return null;
  }

  @Override
  public double calculate(Parser parser) {
    if (number == null) return ((Number) toEvaluate.execute(parser)).calculate(parser);
    return number.calculate(parser);
  }

  @Override
  public String toString() {
    if (variableNode != null) return "Variable: " + variableNode + " Evaluate: " + toEvaluate;
    else return "Evaluate: " + toEvaluate;
  }
}
