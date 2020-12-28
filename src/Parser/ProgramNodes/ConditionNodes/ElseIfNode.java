package Parser.ProgramNodes.ConditionNodes;

import Parser.Parser;
import Parser.ProgramNodes.Exec;

import java.util.ArrayList;

public class ElseIfNode implements Exec {
  ArrayList<IfNode> ifNodes = new ArrayList<>();
  ElseNode elseNode;

  public ElseIfNode(ArrayList<IfNode> ifs) {
    this.ifNodes = ifs;
  }

  public void addElse(ElseNode elseNode) {
    this.elseNode = elseNode;
  }


  @Override
  public Object execute(Parser parser) {
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
}
