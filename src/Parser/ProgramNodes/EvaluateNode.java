package Parser.ProgramNodes;

import Parser.Parser;
import Parser.ProgramNodes.MathNodes.Number;
import Parser.ProgramNodes.VariableNodes.GetVariableNode;

/**
 * Evaluate and return a number
 */
public class EvaluateNode implements Exec, Number {
  Exec toEvaluate;
  GetVariableNode variableNode;

  public EvaluateNode(Exec toEvaluate) {
    this.toEvaluate = toEvaluate;
  }

  public EvaluateNode(GetVariableNode varNode, Exec toEvaluate) {
    this.variableNode = varNode;
    this.toEvaluate = toEvaluate;
  }


  @Override
  public Object execute(Parser parser) {
    return null;
  }

  @Override
  public double calculate() {
    //todo, will probably have to cast to an number node (instanceof)
    return 0;
  }

  @Override
  public String toString() {
    if (variableNode != null) return "Variable: " + variableNode + " Evaluate: " + toEvaluate;
    else return "Evaluate: " + toEvaluate;
  }
}
