package Parser.ProgramNodes;

public class PrintNode implements Exec{
  StringBuilder toPrint = new StringBuilder();

  public void append(StringBuilder str) {
    toPrint.append(str);
  }

  public void append(Exec print) {
    toPrint.append(" ").append(print.toString());
  }

  @Override
  public void Execute() {
    System.out.println(toPrint);
  }

  @Override
  public String toString() {
    return toPrint.toString();
  }
}
