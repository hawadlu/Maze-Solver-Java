package parser;

import Application.Application;
import Game.Player;
import Utility.Exceptions.ParserFailure;
import parser.interfaces.Condition;
import parser.interfaces.Exec;
import parser.nodes.*;
import parser.nodes.loops.ForNode;
import parser.nodes.loops.WhileNode;
import parser.interfaces.Number;
import parser.nodes.math.*;
import parser.nodes.methods.MazeActionNode;
import parser.nodes.methods.MethodNode;
import parser.nodes.variables.GetVariableNode;
import parser.nodes.variables.VariableActionNode;
import parser.nodes.variables.VariableAssignmentNode;
import parser.nodes.variables.VariableNode;
import parser.nodes.conditions.*;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * This class is responsible for parsing the files.
 */
public class Parser {
  private final boolean DEBUG_ON = false;
  private Scanner fileScanner = null;
  private Exec baseNode;
  private Handler handler;
  private boolean enablePopup = true; //Set to false if debugging and the error popup becomes annoying

  /**
   * Create the scanner that will be used to parse to file.
   * @param toRead the file to use.
   */
  public Parser(File toRead) {
    try {
      //Scan the incoming file to make sure that all parenthesis are balanced.
      scanFile(toRead);

      fileScanner = new Scanner(toRead, StandardCharsets.UTF_8);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Create the parser object.
   * @param toRead the file to use.
   * @param enablePopup a boolean indicating whether or not popups should be enabled.
   */
  public Parser(File toRead, boolean enablePopup) {
    this.enablePopup = enablePopup;
    try {
      //Scan the incoming file to make sure that all parenthesis are balanced.
      scanFile(toRead);

      fileScanner = new Scanner(toRead, StandardCharsets.UTF_8);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * @return the current status of whether or not to display the popup.
   */
  public boolean getPopup() {
    return enablePopup;
  }

  /**
   * Scan the incoming file to make sure that all the parenthesis are balanced.
   * @param toRead the file containing the program.
   */
  private void scanFile(File toRead) {
    int openCurly = 0, closeCurly = 0, openParen = 0, closeParen = 0;

    try {
      Scanner scanner = new Scanner(toRead, StandardCharsets.UTF_8);

      while (scanner.hasNextLine()) {
        String line = scanner.nextLine();

        //Don't count anything if it is a comment
        if (!line.contains("***")) {
          String[] chars = line.split("");

          for (String character: chars) {
            switch (character) {
              case "(" -> openParen++;
              case ")" -> closeParen++;
              case "{" -> openCurly++;
              case "}" -> closeCurly++;
            }
          }
        }
      }

      //Check to see if the count match
      if (openParen != closeParen) Parser.fail("Detected unbalanced '('", "parser", null, getPopup());
      else if (openCurly != closeCurly) Parser.fail("Detected unbalanced '{'", "parser",  null, getPopup());

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Set the maze handler and whether or not popups should be displayed.
   * @param handler the maze handler.
   */
  public void setMazeHandler(Handler handler) {
    this.handler = handler;
    this.handler.setPopup(enablePopup);
  }

  /**
   * Start the parser.
   */
  public void compile() {
    System.out.println("Commenced parsing");

    fileScanner.useDelimiter(Regex.delimiter);

    if (DEBUG_ON) System.out.println("Delimiter: " + Regex.delimiter);
    if (DEBUG_ON) System.out.println("Statement: " + Regex.statement);
    if (DEBUG_ON) System.out.println("Method call: " + Regex.methodCall);
    if (DEBUG_ON) System.out.println("Maze: " + Regex.mazeCall);
    if (DEBUG_ON) System.out.println("Name: " + Regex.name);
    if (DEBUG_ON) System.out.println("Sentence: " + Regex.sentence);
    if (DEBUG_ON) System.out.println("Print: " + Regex.print);
    if (DEBUG_ON) System.out.println("Print concat: " + Regex.printConcatenate);
    if (DEBUG_ON) System.out.println("Declare var: " + Regex.declareVar);
    if (DEBUG_ON) System.out.println("Use var: " + Regex.useVar);
    if (DEBUG_ON) System.out.println("Reassign var: " + Regex.reassignVar);
    if (DEBUG_ON) System.out.println("While: " + Regex.whileLoop);
    if (DEBUG_ON) System.out.println("For: " + Regex.forLoop);
    if (DEBUG_ON) System.out.println("If: " + Regex.ifStmt);
    if (DEBUG_ON) System.out.println("Declaration: " + Regex.declaration);
    if (DEBUG_ON) System.out.println("Math: " + Regex.math);


    if (DEBUG_ON) System.out.println();

    //Start the scanner
    baseNode = new BaseNode(parseProgram());

    fileScanner.close();

    //Recursively search each node validate it.
    baseNode.validate();


    System.out.println("Parsing complete");
  }

  /**
   * Go through the program and parse each of the statements.
   * @return an arraylist of statements which will be executed later.
   */
  private ArrayList<Exec> parseProgram() {
    ArrayList<Exec> statements = new ArrayList<>();

    //Add statements to the list until the scanner is empty
    while (fileScanner.hasNext()) {
      if (fileScanner.hasNext(Regex.statement)) {
        statements.add(parseStatement());
      } else if (fileScanner.hasNext("\n")){
        fileScanner.next(); //Discard the new line
      } else {
        fail(fileScanner.next() + " is not a valid statement", "parser", fileScanner, getPopup());
      }
    }

    return statements;
  }

  /**
   * Parse the program statements.
   * Will return null when parsing comments
   * @return the statement node
   */
  private Exec parseStatement() {
    boolean semiRequired = true; //Indication of the necessity of a semi colon after this statement.
    Exec toReturn = null;
    if (DEBUG_ON) System.out.println("parsing statement");

    //Remove any newline characters
    String next = fileScanner.next().replaceAll("\\n", "");

    if (DEBUG_ON) System.out.println("Next: " + next);

    if (next.matches(Regex.comment.pattern())) {
      parseComment();
      semiRequired = false;
    } else if (next.matches(Regex.print.pattern())) {
      toReturn = parsePrint(true);
    } else if (next.matches(Regex.declaration.pattern())) {
      toReturn = parseDeclaration(next);
    } else if (next.matches(Regex.whileLoop.pattern())) {
      semiRequired = false;
      toReturn = parseWhile();
    } else if (next.matches(Regex.forLoop.pattern())) {
      semiRequired = false;
      toReturn = parseFor();
    } else if (next.matches(Regex.ifStmt.pattern())) {
      semiRequired = false;
      toReturn = parseIf();

      //Check for any else if statements
      if (fileScanner.hasNext(Regex.elseIf)) toReturn = parseElseIf((IfNode) toReturn);

      //Check for any else statements
      if (fileScanner.hasNext(Regex.ifElse)) toReturn = parseElse(toReturn);

    } else if (next.matches(Regex.mazeCall.pattern())) {
      toReturn = parseMazeCall();

      //Last because my regex considers things such as 'while' as valid names
    } else if (next.matches(Regex.name.pattern())) {
      toReturn = parseVariableReference(next, false);
    }

    if (semiRequired && !fileScanner.hasNext(Regex.semiColon)) {
      fail("Statement missing ';'", "parser", fileScanner, getPopup());
    }
    else if (semiRequired) fileScanner.next(); //Remove the semicolon

    //If the statement is empty return a new empty statement
    if (toReturn != null) return toReturn;
    else return new EmptyNode();
  }

  /**
   * Parse an else statement.
   * @param toReturn the first block(s) of the if else statement.
   * @return an else node
   */
  private Exec parseElse(Exec toReturn) {
    if (DEBUG_ON) System.out.println("Parsing else");

    //Discard the 'else'
    fileScanner.next();

    scannerHasNext(Regex.openCurly, "Else missing opening '{'");

    ArrayList<Exec> statements = new ArrayList<>();

    //Parse all of the statements
    while (fileScanner.hasNext(Regex.statement)) {
      statements.add(parseStatement());
    }

    scannerHasNext(Regex.closeCurly, "Else missing closing '}'");

    ElseNode elseNode = new ElseNode(statements);

    //Add the else clause to the if statement
    if (toReturn instanceof IfNode) ((IfNode) toReturn).addElse(elseNode);
    else if (toReturn instanceof ElseIfNode) ((ElseIfNode) toReturn).addElse(elseNode);

    return toReturn;
  }

  /**
   * Parse an else if.
   * @return an elif object
   */
  private Exec parseElseIf(IfNode firstBlock) {
      //Parse all of the else if nodes
      ArrayList<IfNode> ifs = new ArrayList<>();
      ifs.add(firstBlock);

      while (fileScanner.hasNext(Regex.elseIf)) {
        ifs.add((IfNode) parseIf());
      }

      return new ElseIfNode(ifs);
  }

  /**
   * Parse a comparator object that will be used to to sort objects in a collection.
   * This method can take a the method name as a separate parameter or get it itself.
   * @return a comparator object.
   */
  private Exec parseComparator(String methodName) {
    if (DEBUG_ON) System.out.println("Parsing comparator");

    Exec comparator = null;

    //The method name has not been supplied
    if (methodName == null) {
      //Discard the '->'
      fileScanner.next();

      //Check for a method call
      if (!fileScanner.hasNext(Regex.name)) Parser.fail("Comparator missing comparator method", "parser", null, getPopup());
      else comparator = new ComparatorNode(fileScanner.next().replaceAll(" ", ""), handler);
    } else {
      comparator = new ComparatorNode(methodName, handler);
    }

    //Check to see that the next argument is the ';' because a comparator can only have one argument
    if (!fileScanner.hasNext(Regex.semiColon)) fail("Comparator can only have one argument", "parser", fileScanner, getPopup());

    return comparator;
  }

  /**
   * Parse any comments in the file.
   */
  private void parseComment() {
    if (DEBUG_ON) System.out.println("Parsing comment");
    while (!fileScanner.hasNext(Regex.comment)) {
      if (!fileScanner.hasNext()) fail("Comment missing closing '***'", "parser", fileScanner, getPopup()); //Reached the end of the program while parsing the comment
      fileScanner.next(); //Discard all tokens inside the comment
    }
    fileScanner.next(); //Discard the ***
  }

  /**
   * Parse for loops.
   * @return a for loop object
   */
  private Exec parseFor() {
    if (DEBUG_ON) System.out.println("Parsing for loop");
    Exec forLoop;

    //Check for opening '('
    scannerHasNext(Regex.openParen, "For loop missing opening '('");

    //Get the loop variable name
    String varName = null;
    if (!fileScanner.hasNext(Regex.name)) fail("Must declare variable name in for loop.", "parser", fileScanner, getPopup());
    else varName = fileScanner.next().replaceAll("\\s", "");

    //Check for the ':'
    if (!fileScanner.hasNext(Regex.colon)) fail("For loop must contain ':' between variable and collection", "parser", fileScanner, getPopup());
    else fileScanner.next(); //discard the colon

    //Get the name of the collection that the loop will use
    String collectionName = null;
    if (!fileScanner.hasNext(Regex.name)) fail("For loop must contain a collection variable", "parser", fileScanner, getPopup());
    else collectionName = fileScanner.next().replaceAll("\\s", "");

    //Check for the closing ')'
    scannerHasNext(Regex.closeParen, "For loop missing closing ')'");

    //Check for open curly
    scannerHasNext(Regex.openCurly, "For loop missing opening '{'");

    //Parse all the statements
    ArrayList<Exec> statements = new ArrayList<>();
    while (fileScanner.hasNext(Regex.statement)) {
      //Only add the statement if it is not null.
      Exec statement = parseStatement();
      statements.add(statement);
    }

    //Check for closing '}'
    scannerHasNext(Regex.closeCurly, "For loop missing closing '}'");

    forLoop = new ForNode(varName, collectionName, statements, handler);

    return forLoop;
  }

  /**
   * Check if the scanner has a particular regex next.
   * If fail, print a fail message, if success return the token
   * @param regex the regex to check.
   * @param failMessage message to print on failure
   */
  private void scannerHasNext(Pattern regex, String failMessage) {
    if (!fileScanner.hasNext(regex)) fail(failMessage, "parser", fileScanner, getPopup());
    else fileScanner.next();
  }

  /**
   * Parse if statements
   * @return an if statement node
   */
  private Exec parseIf() {
    if (DEBUG_ON) System.out.println("Parsing if");

    //Discard the 'else if' if required
    if (fileScanner.hasNext(Regex.elseIf)) fileScanner.next();

    //Check for an opening brace
    scannerHasNext(Regex.openParen, "If statement missing opening '('");

    Condition condition;
    if (fileScanner.hasNext(Regex.not)) condition = parseNotCondition();
    else condition = parseCondition();

    scannerHasNext(Regex.closeParen, "If statement missing closing ')'");

    //Check for the opening {
    scannerHasNext(Regex.openCurly, "If statement missing opening '{'");

    //Parse all of the statements
    ArrayList<Exec> statements = new ArrayList<>();

    //Repeat until the next no more statements are found
    while (fileScanner.hasNext(Regex.statement)) {
      //Only add the statement if it is not null.
      Exec statement = parseStatement();
      statements.add(statement);
    }

    //Check for the closing }
    scannerHasNext(Regex.closeCurly, "If statement missing closing '}'");

    IfNode ifNode = new IfNode(condition, statements);

    //If there is an else add it to the if node
    if (fileScanner.hasNext(Regex.ifElse)) parseElse(ifNode);

    return ifNode;
  }

  /**
   * Parse while loops.
   * @return a while loop object
   */
  private Exec parseWhile() {
    if (DEBUG_ON) System.out.println("Parsing while");

    //Check for an opening brace
    scannerHasNext(Regex.openParen, "While loop missing opening '('");

    Condition condition;
    if (fileScanner.hasNext(Regex.not)) condition = parseNotCondition();
    else condition = parseCondition();

    scannerHasNext(Regex.closeParen, "While loop missing closing ')'");

    //Check for the opening {
    scannerHasNext(Regex.openCurly, "While loop missing opening '{'");

    //Parse all of the statements
    ArrayList<Exec> statements = new ArrayList<>();

    //Repeat until the next no more statements are found
    while (fileScanner.hasNext(Regex.statement)) {
      //Only add the statement if it is not null.
      Exec statement = parseStatement();
      statements.add(statement);
    }

    //Check for the closing }
    scannerHasNext(Regex.closeCurly, "While loop missing closing '}'");

    return new WhileNode(condition, statements);
  }

  private Condition parseCondition() {
    if (DEBUG_ON) System.out.println("Parsing condition");

    //Parse the method that will evaluate the condition
    if (fileScanner.hasNext(Regex.mazeCall)) return new ConditionNode(parseMazeCall());
    else if (fileScanner.hasNext(Regex.lessThan) || fileScanner.hasNext(Regex.greaterThan) || fileScanner.hasNext(Regex.equalTo)) return new ConditionNode(parseEqualityCheck());
    else {
      if (fileScanner.hasNext(Regex.name)) return new ConditionNode(parseVariableReference(fileScanner.next().replaceAll("\\s", ""), false));
      else fail("Unrecognised value in condition", "parser", fileScanner, getPopup());
    }

    fail("Invalid condition", "parser", fileScanner, getPopup());
    return null;
  }

  /**
   * Parse less than, greater than and equal to.
   * @return an equality node.
   */
  private Exec parseEqualityCheck() {
    if (DEBUG_ON) System.out.println("Parsing equality check");

    //check for less than
    if (fileScanner.hasNext(Regex.lessThan)) {
      return new LessThanNode(parseEqualityConditions(), handler);
    } else if (fileScanner.hasNext(Regex.equalTo)) {
      return new EqualToNode(parseEqualityConditions(), handler);
    } else if (fileScanner.hasNext(Regex.greaterThan)) {
      return new GreaterThanNode(parseEqualityConditions(), handler);
    }


    return null;
  }

  /**
   * Parse the conditions that will be used in checking equality
   * @return an array containing the two conditions to be checked.
   */
  private Number[] parseEqualityConditions() {
    if (DEBUG_ON) System.out.println("Parsing equality conditions");

    Number[] values = new Number[2];

    //Discard the 'lt'
    fileScanner.next();

    //Check for the opening '('
    scannerHasNext(Regex.openParen, "Equality condition missing opening '('");

    //Parse the first part of the condition
    if (fileScanner.hasNext(Regex.math)) values[0] = parseMath();
    else if (fileScanner.hasNext(Regex.mazeCall)) values[0] = parseEvaluateMazeCall();
    else if (fileScanner.hasNext(Regex.name)) values[0] = parseEvaluateNameCall();


    //Check for a comma
    scannerHasNext(Regex.comma, "Equality condition missing ',' ");

    //Parse the second part of the condition
    if (fileScanner.hasNext(Regex.math)) values[1] = parseMath();
    else if (fileScanner.hasNext(Regex.mazeCall)) values[1] = parseEvaluateMazeCall();
    else if (fileScanner.hasNext(Regex.name)) values[1] = parseEvaluateNameCall();


    //Check for the closing ')'
    scannerHasNext(Regex.closeParen, "Equality condition missing closing ')'");

    return values;
  }

  /**
   * Parse a not condition.
   * @return a new not node.
   */
  private NotNode parseNotCondition() {
    if (DEBUG_ON) System.out.println("parsing not condition");

    fileScanner.next(); //Remove the !

    return new NotNode(parseCondition());
  }

  /**
   * Parse any variable actions, such as update or reassign
   * @param varName the name of the variable
   * @param toPrint indicate if this variable is being accessed in a print statement
   * @return an update node
   */
  private Exec parseVariableReference(String varName, boolean toPrint) {
    if (DEBUG_ON) System.out.println("Parsing variable reference");

    //Remove any spaces
    varName = varName.replaceAll(" ", "");

    Exec actionOrAssignment = null;

    //If there is a .xyz return an action node
    if (varName.matches(Regex.math.pattern())) actionOrAssignment = new VariableActionNode(varName, parseMath(), handler);
    else if (fileScanner.hasNext(Regex.dot)) actionOrAssignment = parseVariableAction(varName);
    else if (fileScanner.hasNext(Regex.equals) || fileScanner.hasNext(Regex.comparatorAssignment)) actionOrAssignment = parseVariableAssignment(varName);
    else if (fileScanner.hasNext(Regex.plus) && !toPrint) actionOrAssignment = handler.getFromMap(varName);
    else if (varName.matches(Regex.comparatorMethod.pattern())) actionOrAssignment = parseComparator(varName);
    else if (toPrint || varName.matches(Regex.name.pattern())) actionOrAssignment = parseGetVar(varName); //Return the variable object
    else fail("Invalid variable reference", "parser", fileScanner, getPopup());

    return actionOrAssignment;
  }

  /**
   * Parse a reference to get a variable.
   * @param varName the variable name
   * @return the get var object
   */
  private Exec parseGetVar(String varName) {
    return new GetVariableNode(varName, handler);
  }

  /**
   * Parse a variable assignment
   * @param varName The name of the variable.
   * @return A variable assignment node.
   */
  private Exec parseVariableAssignment(String varName) {
    if (DEBUG_ON) System.out.println("parsing variable assignment");

    //Discard the = or ->
    fileScanner.next();

    if (fileScanner.hasNext(Regex.mazeCall)) return new VariableAssignmentNode(varName, parseMazeCall(), handler);
    else if (fileScanner.hasNext(Regex.math)) return new VariableAssignmentNode(varName, parseMath(), handler);
    else if (fileScanner.hasNext(Regex.comparatorMethod)) return new VariableAssignmentNode(varName, parseComparator(null), handler);
    else if (fileScanner.hasNext(Regex.name)) return new VariableAssignmentNode(varName, parseVariableReference(fileScanner.next(), false), handler);
    else fail("Invalid variable assignment", "parser", fileScanner, getPopup());

    return null;
  }

  /**
   * Parse an action on a variable that has already been created.
   * @param varName the variable name.
   * @return a variable action object.
   */
  private Exec parseVariableAction(String varName) {
    if (DEBUG_ON) System.out.println("parsing variable action");

    //Check if it is a call to a maze method
    if (fileScanner.hasNext(Regex.mazeCall)) return new VariableActionNode(varName, parseMazeCall(), handler);
    else return new VariableActionNode(varName, parseMethod(), handler);
  }

  /**
   * Parse variable declarations
   * @param declarationInfo the type and name of the variable.
   * @return the declaration node.
   */
  private VariableNode parseDeclaration( String declarationInfo) {
    if (DEBUG_ON) System.out.println("parsing declaration");

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
      return new VariableNode(varInfo, parseValue(), handler);
    }

    fail("Incorrect declaration", "parser", fileScanner, getPopup());
    return null;
  }

  /**
   * Parse a value assignment
   * @return a value node
   */
  private Exec parseValue() {
    if (DEBUG_ON) System.out.println("parsing value");

    //Discard the =
    if (fileScanner.hasNext(Regex.equals)) {
      fileScanner.next();
      if (fileScanner.hasNext(Regex.mazeCall)) {
        return parseMazeCall();
      } else if (fileScanner.hasNext(Regex.math)) {
        return parseEvaluateMathCall();
      } else if (fileScanner.hasNext(Regex.lessThan) || fileScanner.hasNext(Regex.greaterThan) || fileScanner.hasNext(Regex.equalTo)) {
        return new EvaluateNode(new ConditionNode(parseEqualityCheck()));
      } else if (fileScanner.hasNext(Regex.name)) {
        String name = fileScanner.next().replaceAll(" ", "");

        //Check if this is just a name, or a method
        if (fileScanner.hasNext(Regex.dot)) return parseEvaluateMethodCall(name);
        else {
          return new GetVariableNode(name, handler);
        }
      }
    } else if (fileScanner.hasNext(Regex.comparatorAssignment)) {
      return parseComparator(null);
    }

    return null;
  }

  /**
   * Parse a call to a math method that is executed at runtime.
   * @return an evaluate object.
   */
  private Exec parseEvaluateMathCall() {
    return new EvaluateNode(parseMath());
  }

  /**
   * Parse a call to an action in the maze
   * @return a MazeAction node
   */
  private Exec parseMazeCall() {
    if (DEBUG_ON) System.out.println("Parsing maze action");

    if (fileScanner.hasNext(Regex.mazeCall)) fileScanner.next();

    if (fileScanner.hasNext(Regex.dot)) {
      return new MazeActionNode(parseMethod(), handler);
    }

    return null;
  }

  /**
   * Parse a call to get something from the maze
   */
  private MethodNode parseMethod() {
    MethodNode toReturn = null;

    if (DEBUG_ON) System.out.println("parsing method");

    //Check for the '.'
    scannerHasNext(Regex.dot, "Method call missing '.'");

    //Check for a valid method name
    String methodName = null;
    if (!fileScanner.hasNext(Regex.name)) fail("Invalid method name", "parser", fileScanner, getPopup());
    else methodName = fileScanner.next();


    //Check for the opening parentheses
    scannerHasNext(Regex.openParen, "Method missing opening '('");

    //Check if there are any parameters
    if (fileScanner.hasNext(Regex.name)) {
      assert methodName != null;
      toReturn = new MethodNode(methodName, parseParams(), handler);
    }
    else if (fileScanner.hasNext(Regex.closeParen)) toReturn = new MethodNode(methodName, handler);
    else if (fileScanner.hasNext(Regex.doubleQuote)) {
      ArrayList<Object> params = new ArrayList<>();
      params.add(parsePrint(false));
      assert methodName != null;
      toReturn = new MethodNode(methodName, params, handler);
    }

    //Check for closing brace
    scannerHasNext(Regex.closeParen, "Method missing closing ');");

    return toReturn;
  }

  /**
   * Parse the parameters of a method
   * @return an arraylist of the parameters
   */
  private ArrayList<Object> parseParams() {
    ArrayList<Object> params = new ArrayList<>();

    if (DEBUG_ON) System.out.println("parsing parameters");

    while (!fileScanner.hasNext(Regex.closeParen)) {
      if (fileScanner.hasNext(Regex.comma)) fileScanner.next(); //Discard the comma
      if (fileScanner.hasNext(Regex.mazeCall)) params.add(parseMazeCall());
      else if (fileScanner.hasNext(Regex.math)) params.add(parseMath());
      else if (fileScanner.hasNext(Regex.name)) {
        String name = fileScanner.next().replaceAll(" ", "");
        if (fileScanner.hasNext(Regex.dot)) params.add(parseEvaluateMethodCall(name));
        else params.add(name);
      }
    }

    if (DEBUG_ON) System.out.println("Parameters: " + params);

    return params;
  }

  /**
   * Parse print statements
   * @param firstParen is this being called directly after the "print" has been encountered
   * @return a new print node
   */
  private Exec parsePrint(boolean firstParen) {
    PrintNode printer = new PrintNode();
    StringBuilder toPrint = new StringBuilder();


    //Check for opening (
    if (firstParen) {
      scannerHasNext(Regex.openParen, "Print missing opening (");
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

      scannerHasNext(Regex.doubleQuote, "Quotation missing closing \"");

      //Add the string to the print node
      printer.append(toPrint);
    } else {
      //parse other things to be printed such as math or other variables
      if (fileScanner.hasNext(Regex.math)) {
        printer.append(parseMath());
      }
      if (fileScanner.hasNext(Regex.name)) {
        String varName = fileScanner.next().replaceAll(" ", "");
        printer.append(parseVariableReference(varName, true));
      }
    }

    //Check if there is any concatenation going on
    if (fileScanner.hasNext(Regex.plus)) {
      fileScanner.next(); //Discard the '+'

      printer.append(parsePrint(false));
    }

    //Check for closing )
    if (firstParen) {
      scannerHasNext(Regex.closeParen, "Print missing closing )");
    }

    return printer;
  }

  /**
   * Parse any math
   * @return a math node
   */
  private Number parseMath() {
    if (DEBUG_ON) System.out.println("Parsing math");

    Number mathNode = null;

    if (fileScanner.hasNext(Regex.number)) mathNode = parseNumber();
    else if (fileScanner.hasNext(Regex.mathPlus)) mathNode = parsePlus();
    else if (fileScanner.hasNext(Regex.mathMinus)) mathNode = parseMinus();
    else if (fileScanner.hasNext(Regex.mathDivide)) mathNode = parseDivide();
    else if (fileScanner.hasNext(Regex.mathMultiply)) mathNode = parseMultiply();
    else if (fileScanner.hasNext(Regex.mathPower)) mathNode = parsePower();
    else if (fileScanner.hasNext(Regex.mathRoot)) mathNode = parseRoot();

    return mathNode;
  }

  /**
   * Parse root operations
   * @return a multiply node
   */
  private Number parseRoot() {
    if (DEBUG_ON) System.out.println("Parsing root");

    RootNode root = new RootNode();

    fileScanner.next(); //Discard the root

    scannerHasNext(Regex.openParen, "Root missing opening '('");

    if (fileScanner.hasNext(Regex.closeParen)) fail("Power must contain one argument", "parser ", fileScanner, getPopup());

    //Repeat until the closing brace
    while (!fileScanner.hasNext(Regex.closeParen)) {
      if (fileScanner.hasNext(Regex.comma)) fileScanner.next(); //discard and continue
      else if (fileScanner.hasNext(Regex.math)) root.add(parseMath());
      else fail("Invalid math operation in root", "parser", fileScanner, getPopup());
    }

    if (root.getArguments().size() > 1) fail("Root can only contain on argument", "parser", fileScanner, getPopup());

    //Discard the closing )
    fileScanner.next();

    return root;
  }

  /**
   * Parse power operations
   * @return a multiply node
   */
  private Number parsePower() {
    if (DEBUG_ON) System.out.println("Parsing power");

    PowerNode power = new PowerNode();

    fileScanner.next(); //Discard the power

    scannerHasNext(Regex.openParen, "Power missing opening '('");

    if (fileScanner.hasNext(Regex.closeParen)) fail("Power must contain at least two arguments", "parser", fileScanner, getPopup());

    //Repeat until the closing brace
    while (!fileScanner.hasNext(Regex.closeParen)) {
      if (fileScanner.hasNext(Regex.comma)) fileScanner.next(); //discard and continue
      else if (fileScanner.hasNext(Regex.math)) power.add(parseMath());
      else fail("Invalid math operation in power", "parser", fileScanner, getPopup());
    }

    if (power.getArguments().size() < 2) fail("Power must contain at least two arguments", "parser", fileScanner, getPopup());


    //Discard the closing )
    fileScanner.next();

    return power;
  }

  /**
   * Parse multiply operations
   * @return a multiply node
   */
  private Number parseMultiply() {
    if (DEBUG_ON) System.out.println("Parsing multiply");

    MultiplyNode multiply = new MultiplyNode();

    fileScanner.next(); //Discard the multiply

    scannerHasNext(Regex.openParen, "Multiply missing opening '('");

    if (fileScanner.hasNext(Regex.closeParen)) fail("Multiply must contain at least two arguments", "parser", fileScanner, getPopup());

    //Repeat until the closing brace
    while (!fileScanner.hasNext(Regex.closeParen)) {
      if (fileScanner.hasNext(Regex.comma)) fileScanner.next(); //discard and continue
      else if (fileScanner.hasNext(Regex.math)) multiply.add(parseMath());
      else fail("Invalid math operation in multiply", "parser", fileScanner, getPopup());
    }

    if (multiply.getArguments().size() < 2) fail("Multiply must contain at least two arguments", "parser", fileScanner, getPopup());

    //Discard the closing )
    fileScanner.next();

    return multiply;
  }

  /**
   * Parse divide operations.
   * @return a divide node
   */
  private Number parseDivide() {
    if (DEBUG_ON) System.out.println("Parsing divide");

    DivideNode divide = new DivideNode();

    fileScanner.next(); //Discard the divide

    scannerHasNext(Regex.openParen, "Divide missing opening '('");

    if (fileScanner.hasNext(Regex.closeParen)) fail("Divide must contain at least two arguments", "parser", fileScanner, getPopup());

    //Repeat until the closing brace
    while (!fileScanner.hasNext(Regex.closeParen)) {
      if (fileScanner.hasNext(Regex.comma)) fileScanner.next(); //discard and continue
      else if (fileScanner.hasNext(Regex.math)) divide.add(parseMath());
      else fail("Invalid math operation in divide", "parser", fileScanner, getPopup());
    }

    if (divide.getArguments().size() < 2) fail("Divide must contain at least two arguments", "parser", fileScanner, getPopup());

    //Discard the closing )
    fileScanner.next();

    return divide;
  }

  /**
   * Parse a minus operation
   * @return a minus node
   */
  private Number parseMinus() {
    if (DEBUG_ON) System.out.println("Parsing minus");

    MinusNode minus = new MinusNode();

    fileScanner.next(); //Discard the minus

    scannerHasNext(Regex.openParen, "Minus missing opening '('");

    if (fileScanner.hasNext(Regex.closeParen)) fail("Minus must contain at least two arguments", "parser", fileScanner, getPopup());

    //Repeat until the closing brace
    while (!fileScanner.hasNext(Regex.closeParen)) {
      if (fileScanner.hasNext(Regex.comma)) fileScanner.next(); //discard and continue
      else if (fileScanner.hasNext(Regex.math)) minus.add(parseMath());
      else fail("Invalid math operation in minus", "parser", fileScanner, getPopup());
    }

    if (minus.getArguments().size() < 2) fail("Minus must contain at least two arguments", "parser", fileScanner, getPopup());

    //Discard the closing )
    fileScanner.next();

    return minus;
  }

  /**
   * Parse a plus statement.
   * @return a plus node.
   */
  private Number parsePlus() {
    if (DEBUG_ON) System.out.println("Parsing plus");

    PlusNode plus = new PlusNode();

    fileScanner.next(); //Discard the plus

    scannerHasNext(Regex.openParen, "Plus missing opening '('");

    if (fileScanner.hasNext(Regex.closeParen)) fail("Plus must contain at least two arguments", "parser", fileScanner, getPopup());

    //Repeat until the closing brace
    while (!fileScanner.hasNext(Regex.closeParen)) {
      if (fileScanner.hasNext(Regex.comma)) fileScanner.next(); //discard and continue
      else if (fileScanner.hasNext(Regex.math)) plus.add(parseMath());
      else if (fileScanner.hasNext(Regex.mazeCall)) plus.add(parseEvaluateMazeCall());
      else if (fileScanner.hasNext(Regex.name)) plus.add(parseEvaluateNameCall());
      else fail("Invalid math operation in plus", "parser", fileScanner, getPopup());
    }

    //Check that the number of arguments is valid
    if (plus.getArguments().size() < 2) fail("Plus must contain at least two arguments", "parser", fileScanner, getPopup());

    //Discard the closing )
    fileScanner.next();

    return plus;
  }

  /**
   * Parse the runtime evaluation of a name.
   * @return an object that can be evaluated at runtime
   */
  private Number parseEvaluateNameCall() {
    if (DEBUG_ON) System.out.println("Parsing runtime variable evaluation");

    String name = fileScanner.next();
    GetVariableNode getVar = new GetVariableNode(name, handler);

    //Check if it is just a name with no attached method
    if (fileScanner.hasNext(Regex.comma) || fileScanner.hasNext(Regex.closeParen)) {
      return new EvaluateNode(getVar);
    }

    EvaluateNode eval = new EvaluateNode(getVar, parseVariableAction(name));

    return new EvaluateNode(getVar, eval);
  }

  /**
   * Parse the evaluation of a method call so that it wil return a number at runtime.
   * @param name the name of the variable that is being used
   * @return a node that will evaluate a method call at runtime.
   */
  private Exec parseEvaluateMethodCall(String name) {
    if (DEBUG_ON) System.out.println("Parsing runtime method call evaluation");

    MethodNode method = parseMethod();
    GetVariableNode variableNode = new GetVariableNode(name, handler);

    return new EvaluateNode(variableNode, method);
  }

  /**
   * Parse the evaluation of a maze call so that it will return a number at runtime.
   * @return a node which will evaluate the contents of a maze call at runtime
   */
  private Number parseEvaluateMazeCall() {
    if (DEBUG_ON) System.out.println("Parsing runtime maze call evaluation");

    return new EvaluateNode(parseMazeCall());
  }

  /**
   * Parse a number from the program.
   * @return a number object.
   */
  private Number parseNumber() {
    if (DEBUG_ON) System.out.println("Parsing number");
    String number = fileScanner.next();
    if (DEBUG_ON) System.out.println(number);
    return new NumberNode(Double.parseDouble(number));
  }

  /**
   * Display a failure message to the user.
   * Has fields that allow the popup to be created when needed and so
   * that it can be disabled during debugging.
   *
   * @param message the error that occurred.
   * @param origin the origin of the error. Either 'Parser' or 'Execution.'
   * @param fileScanner the file scanner object.
   * @param popup should a popup containing the error be created.
   */
  public static void fail(String message, String origin, Scanner fileScanner, boolean popup) {
    StringBuilder msg;
    if (fileScanner != null) {
      msg = new StringBuilder("FAIL: " + origin + " error: " + message + "\n   @ ...");
      for (int i = 0; i < 5 && fileScanner.hasNext(); i++) {
        msg.append(" ").append(fileScanner.next());
      }
    } else {
      msg = new StringBuilder("FAIL: " + origin + " error: " + message);
    }

    throw new ParserFailure(new JFrame(), msg + "...", popup);
  }

  /**
   * execute the compiled code.
   */
  public void execute(Application application, int delay) {
    //Create a new handler object if required
    if (this.handler == null) this.handler = new Handler(application, delay);

    baseNode.execute();
  }

  /**
   * execute the compiled code.
   */
  public void execute(Application application) {
    //Create a new handler object if required
    if (this.handler == null) this.handler = new Handler(application);

    baseNode.execute();
  }

  /**
   * Print the compiled version of the program
   */
  public void print() {
    System.out.println(baseNode.toString());
  }

  /**
   * Check the equality of two parsers.
   * @param o another parser object.
   * @return a boolean to indicate equality.
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Parser parser = (Parser) o;
    return DEBUG_ON == parser.DEBUG_ON && Objects.equals(fileScanner, parser.fileScanner) && Objects.equals(baseNode, parser.baseNode) && Objects.equals(handler, parser.handler);
  }

  /**
   * Create a hashcode of this object.
   * @return a hashcode.
   */
  @Override
  public int hashCode() {
    return Objects.hash(DEBUG_ON, fileScanner, baseNode, handler);
  }

  /**
   * Set the player if required.
   * @param player the player object.
   */
  public void setPlayer(Player player) {
    handler.setPlayer(player);
  }
}
