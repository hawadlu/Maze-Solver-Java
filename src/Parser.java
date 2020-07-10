import customExceptions.ParserFailureException;
import parserInterfaces.ParserExecutionNode;
import parserInterfaces.ParserTypeNode;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * This class contains all of the methods needed to parse the program.
 */
public class Parser {
    JPanel parentComponent;

    /**
     * Parse the program
     * @param fileIn the file to parse
     */
    public void setupParser(File fileIn, JPanel parentComponent) throws FileNotFoundException, ParserFailureException {
        this.parentComponent = parentComponent;
        Scanner programScanner = new Scanner(fileIn);

        //Setup the delimiter
        programScanner.useDelimiter("\\s+|(?=[{}(),;<>])|(?<=[{}(),;<>])");

        ParserExecutionNode program = parseProgram(programScanner);
    }

    /**
     * Run the program
     */
    public void execute() {
    }

    /**
     * Get the path that was created by the program
     * @return the path
     */
    public ArrayList<MazeNode> getPath() {
        return new ArrayList<>();
    }

    private ParserExecutionNode parseProgram(Scanner programScanner) throws ParserFailureException {
        ArrayList<ParserExecutionNode> statements = new ArrayList<>();

        //Add statements until empty
        while (programScanner.hasNext()) {
            System.out.println(Regex.statement);
            //Look for the next statement regex
            if (programScanner.hasNext(Regex.statement)) {
                statements.add(parseStatement(programScanner));
            } else {
                throw new ParserFailureException(programScanner.next() + " is not a valid statement", parentComponent);
            }
        }
        return null; //todo should not return null
    }

    /**
     * Parse a statement
     * @param programScanner the scanner
     * @return the statement object
     */
    private ParserExecutionNode parseStatement(Scanner programScanner) throws ParserFailureException {
        System.out.println("Parsing statement");

        if (programScanner.hasNext(Regex.dataStructure)) return parseDataStructure(programScanner);

        if (!programScanner.hasNext(Regex.semiColon)) throw new ParserFailureException("Statement missing ';'", parentComponent);
        return null; //todo remove?
    }

    /**
     * Parse any data structures
     * @param programScanner the scanner
     * @return the new node
     */
    private ParserExecutionNode parseDataStructure(Scanner programScanner) throws ParserFailureException {
        System.out.println("Parsing data structure");

        //Decide on the type of data structure
        if (programScanner.hasNext(Regex.hashmap)) return parseHashMap(programScanner);

        throw new ParserFailureException("Invalid data structure", parentComponent);
    }

    /**
     * Parse a hashmap
     * @param programScanner the scanner
     * @return the node
     */
    private ParserExecutionNode parseHashMap(Scanner programScanner) throws ParserFailureException {
        //Discard the hashmap token
        programScanner.next();

        ParserTypeNode entry = null;
        ParserTypeNode value = null;

        //Check for an opening <
        if (!programScanner.hasNext(Regex.openPointy)) throw new ParserFailureException("Hashmap missing opening '<'", parentComponent);

        programScanner.next(); //discard the <

        //Process the first object
        if (programScanner.hasNext(Regex.object)) entry = parseTypeArgument(programScanner);
        else throw new ParserFailureException("Map missing opening argument", parentComponent);

        //Check for and discard comma if required
        if (!programScanner.hasNext(Regex.comma)) throw new ParserFailureException("Map missing ','", parentComponent);
        else programScanner.next();

        //Process the second object
        if (programScanner.hasNext(Regex.object)) value = parseTypeArgument(programScanner);
        else throw new ParserFailureException("Map missing second argument", parentComponent);

        //Check for closing >
        if (!programScanner.hasNext(Regex.closePointy)) throw new ParserFailureException("Map missing closing '>'", parentComponent);

        programScanner.next(); //discard the >

        //todo create the variable

        //todo create the map node

        return null; //todo implement this
    }

    /**
     * Parse types for maps etc
     * @param programScanner the scanner
     * @return the object
     */
    private ParserTypeNode parseTypeArgument(Scanner programScanner) throws ParserFailureException {
        if (programScanner.hasNext(Regex.coordinate)) return new TypeNode(programScanner.next());
        if (programScanner.hasNext(Regex.mazeNode)) return new TypeNode(programScanner.next());

        throw new ParserFailureException(programScanner.next() + " is not a valid type", parentComponent);
    }


    /**
     * Simple class to print the scanner
     */
    class PrintScanner {
        /**
         * Print the scanner for debugging
         * @param s the scanner
         */
        private void printScanner(Scanner s) {
            while (s.hasNext()) System.out.println(s.next());
        }
    }
}
