package Parser.ProgramNodes.ConditionNodes;

import Parser.Parser;
import Parser.ProgramNodes.Exec;

public class ConditionNode implements Condition{
  Exec toEvaluate;

  public ConditionNode(Exec toEvaluate) {
    this.toEvaluate = toEvaluate;
  }

  @Override
  public boolean evaluate(Parser parser) {
    return (boolean) toEvaluate.execute(parser);
  }

  @Override
  public String toString() {
    return toEvaluate.toString();
  }
}
