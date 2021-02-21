package parser.nodes.methods;


import parser.Handler;
import parser.Parser;
import parser.interfaces.Exec;

import java.util.ArrayList;

/**
 * Class that handles the execution of methods
 */
public class MethodNode implements Exec {
  private final String name;
  private final ArrayList<Object> parameters = new ArrayList<>();
  private MethodValidator validator;
  private final Handler handler;

  /**
   * Create the object.
   * @param name the name of the method.
   * @param handler the maze handler.
   */
  public MethodNode(String name, Handler handler) {
    this.name = name;
    this.handler = handler;

    setupValidator();
  }

  /**
   * Create the object.
   * @param name the name of the method.
   * @param parameters the parameters supplied to the method.
   * @param handler the maze handler.
   */
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
    switch (name) {
      case "setCost" -> validator = new MethodValidator(name, new String[]{"MazeNode", "Number"}, parameters, handler);
      case "isDone", "getNeighbourCount", "distanceToDestination", "isVisited", "visit", "add", "getCost", "getNeighbours", "finish" -> validator = new MethodValidator(name, new String[]{"MazeNode"}, parameters, handler);
      case "assignComparator" -> validator = new MethodValidator(name, new String[]{"Comparator"}, parameters, handler);
      case "getStart", "getSize", "getNext", "isEmpty" -> validator = new MethodValidator(name, new String[]{}, parameters, handler);
      case "getDistance", "setParent" -> validator = new MethodValidator(name, new String[]{"MazeNode", "MazeNode"}, parameters, handler);
      case "fail" -> validator = new MethodValidator(name, new String[]{"PrintNode"}, parameters, handler);
      case "get" -> validator = new MethodValidator(name, new String[]{"Number"}, parameters, handler);
      default -> Parser.fail("Unrecognised method " + name, "Execution", null, handler.getPopup());
    }
  }

  /**
   * @return the name of the method.
   */
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

  /**
   * Call the method validator object which will validate the method.
   */
  @Override
  public void validate() {
    //Check that the supplied method has the correct number of arguments and types
    validator.validate();
  }

  /**
   * Check the name of the method and then preform the appropriate action.
   */
  @Override
  public Object execute(boolean DEBUG) {
    if (DEBUG) System.out.println(handler.getPlayer() + " " + getExecType());

    if (name.equals("add")) {
      Object toReturn = parameters.get(0);
      if (toReturn instanceof String) return handler.getFromMap((String) toReturn);
      else return ((Exec) toReturn).execute(DEBUG);
    } else if (name.equals("fail")) {
      return parameters.get(0); //The fail message is supplied at param index 0
    }
    return null;
  }

  /**
   * Return a string representation of the object.
   */
  @Override
  public String toString() {
    if (parameters.size() == 0) return "Method (" + name + "())";
    else return "Method (" + name + "(" + parameters + "))";
  }

  /**
   * Get the type that would be returned when the execute method is called.
   */
  @Override
  public String getExecType() {

    if (name.equals("get")) return "MazeNode";
    return null;
  }
}
