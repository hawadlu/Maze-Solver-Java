package Parser.ProgramNodes.ConditionNodes;

import Parser.Parser;
import Parser.ProgramNodes.ConditionNodes.Condition;
import Parser.ProgramNodes.Exec;

import java.util.ArrayList;

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
    if (ifCondition.evaluate(parser)) {
      for (Exec statement: statements) {
        statement.execute(parser);
      }
    } else if (this.elseNode != null) {
      elseNode.execute(parser);
    }
    return null;
  }

  @Override
  public String toString() {
    StringBuilder toReturn = new StringBuilder();
    StringBuilder internals = new StringBuilder();
    for (Exec statement: statements) internals.append("\t").append(statement).append("\n");

    toReturn.append("if (" + ifCondition + "){\n" + internals + "}\n");

    if (elseNode != null) toReturn.append(elseNode);

    return toReturn.toString();
  }
}
