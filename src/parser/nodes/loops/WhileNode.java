package parser.nodes.loops;

import parser.interfaces.Condition;
import parser.interfaces.Exec;

import java.util.ArrayList;

public class WhileNode implements Exec {
  final ArrayList<Exec> statements;
  final Condition loopCondition;

  public WhileNode(Condition condition, ArrayList<Exec> statements) {
    this.loopCondition = condition;
    this.statements = statements;
  }

  @Override
  public void validate() {
    //todo implement me
  }

  @Override
  public Object execute() {

    while (loopCondition.evaluate()) {
      for (Exec statement : statements) {
        statement.execute();
      }
    }

    return null;
  }

  @Override
  public String toString() {
    StringBuilder internals = new StringBuilder();
    for (Exec statement : statements) internals.append("\t").append(statement).append("\n");

    return "while (" + loopCondition + ") {\n" + internals + "\n}\n";
  }

  @Override
  public String getExecType() {
    //todo implement me.
    return null;
  }
}
