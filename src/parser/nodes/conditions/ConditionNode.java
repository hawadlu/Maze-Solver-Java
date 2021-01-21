package parser.nodes.conditions;

import parser.interfaces.Condition;
import parser.interfaces.Exec;

public class ConditionNode implements Condition {
  final Exec toEvaluate;

  public ConditionNode(Exec toEvaluate) {
    this.toEvaluate = toEvaluate;
  }

  @Override
  public boolean evaluate() {
    return (boolean) toEvaluate.execute();
  }

  @Override
  public String toString() {
    return toEvaluate.toString();
  }
}
