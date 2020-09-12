package Parser;

import java.util.regex.Pattern;

//Class containing regular expressions
public class Regex {
    //GRAMMAR
    public static final Pattern openPointy = Pattern.compile("<");
    public static final Pattern closePointy = Pattern.compile(">");
    public static final Pattern comma = Pattern.compile(",");
    public static final Pattern semiColon = Pattern.compile(";");

    //OBJECTS
    public static final Pattern coordinate = Pattern.compile("((?i)Coordinates(?-i))W*");
    public static final Pattern mazeNode = Pattern.compile("((?i)MazeNode(?-i))W*");
    public static final Pattern object = Pattern.compile(coordinate + "|" + mazeNode);

    //DATA STRUCTURES
    public static final Pattern hashmap = Pattern.compile("((?i)Map(?-i))W*");
    public static final Pattern dataStructure = Pattern.compile(hashmap + "");

    //VARIABLES
    public static final Pattern varName = Pattern.compile("([^\\s]+)");

    //EXPRESSIONS
    public static final Pattern variable = Pattern.compile(hashmap + "" + varName);
    public static final Pattern statement = Pattern.compile(dataStructure + "");

}
