package Parser.ProgramNodes;

/**
 * This class deals with calls to maze actions
 */
public class MazeActionNode implements Exec {
  MethodNode methodNode;

  public MazeActionNode(MethodNode methodNode) {
    this.methodNode = methodNode;
  }

  @Override
  public void Execute() {

  }

  @Override
  public String toString() {
    return super.toString();
  }
}
