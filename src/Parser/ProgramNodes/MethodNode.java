package Parser.ProgramNodes;

import java.util.ArrayList;

/**
 * Class that handles the execution of methods
 */
public class MethodNode implements Exec{
  String name;
  ArrayList<Object> parameters;

  public MethodNode(String name) {
    this.name = name;
  }

  public MethodNode(String name, ArrayList<Object> parameters) {
    this.name = name;
    this.parameters = parameters;
  }

  @Override
  public void Execute() {

  }

  @Override
  public String toString() {
    return super.toString();
  }
}
