package Parser;

import Parser.ProgramNodes.*;
import Utility.Exceptions.ParserFailure;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/**
 * This class is responsible for parsing the files.
 */
public class Parser {
  Scanner fileScanner = null;
  Exec baseNode;
  HashMap<Object, String> variables;

  public Parser(File toRead) {
    try {
      fileScanner = new Scanner(toRead);

      fileScanner.useDelimiter(Regex.delimiter);

      System.out.println("Statement: " + Regex.statement);
      System.out.println("Method call: " + Regex.methodCall);
      System.out.println("Maze: " + Regex.mazeCall);
      System.out.println("Name: " + Regex.name);
      System.out.println("Scentence: " + Regex.sentence);
      System.out.println("Print: " + Regex.print);
      System.out.println("Print concat: " + Regex.printConcatenate);
      System.out.println("Declare var: " + Regex.declareVar);
      System.out.println("Use var: " + Regex.useVar);
      System.out.println("Reassign var: " + Regex.reassignVar);
      System.out.println("While: " + Regex.whileLoop);
      System.out.println("For: " + Regex.forLoop);
      System.out.println("If: " + Regex.ifStmt);
      System.out.println("Declaration: " + Regex.declaration);


      System.out.println();

      //Start the scanner
      baseNode = parseProgram(fileScanner);

      fileScanner.close();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (ParserFailure e) {
      System.out.println("parser error");
      System.out.println(e.getMessage());
      fileScanner.close();
    }
  }

  private Exec parseProgram(Scanner fileScanner) {
    ArrayList<Exec> statements = new ArrayList<>();

    //Add statements to the list until the scanner is empty
//    printScanner(fileScanner);
    while (fileScanner.hasNext()) {
      if (fileScanner.hasNext(Regex.statement)) {
        statements.add(parseStatement(fileScanner));
      } else {
        fail(fileScanner.next() + " is not a valid statement");
      }
    }

    return new StatementNode();
  }

  /**
   * Parse the program statements.
   * @param fileScanner the file scanner.
   * @return the statement node
   */
  private Exec parseStatement(Scanner fileScanner) {
    Exec toReturn = null;
    System.out.println("parsing statement");

    //Remove any newline characters
    String next = fileScanner.next().replaceAll("\\n", "");

    System.out.println("Next: " + next);

    if (next.matches(Regex.print.pattern())) toReturn = parsePrint(fileScanner);
    else if (next.matches(Regex.declaration.pattern())) toReturn = parseDeclaration(fileScanner, next);
    else if (next.matches(Regex.name.pattern())) toReturn = parseVariableReference(fileScanner, next);
    else if (next.matches(Regex.whileLoop.pattern())) toReturn = parseWhile(fileScanner);

    if (!fileScanner.hasNext(Regex.semiColon)) fail("Statement missing ;");
    else fileScanner.next();

    return toReturn;
  }

  /**
   * Parse while loops.
   * @param fileScanner the file scanner.
   * @return a while loop object
   */
  private Exec parseWhile(Scanner fileScanner) {
    System.out.println("Parsing while");

    //Check for an opening brace
    if (!fileScanner.hasNext(Regex.openParen)) fail("While loop missing opening '('");
    else fileScanner.next();

    Condition condition;
    if (fileScanner.hasNext(Regex.not)) condition = parseNotCondition(fileScanner);
    else condition = parseCondition(fileScanner);

    if (!fileScanner.hasNext(Regex.closeParen)) fail("While loop missing closing ')'");
    else fileScanner.next();

    //Check for the opening {
    System.out.println("Scanner");
    printScanner(fileScanner);
    if (!fileScanner.hasNext(Regex.openCurly)) fail("While loop missing opening '{'");
    else fileScanner.next();

    //Parse all of the statements
    WhileLoop whileLoop = new WhileLoop();

    //Repeat until the next no more statements are found
    while (fileScanner.hasNext(Regex.statement)) {
      whileLoop.addStatement(parseStatement(fileScanner));
    }

    //Check for the closing }
    if (!fileScanner.hasNext(Regex.closeCurly)) fail("While loop missing closing '}'");
    else fileScanner.next();

    return null;
  }

  private Condition parseCondition(Scanner fileScanner) {
    System.out.println("Parsing condition");

    //Parse the method that will evaluate the condition
    if (fileScanner.hasNext(Regex.mazeCall)) return new ConditionNode(parseMazeCall(fileScanner));
    else {
      if (!fileScanner.hasNext(Regex.name)) fail("Unrecognised value in condition");
      else return new ConditionNode(parseVariableReference(fileScanner, fileScanner.next()));
    }

    fail("Invalid condition");
    return null;
  }

  /**
   * Parse a not condition.
   * @param fileScanner the file scanner
   * @return
   */
  private NotCondition parseNotCondition(Scanner fileScanner) {
    System.out.println("parsing not condition");

    fileScanner.next(); //Remove the !

    return new NotCondition(parseCondition(fileScanner));
  }

  /**
   * Parse any variable actions, such as update or reassign
   * @param fileScanner the file scanner.
   * @param varName the name of the variable
   * @return an update node
   */
  private Exec parseVariableReference(Scanner fileScanner, String varName) {
    System.out.println("Parsing variable reference");

    //If there is a .xyz return an action node
    if (fileScanner.hasNext(Regex.dot)) return parseVariableAction(fileScanner, varName);
    else if (fileScanner.hasNext(Regex.equals)) return parseVariableAssignment(fileScanner, varName);
    else fail("Invalid variable reference");

    return null;
  }

  private Exec parseVariableAssignment(Scanner fileScanner, String varName) {
    System.out.println("parsing variable assignment");
    return null;
  }

  /**
   * Parse an action on a variable that has already been created.
   * @param fileScanner the scanner.
   * @param varName the variable name.
   * @return a variable action object.
   */
  private Exec parseVariableAction(Scanner fileScanner, String varName) {
    System.out.println("parsing variable action");

    //Check if it is a call to a maze method
    if (fileScanner.hasNext(Regex.mazeCall)) return new VariableAction(varName, parseMazeCall(fileScanner));
    else return new VariableAction(varName, parseMethod(fileScanner));
  }

  /**
   * Parse variable declarations
   * @param fileScanner the file scanner.
   * @param declarationInfo the type and name of the variable.
   * @return the declaration node.
   */
  private VariableNode parseDeclaration(Scanner fileScanner, String declarationInfo) {
    System.out.println("parsing declaration");

    //Split the variable into type and name
    String[] varInfo = declarationInfo.split(" ");

    //Check if the variable is being assigned at the same time
    if (fileScanner.hasNext(Regex.semiColon)) return new VariableNode(varInfo[0], varInfo[1]);
    else if (fileScanner.hasNext(Regex.equals)) return new VariableNode(varInfo[0], varInfo[1], parseValue(fileScanner));

    fail("Incorrect declaration");
    return null;
  }

  /**
   * Parse a value assignment
   * @param fileScanner the file scanner.
   * @return a value node
   */
  private Exec parseValue(Scanner fileScanner) {
    System.out.println("parsing value");

    //Discard the =
    System.out.println(fileScanner.next());

    if (fileScanner.hasNext(Regex.mazeCall)) return parseMazeCall(fileScanner);

    return null;
  }

  /**
   * Parse a call to an action in the maze
   * @param fileScanner the file scanner.
   * @return a MazeAction node
   */
  private Exec parseMazeCall(Scanner fileScanner) {
    System.out.println("Parsing maze action");

    //Discard the "Maze"
    fileScanner.next();

    if (fileScanner.hasNext(Regex.dot)) {
      return new MazeAction(parseMethod(fileScanner));
    }

    return null;
  }

  /**
   * Parse a call to get something from the maze
   * @param fileScanner the file scanner.
   */
  private MethodNode parseMethod(Scanner fileScanner) {
    MethodNode toReturn = null;

    System.out.println("parsing method");

    //Check for the '.'
    if (!fileScanner.hasNext(Regex.dot)) fail("Method call missing '.'");
    else fileScanner.next();

    //Check for a valid method name
    String methodName = null;
    if (!fileScanner.hasNext(Regex.name)) fail("Invalid method name");
    else methodName = fileScanner.next();


    //Check for the opening parentheses
    if (!fileScanner.hasNext(Regex.openParen)) fail("Method missing opening '('");
    else fileScanner.next();

    //Check if there are any parameters
    if (fileScanner.hasNext(Regex.name)) toReturn = new MethodNode(methodName, parseParams(fileScanner));
    else if (fileScanner.hasNext(Regex.closeParen)) toReturn = new MethodNode(methodName);

    //Check for closing brace
    if (!fileScanner.hasNext(Regex.closeParen)) fail("Method missing closing ');");
    else fileScanner.next();

    return toReturn;
  }

  /**
   * Parse the parameters of a method
   * @return an arraylist of the parameters
   */
  private ArrayList<Object> parseParams(Scanner fileScanner) {
    ArrayList<Object> params = new ArrayList<>();

    System.out.println("parsing parameters");

    while (!fileScanner.hasNext(Regex.closeParen)) {
      String parameter = fileScanner.next();
      if (!parameter.contains(",")) params.add(parameter);
    }

    return params;
  }

  /**
   * Parse print statements
   * @param fileScanner the file scanner.
   * @return a new print node
   */
  private Exec parsePrint(Scanner fileScanner) {
    PrintNode toReturn = null;
    StringBuilder toPrint = new StringBuilder();


    //Check for opening (
    if (!fileScanner.hasNext(Regex.openParen)) fail("Print missing opening (");

    fileScanner.next();

    if (fileScanner.hasNext(Regex.doubleQuote)) {
      fileScanner.next();
      toPrint.append(fileScanner.next());

      if (!fileScanner.hasNext(Regex.doubleQuote)) fail("Quotation missing closing ");
      else fileScanner.next();
    }

    //Check for closing )
    if (!fileScanner.hasNext(Regex.closeParen)) fail("Print missing closing )");
    fileScanner.next();

    //Add to the print node
    toReturn = new PrintNode(toPrint.toString());

    return toReturn;
  }


  private void printScanner(Scanner s) {
    while (s.hasNext()) {
      System.out.println(s.next());
    }
  }

  private void fail(String message) {
    String msg = "Parse error: " + message + "\n   @ ...";
    for (int i = 0; i < 5 && fileScanner.hasNext(); i++) {
      msg += " " + fileScanner.next();
    }
    throw new ParserFailure(msg + "...");
  }
}
