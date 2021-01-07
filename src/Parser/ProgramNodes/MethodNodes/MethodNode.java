package Parser.ProgramNodes.MethodNodes;


import Parser.Parser;
import Parser.ProgramNodes.Exec;

import java.util.ArrayList;

/**
 * Class that handles the execution of methods
 */
public class MethodNode implements Exec {
  String name;
  ArrayList<Object> parameters = new ArrayList<>();
  ParameterValidator validator;

  public MethodNode(String name) {
    this.name = name;
    validator = new ParameterValidator(name);
  }

  public MethodNode(String name, ArrayList<Object> parameters) {
    this.name = name.replaceAll(" ", "");

    //Add the parameters, removing all of the spaces
    for (Object obj: parameters) {
      if (obj instanceof String) {
        ((String) obj).replaceAll(" ", "");
      }
      this.parameters.add(obj);
    }

    validator = new ParameterValidator(name, parameters);
  }

  public String getName() {
    return name;
  }

  /**
   * Get the parameters of this method.
   * @return the parameters.
   */
  public ArrayList<Object> getParameters() {
    return parameters;
  }

  @Override
  public Object execute(Parser parser) {
    if (name.equals("add")) {
      Object toReturn = parameters.get(0);
      if (toReturn instanceof String) return parser.variables.get(toReturn);
      else return ((Exec) toReturn).execute(parser);
    }
    return null;
  }

  @Override
  public String toString() {
    if (parameters == null) return "Method (" + name + "())";
    else return "Method (" + name + "(" + parameters + "))";
  }

  /**
   * Check that the supplied parameters are of the correct type
   */
  public void checkParameters(Parser parser) {
    validator.validate(parser);
  }
}
