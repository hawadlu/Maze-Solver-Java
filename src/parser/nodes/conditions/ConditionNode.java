package parser.nodes.conditions;

import parser.nodes.Exec;

public class ConditionNode implements Condition{
  Exec toEvaluate;

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
