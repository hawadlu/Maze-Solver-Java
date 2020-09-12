package Parser;

import Parser.ParserExecutionNode;
import Parser.ParserTypeNode;

/**
 * Classes that hold all the nodes for the parser
 */
public class ParserNode implements ParserExecutionNode {
    @Override
    public void execute() {
        //todo implement me
    }

    public String toString() {
        return null; //todo implement me
    }
}

class TypeNode implements ParserTypeNode {
    String typeName;

    TypeNode(String typeName) {
        this.typeName = typeName;
    }

    @Override
    public String getTypeName() {
        return null;
    }
}
