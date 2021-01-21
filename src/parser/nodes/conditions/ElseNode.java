package parser.nodes.conditions;

import parser.nodes.Exec;

import java.util.ArrayList;

/**
 * This class is attached to if nodes and contains the statements that will be run if the
 * else branch is triggered.
 */
public class ElseNode implements Exec {
  ArrayList<Exec> statements = new ArrayList<>();

  public ElseNode(ArrayList<Exec> statements) {
    this.statements = statements;
  }

  @Override
  public Object execute() {
    for (Exec statement: statements) {
      statement.execute();
    }
    return null;
  }

  @Override
  public void validate() {
    //todo implement me
  }

  @Override
  public String toString() {
    StringBuilder internals = new StringBuilder();
    for (Exec statement: statements) internals.append("\t").append(statement).append("\n");

    return "else {\n" + internals + "}\n";
  }

  @Override
  public String getExecType() {
    //todo implement me.
    return null;
  }
}
