package Parser.ProgramNodes;

import Parser.ProgramNodes.MathNodes.Number;
import Parser.ProgramNodes.MathNodes.NumberNode;
import Parser.ProgramNodes.MethodNodes.MethodNode;
import Parser.ProgramNodes.VariableNodes.GetVariableNode;
import Parser.ProgramNodes.VariableNodes.VariableNode;

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

  public EvaluateNode(GetVariableNode variableNode, MethodNode method) {
    this.variableNode = variableNode;
    this.toEvaluate = method;
  }


  @Override
  public Object execute() {
    if (variableNode != null) return variableNode.extractVariable().callMethod((MethodNode) toEvaluate);
    else if (toEvaluate != null) return toEvaluate.execute();
    else if (number != null) return new NumberNode(number.calculate());
    return null;
  }

  @Override
  public double calculate() {
    if (number == null) {
      if (toEvaluate instanceof GetVariableNode) {
        VariableNode var = (VariableNode) toEvaluate.execute();
        Number num = (Number) var.getValue();
        return ((Number) var.getValue()).calculate();
      } else {
        return ((Number) toEvaluate.execute()).calculate();
      }
    }
    return number.calculate();
  }

  @Override
  public void validate() {
    //todo implement me
  }

  @Override
  public String toString() {
    if (variableNode != null) return "Variable: " + variableNode + " Evaluate: " + toEvaluate;
    else return "Evaluate: " + toEvaluate;
  }
}
