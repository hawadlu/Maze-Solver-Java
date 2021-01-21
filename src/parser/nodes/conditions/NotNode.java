package parser.nodes.conditions;

import parser.interfaces.Condition;

public class NotNode implements Condition {
  final Condition condition;

  public NotNode(Condition condition) {
    this.condition = condition;
  }

  @Override
  public boolean evaluate() {
    return !condition.evaluate();
  }

  @Override
  public String toString() {
    return "not(" + condition + ")";
  }
}
