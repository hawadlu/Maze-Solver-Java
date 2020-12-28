package Parser.ProgramNodes.ConditionNodes;

import Parser.Parser;

public interface Condition {
  public boolean evaluate(Parser parser);
}
