package Parser.ProgramNodes;

/**
 * This class holds the variable info including the type, name and value object
 */
public class VariableNode implements Exec {
  String type, name;
  Exec value;

  public VariableNode(String type, String name) {
    this.name = name.replaceAll(" ", "");;
    this.type = type.replaceAll(" ", "");;
  }

  public VariableNode(String type, String name, Exec value) {
    this.name = name.replaceAll(" ", "");
    this.type = type.replaceAll(" ", "");;
    this.value = value;
  }

  @Override
  public void Execute() {

  }

  @Override
  public String toString() {
    return type + " " + name + " " + value;
  }
}
