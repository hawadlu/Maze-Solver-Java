package parser.programNodes.conditionNodes;

public class NotNode implements Condition {
  Condition condition;

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
