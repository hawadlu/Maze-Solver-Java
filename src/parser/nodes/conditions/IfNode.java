package parser.nodes.conditions;

import parser.interfaces.Condition;
import parser.interfaces.Exec;

import java.util.ArrayList;

public class IfNode implements Exec {

  final ArrayList<Exec> statements;
  final Condition ifCondition;
  ElseNode elseNode;

  public IfNode(Condition condition, ArrayList<Exec> statements) {
    this.ifCondition = condition;
    this.statements = statements;
  }

  public void addElse(ElseNode elseNode) {
    this.elseNode = elseNode;
  }

  @Override
  public void validate() {
    //todo implement me
  }

  @Override
  public Object execute() {
    if (ifCondition.evaluate()) {
      for (Exec statement: statements) {
        statement.execute();
      }
    } else if (this.elseNode != null) {
      elseNode.execute();
    }
    return null;
  }

  @Override
  public String toString() {
    StringBuilder toReturn = new StringBuilder();
    StringBuilder internals = new StringBuilder();
    for (Exec statement: statements) internals.append("\t").append(statement).append("\n");

    toReturn.append("if (").append(ifCondition).append("){\n").append(internals).append("}\n");

    if (elseNode != null) toReturn.append(elseNode);

    return toReturn.toString();
  }

  @Override
  public String getExecType() {
    //todo implement me.
    return null;
  }
}
