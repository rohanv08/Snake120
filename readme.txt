=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=
CIS 120 Game Project README
PennKey: rover
=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=

===================
=: Core Concepts :=
===================

- List the four core concepts, the features they implement, and why each feature
  is an appropriate use of the concept. Incorporate the feedback you got after
  submitting your proposal.

  1. 2D Arrays - I've used 2D arrays to represent my board state i.e the placement of food and the
     bombs. A 1 in the 2D array (labeled as the occupancyMatrix) reprents a food cell, (the thickness
     of food can be changed, so a single food bit can occupy multiple cells) and a 2 represents a bomb 
     (again, according to the side of the sqaure bomb, a bomb may occupy multiple cells). Note I do not
     store any information regarding my snake here. I made the occupancy matrix because when moving the snake, 
     I have to constantly check whether the snake has landed on the food or a bomb, and storing all this in 
     a 2D array makes it really easy, and not to mention effiecient, to check. 

  2. Collections - I've used a linked list of my "Snake Segments" to represent my snake. Every time the snake
     bends or curves, a new "Snake Segment" is added to the list. Essentially, the size of my snakeSegments
     linked list is the size of the number of bends the snake has plus one. The head of this linked list represents
     the head segment of the snake. The snake segment itself is a class, which stores the starting row, ending row,
     starting column, ending column and the direction of that particular segment.

  3. File I/O - I use file writing whenever the user presses S to save and exit the game. I essentially store
     (and therefore write) all neccessary variables so that I'm able to re-create the exact situation the user
     left the game in. As for file reading, I use it whenever the user chooses the option to "Load saved game"
     on the starting page. If there is no data, my game alerts the user and suggests to start a new game. If the 
     data is corrupted, again, my program suggests the user to start a new game. Otherwise, if all is good, I 
     re-create the exact same previous state and resume the game.

  4. Novel Recursive or Linked Data Structure - I've used the concept of a graph in my game, where the bombs represent
     the vertices of that graph and a connection between any two bombs is an edge. In my game, I randomly spawn 20 bombs and
     randomly create edges between pairs of vertices (basically I set 1 or 0 in the upper triangular part of the adjacency matrix
     since this is an undirected graph). In my code, the adjacency graph is labelled as bombMatrix. My game 
     design is to randomly start the timer of one of the bombs (which then starts blinking, indicating that it is about to blast) 
     and once this bomb blasts, I activate the timer of all connected bombs (i.e bombs which had an 
     edge to this blasted bomb). Note that for all bombs to blast I would need a conencted graph, so I make the 
     graph connected by (randomly) adding an edge between vertices of connected components.

=========================
=: Your Implementation :=
=========================

- Provide an overview of each of the classes in your code, and what their
  function is in the overall game.
  1. Console - Handles all the drawing. This class is responsible for drawing the snake, food, 
  bombs, bomb edges, bomb blinking and the bomb blast animation.
  
  2. Snake Segment - Stores the start row, start col, end row, end col and direction of each snake 
  segment. My snake is a collection of snake segments, so this class functions as the building block 
  of my snake.
  
  3. Bomb - Stores the X (column), Y (row), timer (time left to blast), and the blast radius of each 
  bomb.
  
  4. Food - Stores the column, row, score and lifetime of each food. Foods automatically disappear if
  the snake couldn't get to the food in 10 seconds. I've also added the score variable to food (although
  it is right now always set to 1), because I may want to introduce multiple foods with different point values
  in the future and this makes it really easy.
  
  5. Snake - This is where the magic happens. This is the class which handles which direction my snake's
  moving in, when to expand it's head, when to shrink its tail and whether the snake has eaten the food, hit the board,
  landed on a bomb or even hit it self. 


- Were there any significant stumbling blocks while you were implementing your
  game (related to your design, or otherwise)?
  I think the main area where I was stuck for a while was how to incorporate the four core concepts.
  Earlier, I was planning to implement the classic version of the snake game, but this only used up 3
  of my 4 core concepts. This is why I had to modify and improve the game a little bit by introducing 
  bombs and making it more interesting to play!
 


- Evaluate your design. Is there a good separation of functionality? How well is
  private state encapsulated? What would you refactor, if given the chance?
  I've tried my best to ensure good separation of functionality as well as use getter methods when 
  accessing private variables. The only class where I haven't used getter setter methods is the Game
  class, where I've mostly used static variables since those variables (size of board (width, height), 
  score, number of bombs, snake radius (thickness of the snake), food radius (lenght of side of food 
  square), occupancyMatrix) are constant throughout the game run.



========================
=: External Resources :=
========================

- Cite any external resources (libraries, images, tutorials, etc.) that you may
  have used while implementing your game.
  Only Javadocs.
