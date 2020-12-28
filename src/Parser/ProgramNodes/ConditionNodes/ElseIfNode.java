package Parser.ProgramNodes.ConditionNodes;

import Parser.Parser;
import Parser.ProgramNodes.Exec;

import java.util.ArrayList;

public class ElseIfNode implements Exec {
  ArrayList<IfNode> ifNodes = new ArrayList<>();
  ArrayList<Exec> statements = new ArrayList<>(); //These will be the 'else' part of the if else statement

  public ElseIfNode(ArrayList<IfNode> ifs) {
    this.ifNodes = ifs;
  }


  @Override
  public Object execute(Parser parser) {
    return null;
  }

  @Override
  public String toString() {
    //todo implement me
    return null;
  }
}
