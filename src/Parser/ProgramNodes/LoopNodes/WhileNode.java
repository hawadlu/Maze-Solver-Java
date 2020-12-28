package Parser.ProgramNodes.LoopNodes;

import Parser.Parser;
import Parser.ProgramNodes.ConditionNodes.Condition;
import Parser.ProgramNodes.Exec;

import java.util.ArrayList;

public class WhileNode implements Exec {
  ArrayList<Exec> statements = new ArrayList<>();
  Condition loopCondition;

  public WhileNode(Condition condition, ArrayList<Exec> statements) {
    this.loopCondition = condition;
    this.statements = statements;
  }


  @Override
  public Object execute(Parser parser) {

    while (loopCondition.evaluate(parser)) {
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

    return "while (" + loopCondition + ") {\n" + internals + "\n}\n";
  }
}
