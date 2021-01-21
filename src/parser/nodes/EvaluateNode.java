package parser.nodes;

import parser.interfaces.Exec;
import parser.nodes.conditions.ConditionNode;
import parser.interfaces.Number;
import parser.nodes.math.NumberNode;
import parser.nodes.methods.MethodNode;
import parser.nodes.variables.GetVariableNode;
import parser.nodes.variables.VariableNode;

/**
 * Evaluate and return a number
 */
public class EvaluateNode implements Exec, Number {
  Exec toEvaluate;
  GetVariableNode variableNode;
  Number number;
  ConditionNode conditionNode;

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

  public EvaluateNode(ConditionNode conditionNode) {
    this.conditionNode = conditionNode;
  }


  @Override
  public Object execute() {
    if (variableNode != null) return variableNode.extractVariable().callMethod((MethodNode) toEvaluate);
    else if (toEvaluate != null) return toEvaluate.execute();
    else if (number != null) return new NumberNode(number.calculate());
    else if (conditionNode != null) conditionNode.evaluate();
    return null;
  }

  @Override
  public double calculate() {
    if (number == null) {
      if (toEvaluate instanceof GetVariableNode) {
        VariableNode var = (VariableNode) toEvaluate.execute();
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

  @Override
  public String getExecType() {
    if (toEvaluate != null) return toEvaluate.getExecType();
    else if (number != null) return "Number";
    else if (conditionNode != null) return "Condition";
    return null;
  }
}
