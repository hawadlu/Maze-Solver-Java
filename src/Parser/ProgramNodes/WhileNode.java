package Parser.ProgramNodes;

import java.util.ArrayList;

public class WhileNode implements Exec {
  ArrayList<Exec> statements = new ArrayList<>();
  Condition loopCondition;

  public WhileNode(Condition condition, ArrayList<Exec> statements) {
    this.loopCondition = condition;
    this.statements = statements;
  }


  @Override
  public void Execute() {

  }

  @Override
  public String toString() {
    StringBuilder internals = new StringBuilder();
    for (Exec statement: statements) internals.append("\t").append(statement).append("\n");

    return "while (" + loopCondition + ") {\n" + internals + "\n}\n";
  }
}
