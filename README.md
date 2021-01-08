<H1>How to write your own parser</H1>

<H2>Statements</H2>
All single line statements must be terminated with a ';'
This does not include comments, if/elseIf/else or loops.

A statement is...
<ul>
    <li>An empty line</li>
    <li>Comment</li>
    <li>Declaration</li>
    <li>Print statement</li>
    <li>Maze Method</li>
    <li>Variable method</li>
    <li>If statement</li>
    <li>For or while loop</li>
</ul>

<H2>Comments</H2>
Comments are written using *** at the beginning and end
<br>
e.g.
<br>
<ul>
    <li>***Some comment***</li>
</ul>

<H2>Print</H2>
Print statements use the keyword print followed by a string, variable reference or mathematical operation.
<br>
e.g.
<br>

print(""), print("Hello world"), print(someVar.getSize()) or print(plus(2, 2))

<H2>Declarations</H2>
Declarations are where a variable is created. 
<br>
The first part is the variable type which may be any of the following...
<ul>
    <li>Comparator</li>
    <li>Node</li>
    <li>Stack</li>
    <li>Queue</li>
    <li>PriorityQueue</li>
    <li>List</li>
    <li>Number</li>
</ul>
<br>
The next part is the variable name. This can be any word that you like.

Declarations have an optional assignment operator '=' which may be followed by a maze method if the varaible
type is a List, Stack, Queue or PriorityQueue.

If the type is a number the '=' may be followed by any number of math operator.
<br><br>
e.g.
<br>
<ul>
    <li>Node parent</li>
    <li>Number nodeCost = plus(1, 2)</li>
    <li>List neighbours = Maze.getNeighbours(parent)</li>
</ul>


<br><br>
<H3>Comparators</H3>
In the case of comparators the declaration must be followed by '->' and then one of the following
maze methods.

<uL>
    <li>Maze.getNeighbourCount(Node)</li>
    <li>Maze.getCost(Node)</li>
    <li>Maze.distanceToDestination(node)</li>
</uL>
<br>
e.g.
<ul>
    <li>Comparator compare -> Maze.getCost(node)</li>
</ul>

<H2>Maze Methods</H2>
maze methods are methods that call functions in the underlying program.
<br>
They are specified using the keyword maze.xyz()
<br><br>
List of all available methods
<br>
<ul>
    <li>
        Maze.getStart()
        <br>
        Gets the node at the start of the maze.
    </li>
    <li>
        Maze.setCost(node, Number cost)
        <br>
        Sets the cost of a node to the provided number
    </li>
    <li>
        Maze.getCost(node)
        <br>
        Takes a node and returns the associated cost
    </li>
    <li>
        Maze.visit(node)
        <br>
        Takes a node and sets its flag to visited.
    </li>
    <li>
        Maze.isDone(node)
        <br>
        Takes a node and checks if it is the destination. It also checks if a path can be traced back to 
        the start using the parent field.
    </li>
    <li>
        Maze.finish()
        <br>
        Flags the program as complete. The underlying program will then check if the solution is valid.
    </li>
    <li>
        Maze.getNeighbours(node)
        <br>
        Returns a list of all the neighbours of the specified node.
    </li>
    <li>
        Maze.isVisited(node)
        <br>
        Check if a given node has been visited.
    </li>
    <li>
        Maze.getDistance(nodeOne, nodeTwo)
        <br>
        returns a number that represents the distance between two given nodes.
    </li>
    <li>
        Maze.distanceToDestination(node)
        <br>
        Returns the distance from this node to the destination node.
    </li>
    <li>
        Maze.getParent(node)
        <br>
        Returns the node that is the parent of this node.
    </li>
    <li>
        Maze.setParent(node, parent)
        <br>
        Sets the parent of a node.
    </li>
    <li>
        Maze.fail("Some message")
        <br>
        Deliberately fail the program. Probaly not useful in an actual maze solver but it is good for testing.
    </li>
</ul>

<H2>Variable Methods</H2>
These are the methods that can be called on variables. The methods that can be called are restricted to
the type of variable.

<br>
They all called in the following way... variable.xyz()

<br>
List of all methods and there relevant types
<ul>
    <li>
        assignComparator()
        <br>
        Types
        <br>
        <ul>
            <li>PriorityQueue</li>
        </ul>
        <br>
        Add a comparator which will be used to sort the priority queue.
        <br><br>
    </li>
    <li>
        add(node)
        <br>
        Types
        <br>
        <ul>
            <li>Queue</li>
            <li>PriorityQueue</li>
            <li>Stack</li>
        </ul>
        <br>
        Add a node to the desired variable.
        <br><br>
    </li>
    <li>
        isEmpty()
        <br>
        Types
        <br>
        <ul>
            <li>Queue</li>
            <li>PriorityQueue</li>
            <li>Stack</li>
            <li>List</li>
        </ul>
        <br>
        Check if the collection is empty.
        <br><br>
    </li>
    <li>
        getNext()
        <br>
        Types
        <br>
        <ul>
            <li>Queue</li>
            <li>PriorityQueue</li>
            <li>Stack</li>
            <li>List</li>
        </ul>
        <br>
        Get the next node in the collection.
        <br><br>
    </li>
    <li>
        getSize()
        <br>
        Types
        <br>
        <ul>
            <li>Queue</li>
            <li>PriorityQueue</li>
            <li>Stack</li>
            <li>List</li>
        </ul>
        <br>
        Get the number of nodes in the collection.
        <br><br>
    </li>
