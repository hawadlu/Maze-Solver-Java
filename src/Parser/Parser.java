package Parser;

import Application.Application;
import Game.Player;
import Parser.ProgramNodes.*;
import Parser.ProgramNodes.ConditionNodes.*;
import Parser.ProgramNodes.LoopNodes.ForNode;
import Parser.ProgramNodes.LoopNodes.WhileNode;
import Parser.ProgramNodes.MathNodes.Number;
import Parser.ProgramNodes.MathNodes.*;
import Parser.ProgramNodes.MethodNodes.MazeActionNode;
import Parser.ProgramNodes.MethodNodes.MethodNode;
import Parser.ProgramNodes.VariableNodes.GetVariableNode;
import Parser.ProgramNodes.VariableNodes.VariableActionNode;
import Parser.ProgramNodes.VariableNodes.VariableAssignmentNode;
import Parser.ProgramNodes.VariableNodes.VariableNode;
import Utility.Exceptions.ParserFailure;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * This class is responsible for parsing the files.
 */
public class Parser {
  boolean debug = false;
  Scanner fileScanner = null;
  Exec baseNode;
  private Handler handler;
  private static boolean enablePopup = true; //Set to false if debugging and the error popup becomes annoying

  public Parser(File toRead) {
    try {
      fileScanner = new Scanner(toRead);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }

  public Parser(File toRead, boolean enablePopup) {
    this.enablePopup = enablePopup;
    try {
      fileScanner = new Scanner(toRead);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }

  public void setMazeHandler(Handler handler) {
    this.handler = handler;
  }

  /**
   * Start the parser.
   */
  public void startParser() {
    System.out.println("Commenced parsing");

    fileScanner.useDelimiter(Regex.delimiter);

    if (debug) System.out.println("Delimiter: " + Regex.delimiter);
    if (debug) System.out.println("Statement: " + Regex.statement);
    if (debug) System.out.println("Method call: " + Regex.methodCall);
    if (debug) System.out.println("Maze: " + Regex.mazeCall);
    if (debug) System.out.println("Name: " + Regex.name);
    if (debug) System.out.println("Sentence: " + Regex.sentence);
    if (debug) System.out.println("Print: " + Regex.print);
    if (debug) System.out.println("Print concat: " + Regex.printConcatenate);
    if (debug) System.out.println("Declare var: " + Regex.declareVar);
    if (debug) System.out.println("Use var: " + Regex.useVar);
    if (debug) System.out.println("Reassign var: " + Regex.reassignVar);
    if (debug) System.out.println("While: " + Regex.whileLoop);
    if (debug) System.out.println("For: " + Regex.forLoop);
    if (debug) System.out.println("If: " + Regex.ifStmt);
    if (debug) System.out.println("Declaration: " + Regex.declaration);
    if (debug) System.out.println("Math: " + Regex.math);


    if (debug) System.out.println();

    //Start the scanner
    baseNode = new BaseNode(parseProgram(fileScanner));

    fileScanner.close();

    //Recursively search each node validate it.
    baseNode.validate();


    System.out.println("Parsing complete");
  }

  private ArrayList<Exec> parseProgram(Scanner fileScanner) {
    ArrayList<Exec> statements = new ArrayList<>();

    //Add statements to the list until the scanner is empty
//    printScanner(fileScanner);
    while (fileScanner.hasNext()) {
      if (fileScanner.hasNext(Regex.statement)) {
        statements.add(parseStatement(fileScanner));
      } else if (fileScanner.hasNext("\n")){
        fileScanner.next(); //Discard the new line
      } else {
        fail(fileScanner.next() + " is not a valid statement", fileScanner);
      }
    }

    return statements;
  }

  /**
   * Parse the program statements.
   * Will return null when parsing comments
   * @param fileScanner the file scanner.
   * @return the statement node
   */
  private Exec parseStatement(Scanner fileScanner) {
    Boolean semiRequired = true; //Indication of the necessity of a semi colon after this statement.
    Exec toReturn = null;
    if (debug) System.out.println("parsing statement");

    //Remove any newline characters
    String next = fileScanner.next().replaceAll("\\n", "");

    if (debug) System.out.println("Next: " + next);

    if (next.matches(Regex.comment.pattern())) {
      parseComment(fileScanner);
      semiRequired = false;
    } else if (next.matches(Regex.print.pattern())) {
      toReturn = parsePrint(fileScanner, true);
    } else if (next.matches(Regex.declaration.pattern())) {
      toReturn = parseDeclaration(fileScanner, next);
    } else if (next.matches(Regex.whileLoop.pattern())) {
      semiRequired = false;
      toReturn = parseWhile(fileScanner);
    } else if (next.matches(Regex.forLoop.pattern())) {
      semiRequired = false;
      toReturn = parseFor(fileScanner);
    } else if (next.matches(Regex.ifStmt.pattern())) {
      semiRequired = false;
      toReturn = parseIf(fileScanner);

      //Check for any else if statements
      if (fileScanner.hasNext(Regex.elseIf)) toReturn = parseElseIf(fileScanner, (IfNode) toReturn);

      //Check for any else statements
      if (fileScanner.hasNext(Regex.ifElse)) toReturn = parseElse(fileScanner, toReturn);

    } else if (next.matches(Regex.mazeCall.pattern())) {
      toReturn = parseMazeCall(fileScanner);

      //Last because my regex considers things such as 'while' as valid names
    } else if (next.matches(Regex.name.pattern())) {
      toReturn = parseVariableReference(fileScanner, next, false);
    }

    if (semiRequired && !fileScanner.hasNext(Regex.semiColon)) {
//      printScanner(fileScanner);
      fail("Statement missing ';'", fileScanner);
    }
    else if (semiRequired) fileScanner.next(); //Remove the semicolon

    //If the statement is empty return a new empty statement
    if (toReturn != null) return toReturn;
    else return new EmptyStatement();
  }

  /**
   * Parse an else statement.
   * @param fileScanner the file scanner.
   * @param toReturn the first block(s) of the if else statement.
   * @return an else node
   */
  private Exec parseElse(Scanner fileScanner, Exec toReturn) {
    if (debug) System.out.println("Parsing else");

    //Discard the 'else'
    fileScanner.next();

    scannerHasNext(fileScanner, Regex.openCurly, "Else missing opening '{'");

    ArrayList<Exec> statements = new ArrayList<>();

    //Parse all of the statements
    while (fileScanner.hasNext(Regex.statement)) {
      statements.add(parseStatement(fileScanner));
    }

    scannerHasNext(fileScanner, Regex.closeCurly, "Else missing closing '}'");

    ElseNode elseNode = new ElseNode(statements);

    //Add the else clause to the if statement
    if (toReturn instanceof IfNode) ((IfNode) toReturn).addElse(elseNode);
    else if (toReturn instanceof ElseIfNode) ((ElseIfNode) toReturn).addElse(elseNode);

    return toReturn;
  }

  /**
   * Parse an else if.
   * @param fileScanner the file scanner.
   * @return an elif object
   */
  private Exec parseElseIf(Scanner fileScanner, IfNode firstBlock) {
      //Parse all of the else if nodes
      ArrayList<IfNode> ifs = new ArrayList<>();
      ifs.add(firstBlock);

      while (fileScanner.hasNext(Regex.elseIf)) {
        ifs.add((IfNode) parseIf(fileScanner));
      }

      return new ElseIfNode(ifs);
  }

  /**
   * Parse a comparator object that will be used to to sort objects in a collection.
   * @param fileScanner the file scanner.
   * @return a comparator object.
   */
  private Exec parseComparator(Scanner fileScanner) {
    if (debug) System.out.println("Parsing comparator");

    //Discard the '->'
    fileScanner.next();

    Exec comparator = null;

    //Check for a method call
    if (!fileScanner.hasNext(Regex.mazeCall)) fail("Comparator is missing call to maze method.", fileScanner);
    else comparator = new ComparatorNode(parseMazeCall(fileScanner), handler);

    //Check to see that the next argument is the ';' because a comparator can only have one argument
    if (!fileScanner.hasNext(Regex.semiColon)) fail("Comparator can only have one argument", fileScanner);

    return comparator;
  }

  /**
   * Parse any comments in the file.
   * @param fileScanner the file scanner
   */
  private void parseComment(Scanner fileScanner) {
    if (debug) System.out.println("Parsing comment");
    while (!fileScanner.hasNext(Regex.comment)) {
      if (!fileScanner.hasNext()) fail("Comment missing closing '***'", fileScanner); //Reached the end of the program while parsing the comment
      fileScanner.next(); //Discard all tokens inside the comment
    }
    fileScanner.next(); //Discard the ***
  }

  /**
   * Parse for loops.
   * @param fileScanner the file scanner.
   * @return a for loop object
   */
  private Exec parseFor(Scanner fileScanner) {
    if (debug) System.out.println("Parsing for loop");
    Exec forLoop = null;

    //Check for opening '('
    scannerHasNext(fileScanner, Regex.openParen, "For loop missing opening '('");

    //Get the loop variable name
    String varName = null;
    if (!fileScanner.hasNext(Regex.name)) fail("Must declare variable name in for loop.", fileScanner);
    else varName = fileScanner.next().replaceAll("\\s", "");

    //Check for the ':'
    if (!fileScanner.hasNext(Regex.colon)) fail("For loop must contain ':' between variable and collection", fileScanner);
    else fileScanner.next(); //discard the colon

    //Get the name of the collection that the loop will use
    String collectionName = null;
    if (!fileScanner.hasNext(Regex.name)) fail("For loop must contain a collection variable", fileScanner);
    else collectionName = fileScanner.next().replaceAll("\\s", "");

    //Check for the closing ')'
    scannerHasNext(fileScanner, Regex.closeParen, "For loop missing closing ')'");

    //Check for open curly
    scannerHasNext(fileScanner, Regex.openCurly, "For loop missing opening '{'");

    //Parse all the statements
    ArrayList<Exec> statements = new ArrayList<>();
    while (fileScanner.hasNext(Regex.statement)) {
      //Only add the statement if it is not null.
      Exec statement = parseStatement(fileScanner);
      if (statement != null) statements.add(statement);
    }

    //Check for closing '}'
    scannerHasNext(fileScanner, Regex.closeCurly, "For loop missing closing '}'");

    forLoop = new ForNode(varName, collectionName, statements, handler);

    return forLoop;
  }

  /**
   * Check if the scanner has a particular regex next.
   * If fail, print a fail message, if success return the token
   * @param fileScanner the file scanner.
   * @param regex the regex to check.
   * @param failMessage message to print on failure
   */
  private void scannerHasNext(Scanner fileScanner, Pattern regex, String failMessage) {
    if (!fileScanner.hasNext(regex)) fail(failMessage, fileScanner);
    else fileScanner.next();
  }

  /**
   * Parse if statements
   * @param fileScanner the file scanner.
   * @return an if statement node
   */
  private Exec parseIf(Scanner fileScanner) {
    if (debug) System.out.println("Parsing if");

    //Discard the 'else if' if required
    if (fileScanner.hasNext(Regex.elseIf)) fileScanner.next();

    //Check for an opening brace
    scannerHasNext(fileScanner, Regex.openParen, "If statement missing opening '('");

    Condition condition;
    if (fileScanner.hasNext(Regex.not)) condition = parseNotCondition(fileScanner);
    else condition = parseCondition(fileScanner);

    scannerHasNext(fileScanner, Regex.closeParen, "If statement missing closing ')'");

    //Check for the opening {
    scannerHasNext(fileScanner, Regex.openCurly, "If statement missing opening '{'");

    //Parse all of the statements
    ArrayList<Exec> statements = new ArrayList<>();

    //Repeat until the next no more statements are found
    while (fileScanner.hasNext(Regex.statement)) {
      //Only add the statement if it is not null.
      Exec statement = parseStatement(fileScanner);
      if (statement != null) statements.add(statement);
    }

    //Check for the closing }
    scannerHasNext(fileScanner, Regex.closeCurly, "If statement missing closing '}'");

    IfNode ifNode = new IfNode(condition, statements);

    //If there is an else add it to the if node
    if (fileScanner.hasNext(Regex.ifElse)) parseElse(fileScanner, ifNode);

    return ifNode;
  }

  /**
   * Parse while loops.
   * @param fileScanner the file scanner.
   * @return a while loop object
   */
  private Exec parseWhile(Scanner fileScanner) {
    if (debug) System.out.println("Parsing while");

    //Check for an opening brace
    scannerHasNext(fileScanner, Regex.openParen, "While loop missing opening '('");

    Condition condition;
    if (fileScanner.hasNext(Regex.not)) condition = parseNotCondition(fileScanner);
    else condition = parseCondition(fileScanner);

    scannerHasNext(fileScanner, Regex.closeParen, "While loop missing closing ')'");

    //Check for the opening {
    scannerHasNext(fileScanner, Regex.openCurly, "While loop missing opening '{'");

    //Parse all of the statements
    ArrayList<Exec> statements = new ArrayList<>();

    //Repeat until the next no more statements are found
    while (fileScanner.hasNext(Regex.statement)) {
      //Only add the statement if it is not null.
      Exec statement = parseStatement(fileScanner);
      if (statement != null) statements.add(statement);
    }

    //Check for the closing }
    scannerHasNext(fileScanner, Regex.closeCurly, "While loop missing closing '}'");

    return new WhileNode(condition, statements);
  }

  private Condition parseCondition(Scanner fileScanner) {
    if (debug) System.out.println("Parsing condition");

    //Parse the method that will evaluate the condition
    if (fileScanner.hasNext(Regex.mazeCall)) return new ConditionNode(parseMazeCall(fileScanner));
    else if (fileScanner.hasNext(Regex.lessThan) || fileScanner.hasNext(Regex.greaterThan) || fileScanner.hasNext(Regex.equalTo)) return new ConditionNode(parseEqualityCheck(fileScanner));
    else {
      if (fileScanner.hasNext(Regex.name)) return new ConditionNode(parseVariableReference(fileScanner, fileScanner.next().replaceAll("\\s", ""), false));
      else fail("Unrecognised value in condition", fileScanner);
    }

    fail("Invalid condition", fileScanner);
    return null;
  }

  /**
   * Parse less than, greater than and equal to.
   * @param fileScanner the file scanner.
   * @return an equality node.
   */
  private Exec parseEqualityCheck(Scanner fileScanner) {
    if (debug) System.out.println("Parsing equality check");

    //check for less than
    if (fileScanner.hasNext(Regex.lessThan)) {
      return new LessThanNode(parseEqualityConditions());
    } else if (fileScanner.hasNext(Regex.equalTo)) {
      return new EqualToNode(parseEqualityConditions());
    } else if (fileScanner.hasNext(Regex.greaterThan)) {
      return new GreaterThanNode(parseEqualityConditions());
    }


    return null;
  }

  /**
   * Parse the conditions that will be used in checking equality
   * @return an array containing the two conditions to be checked.
   */
  private Number[] parseEqualityConditions() {
    if (debug) System.out.println("Parsing equality conditions");

    Number[] values = new Number[2];

    //Discard the 'lt'
    fileScanner.next();

    //Check for the opening '('
    scannerHasNext(fileScanner, Regex.openParen, "Equality condition missing opening '('");

    //Parse the first part of the condition
    if (fileScanner.hasNext(Regex.math)) values[0] = parseMath(fileScanner);
    else if (fileScanner.hasNext(Regex.mazeCall)) values[0] = parseEvaluateMazeCall(fileScanner);
    else if (fileScanner.hasNext(Regex.name)) values[0] = parseEvaluateNameCall(fileScanner);


    //Check for a comma
    scannerHasNext(fileScanner, Regex.comma, "Equality condition missing ',' ");

    //Parse the second part of the condition
    if (fileScanner.hasNext(Regex.math)) values[1] = parseMath(fileScanner);
    else if (fileScanner.hasNext(Regex.mazeCall)) values[1] = parseEvaluateMazeCall(fileScanner);
    else if (fileScanner.hasNext(Regex.name)) values[1] = parseEvaluateNameCall(fileScanner);


    //Check for the closing ')'
    scannerHasNext(fileScanner, Regex.closeParen, "Equality condition missing closing ')'");

    return values;
  }

  /**
   * Parse a not condition.
   * @param fileScanner the file scanner
   * @return
   */
  private NotConditionNode parseNotCondition(Scanner fileScanner) {
    if (debug) System.out.println("parsing not condition");

    fileScanner.next(); //Remove the !

    return new NotConditionNode(parseCondition(fileScanner));
  }

  /**
   * Parse any variable actions, such as update or reassign
   * @param fileScanner the file scanner.
   * @param varName the name of the variable
   * @param toPrint indicate if this variable is being accessed in a print statement
   * @return an update node
   */
  private Exec parseVariableReference(Scanner fileScanner, String varName, boolean toPrint) {
    if (debug) System.out.println("Parsing variable reference");

    Exec actionOrAssignment = null;

    //If there is a .xyz return an action node
    if (fileScanner.hasNext(Regex.dot)) actionOrAssignment = parseVariableAction(fileScanner, varName);
    else if (fileScanner.hasNext(Regex.equals)) actionOrAssignment = parseVariableAssignment(fileScanner, varName);
    else if (fileScanner.hasNext(Regex.plus) && !toPrint) actionOrAssignment = handler.getFromMap(varName);
    else if (toPrint) actionOrAssignment = parseGetVar(varName); //Return the variable object
    else fail("Invalid variable reference", fileScanner);

    return actionOrAssignment;
  }

  /**
   * Parse a reference to get a variable
   * @param varName the variable name
   * @return the get var object
   */
  private Exec parseGetVar(String varName) {
    return new GetVariableNode(varName, handler);
  }

  private Exec parseVariableAssignment(Scanner fileScanner, String varName) {
    if (debug) System.out.println("parsing variable assignment");

    //Discard the =
    fileScanner.next();

    if (fileScanner.hasNext(Regex.mazeCall)) return new VariableAssignmentNode(varName, parseMazeCall(fileScanner), handler);
    else if (fileScanner.hasNext(Regex.name)) return new VariableAssignmentNode(varName, parseVariableReference(fileScanner, fileScanner.next(), false), handler);
    else fail("Invalid variable assignment", fileScanner);

    return null;
  }

  /**
   * Parse an action on a variable that has already been created.
   * @param fileScanner the scanner.
   * @param varName the variable name.
   * @return a variable action object.
   */
  private Exec parseVariableAction(Scanner fileScanner, String varName) {
    if (debug) System.out.println("parsing variable action");

    //Check if it is a call to a maze method
    if (fileScanner.hasNext(Regex.mazeCall)) return new VariableActionNode(varName, parseMazeCall(fileScanner), handler);
    else return new VariableActionNode(varName, parseMethod(fileScanner), handler);
  }

  /**
   * Parse variable declarations
   * @param fileScanner the file scanner.
   * @param declarationInfo the type and name of the variable.
   * @return the declaration node.
   */
  private VariableNode parseDeclaration(Scanner fileScanner, String declarationInfo) {
    if (debug) System.out.println("parsing declaration");

    //Split the variable into type and name
    String[] varInfo = declarationInfo.split(" ");

    //Go through the array and remove any unwanted characters
    for (int i = 0; i < varInfo.length; i++) {
      if (varInfo[i].contains("\t")) varInfo[i] = varInfo[i].replaceAll("\t", "");
      if (varInfo[i].contains("\n")) varInfo[i] = varInfo[i].replaceAll("\n", "");
    }

    //Check if the variable is being assigned at the same time
    if (fileScanner.hasNext(Regex.semiColon)) {
      return new VariableNode(varInfo[0], varInfo[1], handler);
    } else if (fileScanner.hasNext(Regex.equals) || fileScanner.hasNext(Regex.comparatorAssignment)) {
      return new VariableNode(varInfo, parseValue(fileScanner), handler);
    }

    fail("Incorrect declaration", fileScanner);
    return null;
  }

  /**
   * Parse a value assignment
   * @param fileScanner the file scanner.
   * @return a value node
   */
  private Exec parseValue(Scanner fileScanner) {
    if (debug) System.out.println("parsing value");

    //Discard the =
    if (fileScanner.hasNext(Regex.equals)) {
      fileScanner.next();
      if (fileScanner.hasNext(Regex.mazeCall)) return parseMazeCall(fileScanner);
      else if (fileScanner.hasNext(Regex.math)) return parseEvaluateMathCall(fileScanner);
    } else if (fileScanner.hasNext(Regex.comparatorAssignment)) {
      return parseComparator(fileScanner);
    }

    return null;
  }

  /**
   * Parse a call to a math method that is executed at runtime.
   * @param fileScanner the file scanner.
   * @return an evaluate object.
   */
  private Exec parseEvaluateMathCall(Scanner fileScanner) {
    return new EvaluateNode(parseMath(fileScanner));
  }

  /**
   * Parse a call to an action in the maze
   * @param fileScanner the file scanner.
   * @return a MazeAction node
   */
  private Exec parseMazeCall(Scanner fileScanner) {
    if (debug) System.out.println("Parsing maze action");

    if (fileScanner.hasNext(Regex.mazeCall)) fileScanner.next();

    if (fileScanner.hasNext(Regex.dot)) {
      return new MazeActionNode(parseMethod(fileScanner), handler);
    }

    return null;
  }

  /**
   * Parse a call to get something from the maze
   * @param fileScanner the file scanner.
   */
  private MethodNode parseMethod(Scanner fileScanner) {
    MethodNode toReturn = null;

    if (debug) System.out.println("parsing method");

    //Check for the '.'
    scannerHasNext(fileScanner, Regex.dot, "Method call missing '.'");

    //Check for a valid method name
    String methodName = null;
    if (!fileScanner.hasNext(Regex.name)) fail("Invalid method name", fileScanner);
    else methodName = fileScanner.next();


    //Check for the opening parentheses
    scannerHasNext(fileScanner, Regex.openParen, "Method missing opening '('");

    //Check if there are any parameters
    if (fileScanner.hasNext(Regex.name)) toReturn = new MethodNode(methodName, parseParams(fileScanner), handler);
    else if (fileScanner.hasNext(Regex.closeParen)) toReturn = new MethodNode(methodName, handler);

    //Check for closing brace
    scannerHasNext(fileScanner, Regex.closeParen, "Method missing closing ');");

    return toReturn;
  }

  /**
   * Parse the parameters of a method
   * @return an arraylist of the parameters
   */
  private ArrayList<Object> parseParams(Scanner fileScanner) {
    ArrayList<Object> params = new ArrayList<>();

    if (debug) System.out.println("parsing parameters");

    while (!fileScanner.hasNext(Regex.closeParen)) {
      if (fileScanner.hasNext(Regex.comma)) fileScanner.next(); //Discard the comma
      if (fileScanner.hasNext(Regex.mazeCall)) params.add(parseMazeCall(fileScanner));
      else if (fileScanner.hasNext(Regex.math)) params.add(parseMath(fileScanner));
      else params.add(fileScanner.next().replaceAll(" ", "")); //Remove any spaces in the parameter
    }


    return params;
  }

  /**
   * Parse print statements
   * @param fileScanner the file scanner.
   * @param firstParen is this being called directly after the "print" has been encountered
   * @return a new print node
   */
  private Exec parsePrint(Scanner fileScanner, boolean firstParen) {
    PrintNode printer = new PrintNode(handler);
    StringBuilder toPrint = new StringBuilder();


    //Check for opening (
    if (firstParen) {
      scannerHasNext(fileScanner, Regex.openParen, "Print missing opening (");
    }

    //parse sections with a quote mark
    if (fileScanner.hasNext(Regex.doubleQuote)) {
      fileScanner.next();

      if (!fileScanner.hasNext(Regex.doubleQuote)) {
        while (!fileScanner.hasNext(Regex.doubleQuote)) {
          String next = fileScanner.next();
          toPrint.append(next);
        }
      } else {
        toPrint.append(" ");
      }

      scannerHasNext(fileScanner, Regex.doubleQuote, "Quotation missing closing \"");

      //Add the string to the print node
      printer.append(toPrint);
    } else {
      //parse other things to be printed such as math or other variables
      if (fileScanner.hasNext(Regex.math)) {
        printer.append(parseMath(fileScanner));
      }
      if (fileScanner.hasNext(Regex.name)) {
        String varName = fileScanner.next().replaceAll(" ", "");
        printer.append(parseVariableReference(fileScanner, varName, true));
      }
    }

    //Check if there is any concatenation going on
    if (fileScanner.hasNext(Regex.plus)) {
      fileScanner.next(); //Discard the '+'

      printer.append(parsePrint(fileScanner, false));
    }

    //Check for closing )
    if (firstParen) {
      scannerHasNext(fileScanner, Regex.closeParen, "Print missing closing )");
    }

    return printer;
  }

  /**
   * Parse any math
   * @param fileScanner the file scanner
   * @return a math node
   */
  private Number parseMath(Scanner fileScanner) {
    if (debug) System.out.println("Parsing math");

    Number mathNode = null;

    if (fileScanner.hasNext(Regex.number)) mathNode = parseNumber(fileScanner);
    else if (fileScanner.hasNext(Regex.mathPlus)) mathNode = parsePlus(fileScanner);
    else if (fileScanner.hasNext(Regex.mathMinus)) mathNode = parseMinus(fileScanner);
    else if (fileScanner.hasNext(Regex.mathDivide)) mathNode = parseDivide(fileScanner);
    else if (fileScanner.hasNext(Regex.mathMultiply)) mathNode = parseMultiply(fileScanner);
    else if (fileScanner.hasNext(Regex.mathPower)) mathNode = parsePower(fileScanner);
    else if (fileScanner.hasNext(Regex.mathRoot)) mathNode = parseRoot(fileScanner);

    return mathNode;
  }

  /**
   * Parse root operations
   * @param fileScanner the file scanner.
   * @return a multiply node
   */
  private Number parseRoot(Scanner fileScanner) {
    if (debug) System.out.println("Parsing root");

    RootNode root = new RootNode();

    fileScanner.next(); //Discard the root

    scannerHasNext(fileScanner, Regex.openParen, "Root missing opening '('");

    if (fileScanner.hasNext(Regex.closeParen)) fail("Power must contain one argument", fileScanner);

    //Repeat until the closing brace
    while (!fileScanner.hasNext(Regex.closeParen)) {
      if (fileScanner.hasNext(Regex.comma)) fileScanner.next(); //discard and continue
      else if (fileScanner.hasNext(Regex.math)) root.add(parseMath(fileScanner));
      else fail("Invalid math operation in root", fileScanner);
    }

    if (root.getArguments().size() > 1) fail("Root can only contain on argument", fileScanner);

    //Discard the closing )
    fileScanner.next();

    return root;
  }

  /**
   * Parse power operations
   * @param fileScanner the file scanner.
   * @return a multiply node
   */
  private Number parsePower(Scanner fileScanner) {
    if (debug) System.out.println("Parsing power");

    PowerNode power = new PowerNode();

    fileScanner.next(); //Discard the power

    scannerHasNext(fileScanner, Regex.openParen, "Power missing opening '('");

    if (fileScanner.hasNext(Regex.closeParen)) fail("Power must contain at least two arguments", fileScanner);

    //Repeat until the closing brace
    while (!fileScanner.hasNext(Regex.closeParen)) {
      if (fileScanner.hasNext(Regex.comma)) fileScanner.next(); //discard and continue
      else if (fileScanner.hasNext(Regex.math)) power.add(parseMath(fileScanner));
      else fail("Invalid math operation in power", fileScanner);
    }

    if (power.getArguments().size() < 2) fail("Power must contain at least two arguments", fileScanner);


    //Discard the closing )
    fileScanner.next();

    return power;
  }

  /**
   * Parse multiply operations
   * @param fileScanner the file scanner.
   * @return a multiply node
   */
  private Number parseMultiply(Scanner fileScanner) {
    if (debug) System.out.println("Parsing multiply");

    MultiplyNode multiply = new MultiplyNode();

    fileScanner.next(); //Discard the multiply

    scannerHasNext(fileScanner, Regex.openParen, "Multiply missing opening '('");

    if (fileScanner.hasNext(Regex.closeParen)) fail("Multiply must contain at least two arguments", fileScanner);

    //Repeat until the closing brace
    while (!fileScanner.hasNext(Regex.closeParen)) {
      if (fileScanner.hasNext(Regex.comma)) fileScanner.next(); //discard and continue
      else if (fileScanner.hasNext(Regex.math)) multiply.add(parseMath(fileScanner));
      else fail("Invalid math operation in multiply", fileScanner);
    }

    if (multiply.getArguments().size() < 2) fail("Multiply must contain at least two arguments", fileScanner);

    //Discard the closing )
    fileScanner.next();

    return multiply;
  }

  /**
   * Parse divide operations.
   * @param fileScanner the file scanner.
   * @return a divide node
   */
  private Number parseDivide(Scanner fileScanner) {
    if (debug) System.out.println("Parsing divide");

    DivideNode divide = new DivideNode();

    fileScanner.next(); //Discard the divide

    scannerHasNext(fileScanner, Regex.openParen, "Divide missing opening '('");

    if (fileScanner.hasNext(Regex.closeParen)) fail("Divide must contain at least two arguments", fileScanner);

    //Repeat until the closing brace
    while (!fileScanner.hasNext(Regex.closeParen)) {
      if (fileScanner.hasNext(Regex.comma)) fileScanner.next(); //discard and continue
      else if (fileScanner.hasNext(Regex.math)) divide.add(parseMath(fileScanner));
      else fail("Invalid math operation in divide", fileScanner);
    }

    if (divide.getArguments().size() < 2) fail("Divide must contain at least two arguments", fileScanner);

    //Discard the closing )
    fileScanner.next();

    return divide;
  }

  /**
   * Parse a minus operation
   * @param fileScanner the file scanner
   * @return a minus node
   */
  private Number parseMinus(Scanner fileScanner) {
    if (debug) System.out.println("Parsing minus");

    MinusNode minus = new MinusNode();

    fileScanner.next(); //Discard the minus

    scannerHasNext(fileScanner, Regex.openParen, "Minus missing opening '('");

    if (fileScanner.hasNext(Regex.closeParen)) fail("Minus must contain at least two arguments", fileScanner);

    //Repeat until the closing brace
    while (!fileScanner.hasNext(Regex.closeParen)) {
      if (fileScanner.hasNext(Regex.comma)) fileScanner.next(); //discard and continue
      else if (fileScanner.hasNext(Regex.math)) minus.add(parseMath(fileScanner));
      else fail("Invalid math operation in minus", fileScanner);
    }

    if (minus.getArguments().size() < 2) fail("Minus must contain at least two arguments", fileScanner);

    //Discard the closing )
    fileScanner.next();

    return minus;
  }

  private Number parsePlus(Scanner fileScanner) {
    if (debug) System.out.println("Parsing plus");

    PlusNode plus = new PlusNode();

    fileScanner.next(); //Discard the plus

    scannerHasNext(fileScanner, Regex.openParen, "Plus missing opening '('");

    if (fileScanner.hasNext(Regex.closeParen)) fail("Plus must contain at least two arguments", fileScanner);

    //Repeat until the closing brace
    while (!fileScanner.hasNext(Regex.closeParen)) {
      if (fileScanner.hasNext(Regex.comma)) fileScanner.next(); //discard and continue
      else if (fileScanner.hasNext(Regex.math)) plus.add(parseMath(fileScanner));
      else if (fileScanner.hasNext(Regex.mazeCall)) plus.add(parseEvaluateMazeCall(fileScanner));
      else if (fileScanner.hasNext(Regex.name)) plus.add(parseEvaluateNameCall(fileScanner));
      else fail("Invalid math operation in plus", fileScanner);
    }

    //Check that the number of arguments is valid
    if (plus.getArguments().size() < 2) fail("Plus must contain at least two arguments", fileScanner);

    //Discard the closing )
    fileScanner.next();

    return plus;
  }

  /**
   * Parse the runtime evaluation of a name.
   * @param fileScanner the file scanner.
   * @return an object that can be evaluated at runtime
   */
  private Number parseEvaluateNameCall(Scanner fileScanner) {
    if (debug) System.out.println("Parsing runtime variable evaluation");

    String name = fileScanner.next();
    GetVariableNode getVar = new GetVariableNode(name, handler);

    //Check if it is just a name with no attached method
    if (fileScanner.hasNext(Regex.comma) || fileScanner.hasNext(Regex.closeParen)) {
      return new EvaluateNode(getVar);
    }

    EvaluateNode eval = new EvaluateNode(getVar, parseVariableAction(fileScanner, name));

    return new EvaluateNode(getVar, eval);
  }

  /**
   * Parse the evaluation of a maze call so that it will return a number at runtime.
   * @param fileScanner the file scanner.
   * @return a node which will evaluate the contents of a variable at runtime
   */
  private Number parseEvaluateMazeCall(Scanner fileScanner) {
    if (debug) System.out.println("Parsing runtime variable evaluation");

    return new EvaluateNode(parseMazeCall(fileScanner));
  }

  private Number parseNumber(Scanner fileScanner) {
    if (debug) System.out.println("Parsing number");
    String number = fileScanner.next();
    if (debug) System.out.println(number);
    return new NumberNode(Double.parseDouble(number));
  }


  private void printScanner(Scanner s) {
    while (s.hasNext()) {
      System.out.println(s.next());
    }
  }

  public static void fail(String message, Scanner fileScanner) {
    String msg;
    if (fileScanner != null) {
      msg = "FAIL: Parse error: " + message + "\n   @ ...";
      for (int i = 0; i < 5 && fileScanner.hasNext(); i++) {
        msg += " " + fileScanner.next();
      }
    } else {
      msg = "FAIL: Parse error: " + message;
    }

    throw new ParserFailure(new JFrame(), msg + "...", enablePopup);
  }

  /**
   * execute the compile code.
   */
  public Object execute(Application application) {
    //Create a new handler object if required
    if (this.handler == null) this.handler = new Handler(application);

    baseNode.execute();
    return null;
  }

  /**
   * Print the compiled version of the program
   */
  public void print() {
    System.out.println(baseNode.toString());
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Parser parser = (Parser) o;
    return debug == parser.debug && Objects.equals(fileScanner, parser.fileScanner) && Objects.equals(baseNode, parser.baseNode) && Objects.equals(handler, parser.handler);
  }

  @Override
  public int hashCode() {
    return Objects.hash(debug, fileScanner, baseNode, handler);
  }

  /**
   * Set the player if required.
   * @param player the player object.
   */
  public void setPlayer(Player player) {
    handler.setPlayer(player);
  }
}
