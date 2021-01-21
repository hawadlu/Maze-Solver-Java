package parser.nodes.conditions;

import parser.interfaces.Exec;

import java.util.ArrayList;

/**
 * This class contains deals with executing code in the else if branch of an if statement.
 */
public class ElseIfNode implements Exec {
  final ArrayList<IfNode> ifNodes;
  ElseNode elseNode;

  /**
   * Create the object.
   * @param ifs an arraylist of all of the conditions and their respective code blocks.
   */
  public ElseIfNode(ArrayList<IfNode> ifs) {
    this.ifNodes = ifs;

    //If the last statement has an else condition add it to the else here
    if (ifNodes.get(ifNodes.size() - 1).elseNode != null) {
      this.elseNode = new ElseNode(ifNodes.get(ifNodes.size() - 1).elseNode.statements);
      ifNodes.get(ifNodes.size() - 1).elseNode = null;
    }
  }

  /**
   * Add an else statement to the current object.
   * @param elseNode a node containing a block of code to be executed.
   */
  public void addElse(ElseNode elseNode) {
    this.elseNode = elseNode;
  }

  /**
   * Call th validate methods on the IfNodes and ElseNode
   */
  @Override
  public void validate() {
    for (IfNode ifNode: ifNodes) ifNode.validate();

    if (elseNode != null) elseNode.validate();
  }

  /**
   * Run through each of the if statements and check their associated conditions.
   * When one condition is true, execute the statement and then stop.
   *
   * If no conditions are true and their is an else statement, execute that.
   * @return this never needs to return anything.
   */
  @Override
  public Object execute() {
    //Go through each of the statements until one is true
    boolean run = false;

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

  /**
   * @return a string representation of the object.
   */
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

  /**
   * @return the type of node that is being executed.
   */
  @Override
  public String getExecType() {
    return "ElseIfNode";
  }
}
