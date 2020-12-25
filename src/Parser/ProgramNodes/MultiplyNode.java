package Parser.ProgramNodes;

import java.util.ArrayList;

/**
 * This class deals with adding values together
 */
public class MultiplyNode implements Exec{
  ArrayList<Exec> values = new ArrayList<>();

  public void add(Exec number) {
    values.add(number);
  }

  @Override
  public void Execute() {

  }

  @Override
  public String toString() {
    return "multiply(" + values + ")";
  }
}
