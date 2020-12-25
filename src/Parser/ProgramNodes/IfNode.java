package Parser.ProgramNodes;

import java.util.ArrayList;

public class IfNode implements Exec {
  ArrayList<Exec> statements = new ArrayList<>();
  Condition ifCondition;

  public IfNode(Condition condition, ArrayList<Exec> statements) {
    this.ifCondition = condition;
    this.statements = statements;
  }

  @Override
  public void Execute() {

  }

  @Override
  public String toString() {
    StringBuilder internals = new StringBuilder();
    for (Exec statement: statements) internals.append("\t").append(statement).append("\n");

    return "if (" + ifCondition + "){\n" + internals + "}\n";
  }
}
