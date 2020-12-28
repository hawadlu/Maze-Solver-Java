package Parser.ProgramNodes.ConditionNodes;

import Parser.Parser;
import Parser.ProgramNodes.ConditionNodes.Condition;
import Parser.ProgramNodes.Exec;

import java.util.ArrayList;

//todo deal with else statements
public class IfNode implements Exec {

  ArrayList<Exec> statements = new ArrayList<>();
  Condition ifCondition;
  ElseNode elseNode;

  public IfNode(Condition condition, ArrayList<Exec> statements) {
    this.ifCondition = condition;
    this.statements = statements;
  }

  public void addElse(ElseNode elseNode) {
    this.elseNode = elseNode;
  }

  @Override
  public Object execute(Parser parser) {
    //todo refactor to deal with else
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
