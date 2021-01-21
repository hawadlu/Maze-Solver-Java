package parser.nodes.MethodNodes;


import parser.Handler;
import parser.Parser;
import parser.nodes.Exec;

import java.util.ArrayList;

/**
 * Class that handles the execution of methods
 */
public class MethodNode implements Exec {
  private String name;
  private ArrayList<Object> parameters = new ArrayList<>();
  private MethodValidator validator;
  private Handler handler;

  public MethodNode(String name, Handler handler) {
    this.name = name;
    this.handler = handler;

    setupValidator();
  }

  public MethodNode(String name, ArrayList<Object> parameters, Handler handler) {
    this.name = name.replaceAll(" ", "");
    this.handler = handler;

    //Add the parameters, removing all of the spaces
    for (Object obj : parameters) {
      if (obj instanceof String) {
        String value = ((String) obj).replaceAll(" ", "");
        this.parameters.add(value);
      } else {
        this.parameters.add(obj);
      }
    }

    setupValidator();
  }

  /**
   * Setup the validator object with the expected argument count and types
   */
  private void setupValidator() {
    if (name.equals("setCost")) validator = new MethodValidator(name, new String[]{"MazeNode", "Number"}, parameters, handler);
    else if (name.equals("isDone")) validator = new MethodValidator(name, new String[]{"MazeNode"}, parameters, handler);
    else if (name.equals("getNeighbours")) validator = new MethodValidator(name, new String[]{"MazeNode"}, parameters, handler);
    else if (name.equals("getCost")) validator = new MethodValidator(name, new String[]{"MazeNode"}, parameters, handler);
    else if (name.equals("assignComparator")) validator = new MethodValidator(name, new String[]{"Comparator"}, parameters, handler);
    else if (name.equals("getStart")) validator = new MethodValidator(name, new String[]{}, parameters, handler);
    else if (name.equals("add")) validator = new MethodValidator(name, new String[]{"MazeNode"}, parameters, handler);
    else if (name.equals("isEmpty")) validator = new MethodValidator(name, new String[]{}, parameters, handler);
    else if (name.equals("getNext")) validator = new MethodValidator(name, new String[]{}, parameters, handler);
    else if (name.equals("visit")) validator = new MethodValidator(name, new String[]{"MazeNode"}, parameters, handler);
    else if (name.equals("finish")) validator = new MethodValidator(name, new String[]{}, parameters, handler);
    else if (name.equals("getSize")) validator = new MethodValidator(name, new String[]{}, parameters, handler);
    else if (name.equals("isVisited")) validator = new MethodValidator(name, new String[]{"MazeNode"}, parameters, handler);
    else if (name.equals("getDistance")) validator = new MethodValidator(name, new String[]{"MazeNode", "MazeNode"}, parameters, handler);
    else if (name.equals("distanceToDestination")) validator = new MethodValidator(name, new String[]{"MazeNode"}, parameters, handler);
    else if (name.equals("setParent")) validator = new MethodValidator(name, new String[]{"MazeNode", "MazeNode"}, parameters, handler);
    else if (name.equals("getNeighbourCount")) validator = new MethodValidator(name, new String[]{"MazeNode"}, parameters, handler);
    else if (name.equals("fail")) validator = new MethodValidator(name, new String[]{"PrintNode"}, parameters, handler);
    else if (name.equals("get")) validator = new MethodValidator(name, new String[]{"Number"}, parameters, handler);
    else Parser.fail("Unrecognised method " + name, "Execution", null, handler.getPopup());
  }

  public String getName() {
    return name;
  }

  /**
   * Get the parameters of this method.
   *
   * @return the parameters.
   */
  public ArrayList<Object> getParameters() {
    return parameters;
  }

  @Override
  public void validate() {
    //Check that the supplied method has the correct number of arguments and types
    validator.validate();
  }

  @Override
  public Object execute() {
    if (name.equals("add")) {
      Object toReturn = parameters.get(0);
      if (toReturn instanceof String) return handler.getFromMap(toReturn);
      else return ((Exec) toReturn).execute();
    } else if (name.equals("fail")) {
      return parameters.get(0); //The fail message is supplied at param index 0
    }
    return null;
  }

  @Override
  public String toString() {
    if (parameters == null) return "Method (" + name + "())";
    else return "Method (" + name + "(" + parameters + "))";
  }

  @Override
  public String getExecType() {
    if (name.equals("get")) return "MazeNode";
    return null;
  }
}
