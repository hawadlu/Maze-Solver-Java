package Parser.ProgramNodes;

/**
 * This class deals with calls to maze actions
 */
public class MazeAction implements Exec {
  MethodNode method;

  public MazeAction(MethodNode method) {
    this.method = method;
  }

  @Override
  public void Execute() {

  }

  @Override
  public String toString() {
    return super.toString();
  }
}
