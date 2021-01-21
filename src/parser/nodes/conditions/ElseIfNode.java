package parser.nodes.conditions;

import parser.nodes.Exec;

import java.util.ArrayList;

public class ElseIfNode implements Exec {
  ArrayList<IfNode> ifNodes = new ArrayList<>();
  ElseNode elseNode;

  public ElseIfNode(ArrayList<IfNode> ifs) {
    this.ifNodes = ifs;

    //If the last statement has an else condition add it to the else here
    if (ifNodes.get(ifNodes.size() - 1).elseNode != null) {
      this.elseNode = new ElseNode(ifNodes.get(ifNodes.size() - 1).elseNode.statements);
      ifNodes.get(ifNodes.size() - 1).elseNode = null;
    }
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
    //Go through each of the statements until one is true
    Boolean run = false;

    for (IfNode statement: ifNodes) {
      //Check to see if the condition will be evaluated
      if (statement.ifCondition.evaluate()){
        statement.execute();
        run = true;
        break;
      }
    }

    //Run the else node if necessary
    if (!run && elseNode != null) elseNode.execute();

    return null;
  }

  @Override
  public String toString() {

    StringBuilder toReturn = new StringBuilder();
    toReturn.append(ifNodes.get(0));

    for (int i = 1; i < ifNodes.size(); i++) {
      toReturn.append(" else ").append(ifNodes.get(i));
    }

    if (elseNode != null) toReturn.append(elseNode);

    return toReturn.toString();
  }

  @Override
  public String getExecType() {
    //todo implement me.
    return null;
  }
}
