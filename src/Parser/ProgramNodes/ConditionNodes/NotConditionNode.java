package Parser.ProgramNodes.ConditionNodes;

public class NotConditionNode implements Condition {
  Condition condition;

  public NotConditionNode(Condition condition) {
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