</ul>

<H2>Math Operators</H2>
Math operators are used to do math. Each operator (except root) takes two or more arguments.

The available operators are...
<ul>
    <li>
        plus()
        <br>
        Takes two or more arguments and returns arg1 + arg2 + arg3 etc.
        <br><br>
    </li>
    <li>
        minus()
        <br>
        Takes two or more arguments and subtracts arg1 - arg2 - arg3 etc.
        <br><br>
    </li>
    <li>
        divide()
        <br>
        Takes two or more arguments and divides arg1 / arg2 / arg3 etc.
        <br><br>
    </li>
    <li>
        multiply()
        <br>
        Takes two or more arguments and multiplies arg1 * arg2 * arg3 etc.
        <br><br>
    </li>
    <li>
        power()
        <br>
        Takes two or more arguments and subtracts arg1 ^ arg2 ^ arg3 etc.
        <br><br>
    </li>
    <li>
        root
        <br>
        Takes a single argument and returns the square root.
        <br><br>
    </li>
</ul>


<H2>Numbers</H2>
The number type is the representation of a number used by the underling program.

They can be created by declaration e.g. Number cost = 34.

They may also be the result of a math operator, Maze Method or Variable method.

<H2>Comparators</H2>
Comparators are used to sort PriorityQueues.

There are two different comparators available to use, and they are...
<ul>
    <li>Maze.getCost(node)</li>
    <li>Maze.getNeighbourCount(node)</li>
    <li>Maze.distanceToDestination(node)</li>
</ul>

<H2>Nodes</H2>
Nodes are the junctions, corners or dead ends in the maze. The underlying program keeps a map of all the nodes which can 
be accessed by certain methods in the parser.

<H2>Queues</H2>
A queue is a list of nodes. The first node that you put in will be the first node that you can take out.
Any subsequent nodes added will go to the back of the queue.

<H2>Priority Queues</H2>
A PriorityQueue is almost exactly the same as a regular queue (see Queue). The only difference is that the
highest priority nodes as decided by the comparator (see comparator) will go to the front.

<H2>List</H2>
A list is like a shopping list. You can add or remove nodes from it at any position.

<H2>Stack</H2>
A stack is the opposite of a queue. The last node that you put in is the first that you can take out.
You can think of it like a stack of paper, the piece that you put on the top is the first one that you
have to remove.

<H2>For Loops</H2>
A for loop iterates through every node in a give list.

e.g. <br>
```java
List someList = Maze.getNeighbours(node); 
for (node: someList) {
  node.xyz();
}
```
<br>

Each for loop declares a local variable which can be used within the loop.

There can be any number of statements within the loop.

<H2>While Loops</H2>
A while loop will continue to execute while the provided condition is true. As soon as 
the condition is false, the loop will exit.

There can be any number of statements within the loop.

e.g.<br>
```java
Condition someCondition = gt(4, 4);
while(someCondition) {
    statements...
}
```
<br>

Note that in the above case the loop is infinite because 4 will never be greater than itself.

<H2>If</H2>
An if statement is run if a given condition is true.

Inside the if statement there may be any number of other statements.

e.g.<br>
````java
Condition someCondition = eq(4, 4);
if(someCondition) {
  statements...
}
````


If statements can also be chained together to form an else if statement.

In this case if the first statement is not true, the second statement is checked and then executed if 
its condition is true. There may be any number of else if braches on the same if statement

```java
Condition someCondition1= lt(3, 2);
Condition someCondition2 = eq(2, 1);
Condition someCondition3 = eq(1, 1); 
if(someCondition1) {
  statements...
} else if (someCondition2) {
  statements...
} else if (someCondition2) {
  statements...
}
```


If statements may also include an else that is run in none of the conditions are true.

```java
Condition someCondition = eq(4, 4); 
if(someCondition) {
  statements...
} else {
  statements...
}
```


or

```java
Condition someCondition1= lt(3, 2);
Condition someCondition2 = eq(2, 1); 
Condition someCondition3 = eq(1, 1); 
if(someCondition1) {
  statements...
} else if (someCondition2) { 
  statements...<br>
} else if (someCondition2) { 
  statements...
} else {
  statements...
}
```


<H2>Conditions</H2>
A condition is something that can either be true or false.

There are several conditions available to use.
<ul>
    <li>Equal: eq()</li>
    <li>Less Than: lt()</li>
    <li>Greater Than: gt()</li>
</ul>

<H2>Less Than</H2>
lt()

Takes two arguments and checks if the first is less than the second.

The arguments may be any of the following...
<ul>
    <li>A number: 5, 6, -10, 4,35 etc</li>
    <li>Certain variable methods that return a number</li>
    <li>Certain maze methods that return a number</li>
    <li>A math operation</li>
</ul>

<H2>Greater Than</H2>
gt()

Takes two arguments and checks if the first is greater than the second.

The arguments may be any of the following...
<ul>
    <li>A number: 5, 6, -10, 4,35 etc</li>
    <li>Certain variable methods that return a number</li>
    <li>Certain maze methods that return a number</li>
    <li>A math operation</li>
</ul>

<H2>Equal To</H2>
eq()

Takes two arguments and checks if they are equal.

The arguments may be any of the following...
<ul>
    <li>A number: 5, 6, -10, 4,35 etc</li>
    <li>Certain variable methods that return a number</li>
    <li>Certain maze methods that return a number</li>
    <li>A math operation</li>
</ul>


