package Parser.ProgramNodes;

import java.util.ArrayList;

public class WhileLoop implements Exec {
  ArrayList<Exec> statements = new ArrayList<>();

  public void addStatement(Exec statement) {
    statements.add(statement);
  }

  @Override
  public void Execute() {

  }

  @Override
  public String toString() {
    return super.toString();
  }
}
