package Parser.ProgramNodes;

public class PrintNode implements Exec{
  String toPrint;

  public PrintNode(String string) {
    toPrint = string;
  }

  @Override
  public void Execute() {
    System.out.println(toPrint);
  }

  @Override
  public String toString() {
    return toPrint;
  }
}
