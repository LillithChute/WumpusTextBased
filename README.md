# WumpusTextBased
Hunt the Wumpus is a text-based adventure game (Links to an external site.) where a adventurer navigates through a series of caves hunting a monster called the Wumpus.

# Dungeon Builder
Design and implement the interfaces/classes to generate perfect mazes, room mazes, and wrapping room mazes in a way that captures their similarities and accurately represents the relevant data.

## Design
For the purposes of this application the idea is the availability of four different kinds of dungeons.  They are as follows:
1. Perfect:  This type of dungeon has one and only one path from any point in the maze to any other point in the maze. This means that the maze has no inaccessible sections, no circular paths, no open areas.
2. Wrapped perfect dungeon:  For the most part, this is the same as above.  The only change is that  this type of dungeon could have openings at the border, which can then
   be "wrapped" to the other side.
3. Non-perfect dungeon:  each cell in the grid also represent a location in the maze, but there can be multiple paths between any two cells.
4. Wrapped non-perfect dungeon:  Like the wrapped perfect dungeon this is built by wrapping the top and bottom borders, or the left and right borders of a non-perfect maze.
5. The amount of gold the adventurer can gain is 20.  Approximately twenty percent of the rooms will contain that amount.
6. In ten percent of the rooms a thief will appear.  The thief will steal 10% of the adventurer's gold.

## How to run the game
There are three segments to the game.  Each will come up in turn.  There is a brief intro for the game
and then the first section will be displayed.
1. Build the dungeon:
    * You will be asked to set the dungeon size.  This is row x column.
    * Select a perfect or non-perfect dungeon. If a non-perfect dungeon is opted for then the
      number of walls remaining should be selected.
    * Pick whether the dungeon is wrapped or not.

2. Initialize the adventure:
    * Put in the coordinates for the start and goal.

3. Play the game:
    * To move enter up, down, left, or right.
    * Gold will be automagically collected as the adventurer proceeds through the dungeon.
    * If a thief is encountered the gold will automagically be reduced.
    * The adventure is over once the adventurer reaches the goal coordinates.