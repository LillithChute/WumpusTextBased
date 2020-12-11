# WumpusTextBased
Hunt the Wumpus is a text-based adventure game (Links to an external site.) where an adventurer navigates through a series of caves hunting a monster called the Wumpus.

## Concept Design
This application will use the dungeon builder from homework 3 as it's base.  That is to say, the 
dungeon will be configured and laid out in the same fashion.  WHat will be different is that now the
rooms will be built with the idea of being occupied by new items.  The rules are as follows:
1. Exactly 1 cave has the Wumpus. The Wumpus doesn't move but can be smelled in adjacent caves.
2. There are n bottomless pits that provide a draft that can be felt in adjacent caves. 
   this is done by supplying a percentage of caves with pits (a number between 0 and 1).
3. There are m caves with superbats. Entering a cave with superbats means that there is a 50% 
   chance that the superbats will pick up the player and drop them in another place in the maze.  Again, 
   the user will provide a number between 0 and 1 for the amount of bats.
4. If a cave has both a bottomless pit and the superbats, the bats have a 50% chance of picking up 
   the player before they fall into the bottomless pit.
5. A player can attempt to slay the Wumpus by specifying a direction and distance in which to shoot 
   their crooked arrow. Distance is defined as the number of caves (but not tunnels) that an 
   arrow travels. Arrows travel freely down tunnels (even crooked ones) but only travel a 
   straight line through a cave.
6. Distance must be exact. For example, if you shoot an arrow 3 rooms to the east and the Wumpus 
   was only 2 rooms to the east, you miss the Wumpus.
7. You win by killing the Wumpus
8. You lose by being eaten, falling in a pit, or running out of arrows.

## Code Design
1.  This application uses the MVC design.
2.  There is an interface for the controller, adventurer, room, and dungeon.
3.  The model remains essentially the same as previous, with dungeon, room, and adventurer.  These 
    models are adjusted to include bats, pits, firing arrows, and the wumpus; 
    removing the thief and gold.
4.  The major difference is in the controller handling the functionality previously given to the 
    driver program.
    

## How to run the game
There are three segments to the game.  Each will come up in turn.  There is a brief intro for the game
and then the first section will be displayed.
1. Build the dungeon:
    * You will be asked to set the dungeon size.  This is row x column.
    * Select a perfect or non-perfect dungeon.
    * Pick whether the dungeon is wrapped or not.
    * Pick the percentage of bats (between 0 and 1 for example 0.17)
    * Pick the percentage of pits (between 0 and 1 for example 0.17)
    * Input the number of arrows greater than 0.
    * You will be told if the game is winnable or not.  If not you can play anyway or type Quit to end.
   

2. Begin the game:
    * You will be given your location and available directions.
    * Type in Move or Shoot to select what you will do.
    * If you type Move, you will be prompted to input the direction you wish to move in.
    * If you type shoot, you will be asked which direction and then the number of caves you would like 
      the arrow to travel.
      

3. Example of play:
```
Input the number of rows:
3
Input the number of columns:
4
Input the number of walls (0 - 6):
6
Is the dungeon perfect(true/false)?
true
Is the dungeon wrapping(true/false)?
false
Input the percentage of bats (A number between 0 and 1):
.3
Input the percentage of pits (A number between 0 and 1):
.1
Input the number of arrows:
4
You are in cave (2, 1). Tunnels lead to the E, W, N
Shoot or Move?
Move
Where to?
N
You smell a Wumpus! Nasty!
You are in cave (1, 1). Tunnels lead to the E, W, N, S Shoot or Move?
Shoot
Towards direction(E/W/N/S)?
W
No. of caves?
1
You missed the wumpus!
You missed the wumpus, and you have 3 arrows remains.Shoot or Move?
Shoot
Towards direction(E/W/N/S)?
N
No. of caves?
1
You missed the wumpus!
You missed the wumpus, and you have 2 arrows remains.Shoot or Move?
Shoot
Towards direction(E/W/N/S)?
E
No. of caves?
1
You hear a howl and thud. You killed the wumpus!
You shot to the wumpus!
```

4.  A second example:
```
Input the number of rows:
5
Input the number of columns:
4
Input the number of walls (0 - 12):
8
Is the dungeon perfect(true/false)?
false
Is the dungeon wrapping(true/false)?
true
Input the percentage of bats (A number between 0 and 1):
.2
Input the percentage of pits (A number between 0 and 1):
.1
Input the number of arrows:
4
You are in cave (2, 1). Tunnels lead to the E, N, S
Shoot or Move?
Move
Where to?
E
You feel a draft.
You are in cave (2, 2). Tunnels lead to the E, W, N, S Shoot or Move?
Move
Where to?
E
You feel a draft.
You are in cave (2, 3). Tunnels lead to the W, N, S Shoot or Move?
Move
Where to?
S
You feel a draft.
You are in cave (3, 3). Tunnels lead to the E, W, N, S Shoot or Move?
Move
Where to?
W
You feel a draft.
You are in cave (3, 2). Tunnels lead to the E, W, N, S Shoot or Move?
Move
Where to?
S
You smell something ghastly very near.
You are in cave (4, 2). Tunnels lead to the W, N, S Shoot or Move?
Move
Where to?
W
You fell into the bottomless pit and died!
You are in cave (4, 1). Tunnels lead to the E, W, N, S
```