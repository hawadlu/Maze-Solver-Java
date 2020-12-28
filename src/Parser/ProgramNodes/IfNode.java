package Parser.ProgramNodes;

import Parser.Parser;
import Parser.ProgramNodes.ConditionNodes.Condition;

import java.util.ArrayList;

public class IfNode implements Exec {
  ArrayList<Exec> statements = new ArrayList<>();
  Condition ifCondition;

  public IfNode(Condition condition, ArrayList<Exec> statements) {
    this.ifCondition = condition;
    this.statements = statements;
  }

  @Override
  public Object execute(Parser parser) {
    if (ifCondition.evaluate(parser)) {
      for (Exec statement: statements) {
        statement.execute(parser);
      }
    }
    return null;
  }

  @Override
  public String toString() {
    StringBuilder internals = new StringBuilder();
    for (Exec statement: statements) internals.append("\t").append(statement).append("\n");

    return "if (" + ifCondition + "){\n" + internals + "}\n";
  }
}
