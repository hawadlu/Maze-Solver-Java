package Parser.ProgramNodes.ConditionNodes;

import Parser.Parser;

public class NotConditionNode implements Condition {
  Condition condition;

  public NotConditionNode(Condition condition) {
    this.condition = condition;
  }

  @Override
  public boolean evaluate(Parser parser) {
    return !condition.evaluate(parser);
  }

  @Override
  public String toString() {
    return "not(" + condition + ")";
  }
}
