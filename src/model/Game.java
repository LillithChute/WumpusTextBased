package model;

import interfaces.IDungeon;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.Set;

public class Game implements IDungeon {
  private List<Integer> savedWall;
  private int rows;
  private int cols;
  private int remains;
  private Room[][] rooms;
  private boolean isWrapping;
  private boolean isPerfect;
  private List<int[]> caveLst;
  private double batPercent;
  private double pitPercent;
  private boolean isGameOver;
  private boolean isShootSuccess;
  private Room wumpus;
  private String alert;
  private int flag;
  private Adventurer adventurer1;
  private Adventurer currAdventurer;
  private List<Room> walkedRooms;
  private int lives;

  /** Empty constructor. */
  public Game() {
    // Empty constructor as input of the controller.
  }

  /**
   * Constructor for Game class.
   *
   * @param rows The number of rows in the dungeon.
   * @param cols The number of columns in the dungeon.
   * @param remains The number of walls that should remain.
   * @param isPerfect The dungeon is perfect or not.
   * @param isWrapping The dungeon is wrapping or not.
   * @param playerNum The total number of players.
   */
  public Game(
      int rows,
      int cols,
      int remains,
      boolean isPerfect,
      boolean isWrapping,
      double batPercent,
      double pitPercent,
      int arrows,
      int playerNum)
      throws IllegalArgumentException {
    this.rows = rows;
    this.cols = cols;
    this.remains = remains;
    this.isWrapping = isWrapping;
    this.isPerfect = isPerfect;
    savedWall = new ArrayList<>();
    caveLst = new ArrayList<>();
    this.batPercent = batPercent;
    this.pitPercent = pitPercent;
    isGameOver = false;
    isShootSuccess = false;
    flag = 1;
    wumpus = null;
    alert = "";
    adventurer1 = new Adventurer();
    walkedRooms = new ArrayList<>();
    lives = playerNum;

    if (rows < 0) {
      throw new IllegalArgumentException("The number of rows can't be negative!");
    }

    if (cols < 0) {
      throw new IllegalArgumentException("The number of columns can't be negative!");
    }

    if (batPercent < 0) {
      throw new IllegalArgumentException("The percentage value for bats is invalid!");
    }

    if (pitPercent < 0) {
      throw new IllegalArgumentException("The percentage value for pits is invalid!");
    }

    if (arrows <= 0) {
      throw new IllegalArgumentException("The number of arrows is invalid!");
    }

    generatePerfectDungeon();

    if (!isPerfect) {
      if (isWrapping && remains < cols * rows + rows * cols - rows * cols + 1 && remains >= 0) {
        generateDungeonNonWrapping();
      } else if (!isWrapping
          && remains < (cols - 1) * rows + (rows - 1) * cols - rows * cols + 1
          && remains >= 0) {
        generateDungeonNonWrapping();
      } else {
        throw new IllegalArgumentException("The walls remaining input is not valid!");
      }
    }

    assignCaveTunnel();
    linkTunnel();
    setCaveWithWumpus();
    setCavesWithPits();
    setCavesWithSuperbats();
    setStartPosition(1);
    currAdventurer = adventurer1;
    adventurer1.setNumberOfArrows(arrows);
  }

  @Override
  public int getAdventurerRound() {
    return flag;
  }

  private void setStartPosition(int flag) {
    Random random = new Random();
    int randomInt;

    if (flag == 1) {
      random.setSeed(1000);
    } else {
      random.setSeed(50);
      random.nextInt(caveLst.size());
    }

    randomInt = random.nextInt(caveLst.size());
    int x = caveLst.get(randomInt)[0];
    int y = caveLst.get(randomInt)[1];
    Room temp = rooms[x][y];

    while (temp.getIsWumpus() || temp.getIsPit() || temp.getHasBat()) {
      randomInt = random.nextInt(caveLst.size());
      x = caveLst.get(randomInt)[0];
      y = caveLst.get(randomInt)[1];
      temp = rooms[x][y];
    }

    if (flag == 1) {
      adventurer1.setAdventurerStartLocation(x, y);
    }
  }

  /** Generate a Wrapping or a Non-wrapping perfect dungeon. */
  private void generatePerfectDungeon() {
    int[][] cellToUnion = new int[rows][cols];
    List<Integer> walls = new ArrayList<>();
    Map<Integer, List<Integer>> unionToCells = new HashMap<>();
    generateLayout(rows, cols);
    int totalWalls;
    if (isWrapping) {
      totalWalls = cols * rows + rows * cols;
    } else {
      totalWalls = (cols - 1) * rows + (rows - 1) * cols;
    }
    // Initialize the cellToUnion and unionToCells
    setCaveRelationship(cellToUnion, unionToCells);

    for (int i = 0; i < totalWalls; i++) {
      walls.add(i);
    }

    int removedCount = kruskalWallRemoval(cellToUnion, walls, unionToCells, totalWalls, isWrapping);
    if (removedCount == rows * cols - 1) {
      savedWall.addAll(walls);
    } else {
      for (int wall : walls) {
        int[][] cellsPositions;
        if (!isWrapping) {
          cellsPositions = getCellsPositionByWall(wall);
        } else {
          cellsPositions = getCellsPositionByWallWrapping(wall);
        }
        int cell1X = cellsPositions[0][0];
        int cell1Y = cellsPositions[0][1];
        int cell2X = cellsPositions[1][0];
        int cell2Y = cellsPositions[1][1];
        linkCells(cell1X, cell1Y, cell2X, cell2Y);

        setUnionNum(
            unionToCells, cellToUnion, cellToUnion[cell1X][cell1Y], cellToUnion[cell2X][cell2Y]);
      }
    }
  }

  /**
   * Assign the union number for each cave as well as the caves in each union.
   *
   * @param cellToUnion The union number of the cave.
   * @param unionToCells The caves in a union.
   */
  private void setCaveRelationship(
      int[][] cellToUnion, Map<Integer, List<Integer>> unionToCells) {
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < cols; j++) {
        cellToUnion[i][j] = i * cols + j;
        List<Integer> temp = new ArrayList<>();
        temp.add(i * cols + j);
        unionToCells.put(i * cols + j, temp);
      }
    }
  }

  /**
   * Use the Kruskal Algorithm to implement the wall removal process.
   *
   * @param cellToUnion The union number of the cell.
   * @param walls The walls that still remains in the dungeon.
   * @param unionToCells The cells in a union.
   * @param totalWalls The initial number of walls at the beginning.
   * @return The number of walls that has been removed.
   */
  private int kruskalWallRemoval(
      int[][] cellToUnion,
      List<Integer> walls,
      Map<Integer, List<Integer>> unionToCells,
      int totalWalls,
      boolean isWrapping) {
    Random random = new Random();
    random.setSeed(1000);
    int removedCount = 0;
    while (removedCount < rows * cols - 1 && savedWall.size() < totalWalls - rows * cols + 1) {
      int randomInt = random.nextInt(walls.size());
      int[][] cellsPositions;

      if (isWrapping) {
        cellsPositions = getCellsPositionByWallWrapping(walls.get(randomInt));
      } else {
        cellsPositions = getCellsPositionByWall(walls.get(randomInt));
      }
      int cell1X = cellsPositions[0][0];
      int cell1Y = cellsPositions[0][1];
      int cell2X = cellsPositions[1][0];
      int cell2Y = cellsPositions[1][1];

      if (cellToUnion[cell1X][cell1Y] == cellToUnion[cell2X][cell2Y]) {
        savedWall.add(walls.get(randomInt));
      } else {
        linkCells(cell1X, cell1Y, cell2X, cell2Y);
        removedCount++;
        setUnionNum(
            unionToCells, cellToUnion, cellToUnion[cell1X][cell1Y], cellToUnion[cell2X][cell2Y]);
      }

      walls.remove(randomInt);
    }

    return removedCount;
  }

  /**
   * Combine the two unions into one.
   *
   * @param unionToCells The cells in a union.
   * @param cellToUnion The union number of the cell.
   * @param unionNum1 The index of the one cell by the wall.
   * @param unionNum2 The index of the other cell by the wall.
   */
  private void setUnionNum(
      Map<Integer, List<Integer>> unionToCells, int[][] cellToUnion, int unionNum1, int unionNum2) {
    for (int cellIndex : unionToCells.get(unionNum1)) {
      cellToUnion[cellIndex / cols][cellIndex % cols] = unionNum2;
    }
    unionToCells.get(unionNum2).addAll(unionToCells.get(unionNum1));
    unionToCells.remove(unionNum1);
  }

  /**
   * Link two cells by their locations.
   *
   * @param cell1X The horizontal index of cell 1.
   * @param cell1Y The vertical index of cell 1.
   * @param cell2X The horizontal index of cell 2.
   * @param cell2Y The vertical index of cell 2.
   */
  private void linkCells(int cell1X, int cell1Y, int cell2X, int cell2Y) {
    Room room1 = rooms[cell1X][cell1Y];
    Room room2 = rooms[cell2X][cell2Y];
    if (cell1X == cell2X) {
      room1.setNextRoom(room2, "right");
      room2.setNextRoom(room1, "left");
    } else {
      room1.setNextRoom(room2, "down");
      room2.setNextRoom(room1, "up");
    }
  }

  /**
   * Initialize the dungeon by generating m * n empty cells.
   *
   * @param rows The number of rows in the dungeon.
   * @param cols The number of columns in the dungeon.
   */
  private void generateLayout(int rows, int cols) {
    rooms = new Room[rows][cols];
    Random random = new Random();
    random.setSeed(1000);
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < cols; j++) {
        rooms[i][j] = new Room();
      }
    }
  }

  /**
   * Get the location of the two cells by a input wall index for non wrapping dungeon.
   *
   * @param wallIndex The wall index.
   * @return The location array of the two cells.
   */
  private int[][] getCellsPositionByWall(int wallIndex) {
    // The wall index is ordering by vertical first and then horizontal.
    if (wallIndex < rows * (cols - 1)) {
      int colIndex = wallIndex % (cols - 1);
      int rowIndex = wallIndex / (cols - 1);
      return new int[][] {{rowIndex, colIndex}, {rowIndex, colIndex + 1}};
    } else {
      wallIndex -= rows * (cols - 1);
      int colIndex = wallIndex % cols;
      int rowIndex = wallIndex / cols;
      return new int[][] {{rowIndex, colIndex}, {rowIndex + 1, colIndex}};
    }
  }

  /**
   * Get the two cells positions by the wall for wrapping.
   *
   * @param wallIndex The wall index.
   * @return The location array of the two cells.
   */
  private int[][] getCellsPositionByWallWrapping(int wallIndex) {
    if (wallIndex < rows * cols) {
      int colIndex = wallIndex % cols;
      int rowIndex = wallIndex / cols;

      if (colIndex == 0) {
        return new int[][] {{rowIndex, cols - 1}, {rowIndex, colIndex}};
      } else {
        return new int[][] {{rowIndex, colIndex - 1}, {rowIndex, colIndex}};
      }
    } else {
      wallIndex -= rows * cols;
      int colIndex = wallIndex % cols;
      int rowIndex = wallIndex / cols;

      if (rowIndex == 0) {
        return new int[][] {{rows - 1, colIndex}, {rowIndex, colIndex}};
      } else {
        return new int[][] {{rowIndex - 1, colIndex}, {rowIndex, colIndex}};
      }
    }
  }

  /** Generate a Non-wrapping room dungeon. */
  private void generateDungeonNonWrapping() {
    int numToDelete = savedWall.size() - remains;
    Random random = new Random();
    random.setSeed(1000);
    for (int i = 0; i < numToDelete; i++) {
      int randomInt = random.nextInt(savedWall.size());
      int[][] cellsPositions;

      if (isWrapping) {
        cellsPositions = getCellsPositionByWallWrapping(savedWall.get(randomInt));
      } else {
        cellsPositions = getCellsPositionByWall(savedWall.get(randomInt));
      }

      int cell1X = cellsPositions[0][0];
      int cell1Y = cellsPositions[0][1];
      int cell2X = cellsPositions[1][0];
      int cell2Y = cellsPositions[1][1];
      linkCells(cell1X, cell1Y, cell2X, cell2Y);
      savedWall.remove(randomInt);
    }
  }

  /** Turn left operation. */
  public void goLeft() {
    int playerPosX = currAdventurer.getAdventurerLocation()[0];
    int playerPosY = currAdventurer.getAdventurerLocation()[1];
    if (playerPosY - 1 >= 0 && rooms[playerPosX][playerPosY].getRoomOnTheLeft() != null) {
      playerPosY--;
      currAdventurer.setAdventurerLocation(playerPosX, playerPosY);
    } else {
      if (isWrapping && playerPosY - 1 < 0
              && rooms[playerPosX][playerPosY].getRoomOnTheLeft() != null) {
        playerPosY = cols - playerPosY - 1;
        currAdventurer.setAdventurerLocation(playerPosX, playerPosY);
      }
    }
  }

  /** Turn right operation. */
  public void goRight() {
    int playerPosX = currAdventurer.getAdventurerLocation()[0];
    int playerPosY = currAdventurer.getAdventurerLocation()[1];
    if (playerPosY + 1 < cols && rooms[playerPosX][playerPosY].getRoomOnTheRight() != null) {
      playerPosY++;
      currAdventurer.setAdventurerLocation(playerPosX, playerPosY);
    } else {
      if (isWrapping
          && playerPosY + 1 >= cols
          && rooms[playerPosX][playerPosY].getRoomOnTheRight() != null) {
        playerPosY = 0;
        currAdventurer.setAdventurerLocation(playerPosX, playerPosY);
      }
    }
  }

  /** Turn up operation. */
  public void goUp() {
    int playerPosX = currAdventurer.getAdventurerLocation()[0];
    int playerPosY = currAdventurer.getAdventurerLocation()[1];
    if (playerPosX - 1 >= 0 && rooms[playerPosX][playerPosY].getRoomAbove() != null) {
      playerPosX--;
      currAdventurer.setAdventurerLocation(playerPosX, playerPosY);
    } else {
      if (isWrapping && playerPosX - 1 < 0
              && rooms[playerPosX][playerPosY].getRoomAbove() != null) {
        playerPosX = rows - 1;
        currAdventurer.setAdventurerLocation(playerPosX, playerPosY);
      }
    }
  }

  /** Turn down operation. */
  public void goDown() {
    int playerPosX = currAdventurer.getAdventurerLocation()[0];
    int playerPosY = currAdventurer.getAdventurerLocation()[1];
    if (playerPosX + 1 < rows && rooms[playerPosX][playerPosY].getRoomBelow() != null) {
      playerPosX++;
      currAdventurer.setAdventurerLocation(playerPosX, playerPosY);
    } else {
      if (isWrapping
          && playerPosX + 1 >= rows
          && rooms[playerPosX][playerPosY].getRoomBelow() != null) {
        playerPosX = 0;
        currAdventurer.setAdventurerLocation(playerPosX, playerPosY);
      }
    }
  }

  @Override
  public String getAdventurerLocation() {
    StringBuilder sb = new StringBuilder();
    int playerPosX = currAdventurer.getAdventurerLocation()[0];
    int playerPosY = currAdventurer.getAdventurerLocation()[1];
    sb.append("You are in cave (").append(playerPosX).append(", ").append(playerPosY).append("). ");
    sb.append("Tunnels lead to the ");
    Room curr = rooms[playerPosX][playerPosY];
    if (curr.getRoomOnTheRight() != null) {
      sb.append("E, ");
    }
    if (curr.getRoomOnTheLeft() != null) {
      sb.append("W, ");
    }
    if (curr.getRoomAbove() != null) {
      sb.append("N, ");
    }
    if (curr.getRoomBelow() != null) {
      sb.append("S, ");
    }
    sb.delete(sb.length() - 2, sb.length());
    return sb.toString();
  }

  private String convertWallsToString() {
    StringBuilder s = new StringBuilder();
    for (Integer integer : savedWall) {
      s.append(integer);
      s.append(" ");
    }
    return s.toString();
  }

  @Override
  public String toString() {
    String s = "";
    int[] start = currAdventurer.getAdventurerStartLocation();
    if (isPerfect && isWrapping) {
      s +=
          String.format(
              "The dungeon is %d * %d, and it is a wrapping perfect dungeon. "
                  + "The start point of player is (%d, %d). The saved walls are numbered by: "
                  + convertWallsToString(),
              rows,
              cols,
              start[0],
              start[1]);
    }

    if (isPerfect && !isWrapping) {
      s +=
          String.format(
              "The dungeon is %d * %d, and it is a non-wrapping perfect dungeon. "
                  + "The start point of player is (%d, %d). The saved walls are numbered by: "
                  + convertWallsToString(),
              rows,
              cols,
              start[0],
              start[1]);
    }

    if (!isPerfect && isWrapping) {
      s +=
          String.format(
              "The dungeon is %d * %d, and it is a wrapping room dungeon. "
                  + "The start point of player is (%d, %d). The saved walls are numbered by: "
                  + convertWallsToString(),
              rows,
              cols,
              start[0],
              start[1]);
    }

    if (!isPerfect && !isWrapping) {
      s +=
          String.format(
              "The dungeon is %d * %d, and it is a non-wrapping room dungeon. "
                  + "The start point of player is (%d, %d). The saved walls are numbered by: "
                  + convertWallsToString(),
              rows,
              cols,
              start[0],
              start[1]);
    }
    return s;
  }

  /** Traverse the whole dungeon, and assign each cell as a cave or a tunnel. */
  private void assignCaveTunnel() {
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < cols; j++) {
        Room curr = rooms[i][j];

        if (getNumberOfWalls(curr) == 2) {
          curr.setIsCave(false);
          curr.setIsTunnel(true);
        } else {
          curr.setIsCave(true);
          curr.setIsTunnel(false);
          int[] temp = new int[2];
          temp[0] = i;
          temp[1] = j;
          caveLst.add(temp);
        }
      }
    }
  }

  /** Link the two caves if there is a tunnel or tunnels between them. */
  private void linkTunnel() {
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < cols; j++) {
        Room curr = rooms[i][j];
        if (curr.getIsRoom()) {
          if (curr.getRoomAbove() != null && curr.getRoomAbove().getIsTunnel()) {
            linkTunnelHelper(curr, curr.getRoomAbove(), 0);
          }
          if (curr.getRoomBelow() != null && curr.getRoomBelow().getIsTunnel()) {
            linkTunnelHelper(curr, curr.getRoomBelow(), 1);
          }
          if (curr.getRoomOnTheLeft() != null && curr.getRoomOnTheLeft().getIsTunnel()) {
            linkTunnelHelper(curr, curr.getRoomOnTheLeft(), 2);
          }
          if (curr.getRoomOnTheRight() != null && curr.getRoomOnTheRight().getIsTunnel()) {
            linkTunnelHelper(curr, curr.getRoomOnTheRight(), 3);
          }
        }
      }
    }
  }

  /** Update the adventurer's location after passing through the tunnel. */
  private void linkTunnelHelper(Room curr, Room next, int flag) {
    int originFlag = flag;

    while (next.getIsTunnel()) {
      if (next.getRoomBelow() != null && flag != 0) {
        next = next.getRoomBelow();
        flag = 1;
      } else if (next.getRoomAbove() != null && flag != 1) {
        next = next.getRoomAbove();
        flag = 0;
      } else if (next.getRoomOnTheLeft() != null && flag != 3) {
        next = next.getRoomOnTheLeft();
        flag = 2;
      } else if (next.getRoomOnTheRight() != null && flag != 2) {
        next = next.getRoomOnTheRight();
        flag = 3;
      } else {
        break;
      }
    }
    if (originFlag == 0) {
      curr.setNextRoom(next, "up");
    }
    if (originFlag == 1) {
      curr.setNextRoom(next, "down");
    }
    if (originFlag == 2) {
      curr.setNextRoom(next, "left");
    }
    if (originFlag == 3) {
      curr.setNextRoom(next, "right");
    }

    if (flag == 0) {
      next.setNextRoom(curr, "down");
    } else if (flag == 1) {
      next.setNextRoom(curr, "up");
    } else if (flag == 2) {
      next.setNextRoom(curr, "right");
    } else if (flag == 3) {
      next.setNextRoom(curr, "left");
    }
  }

  /**
   * Get the number of walls surrounded by the current cell.
   *
   * @param curr The current cell.
   * @return The number of walls that the current cell has.
   */
  private int getNumberOfWalls(Room curr) {
    int count = 0;

    if (curr.getRoomBelow() == null) {
      count++;
    }

    if (curr.getRoomAbove() == null) {
      count++;
    }

    if (curr.getRoomOnTheLeft() == null) {
      count++;
    }

    if (curr.getRoomOnTheRight() == null) {
      count++;
    }

    return count;
  }

  /** Assign a random cave to have the wumpus. */
  private void setCaveWithWumpus() {
    Random random = new Random();
    random.setSeed(500);
    int index = random.nextInt(caveLst.size());
    int[] wumpusLocation = caveLst.get(index);
    wumpus = rooms[wumpusLocation[0]][wumpusLocation[1]];
    wumpus.setIsWumpus();

    if (wumpus.getRoomOnTheRight() != null) {
      wumpus.getRoomOnTheRight().setCloseToWumpus();
    }

    if (wumpus.getRoomOnTheLeft() != null) {
      wumpus.getRoomOnTheLeft().setCloseToWumpus();
    }

    if (wumpus.getRoomAbove() != null) {
      wumpus.getRoomAbove().setCloseToWumpus();
    }

    if (wumpus.getRoomBelow() != null) {
      wumpus.getRoomBelow().setCloseToWumpus();
    }
  }

  /** Assign some random caves as bottomless pits. */
  private void setCavesWithPits() {
    int caveNum = caveLst.size();
    int pitNum = (int) (caveNum * pitPercent);
    Random random = new Random();
    random.setSeed(300);

    for (int i = 0; i < pitNum; i++) {
      int index = random.nextInt(caveLst.size());
      int[] pitPos = caveLst.get(index);
      Room pit = rooms[pitPos[0]][pitPos[1]];
      pit.setIsPit();

      if (pit.getRoomOnTheRight() != null) {
        pit.getRoomOnTheRight().setCloseToPit();
      }

      if (pit.getRoomOnTheLeft() != null) {
        pit.getRoomOnTheLeft().setCloseToPit();
      }

      if (pit.getRoomAbove() != null) {
        pit.getRoomAbove().setCloseToPit();
      }

      if (pit.getRoomBelow() != null) {
        pit.getRoomBelow().setCloseToPit();
      }
    }
  }

  /** Assign some random caves to have superbats. */
  private void setCavesWithSuperbats() {
    int caveNum = caveLst.size();
    int batNum = (int) (caveNum * batPercent);
    Random random = new Random();
    random.setSeed(200);

    for (int i = 0; i < batNum; i++) {
      int index = random.nextInt(caveLst.size());
      int[] batPos = caveLst.get(index);
      Room bat = rooms[batPos[0]][batPos[1]];
      bat.setHasBat();
    }
  }

  /** Adventurer is in a room with superbats. */
  private void superbatCave() {
    Random random = new Random();
    random.setSeed(50);
    int num = random.nextInt(2);
    int playerPosX = currAdventurer.getAdventurerLocation()[0];
    int playerPosY = currAdventurer.getAdventurerLocation()[1];
    currAdventurer.setAdventurerLocation(playerPosX, playerPosY);

    // both bats and pits
    if (rooms[playerPosX][playerPosY].getIsPit()) {
      // 50% possibility to pits
      if (num == 0) {
        fallsInPit();
      } else {
        // 50% possibility to drop random cave
        movedBySuperbats();
      }
    } else {
      if (num == 1) {
        movedBySuperbats();
      }
    }
  }

  /** The superbats drop the adventurer to a random cave. */
  private void movedBySuperbats() {
    alert = "You have been caught by a superbat and dropped in a new room!";
    System.out.println(alert);

    Random random = new Random();
    int index = random.nextInt(caveLst.size());
    int playerPosX = caveLst.get(index)[0];
    int playerPosY = caveLst.get(index)[1];
    currAdventurer.setAdventurerLocation(playerPosX, playerPosY);

    if (rooms[playerPosX][playerPosY].getIsWumpus()) {
      eatenByTheWumpus();
    } else if (rooms[playerPosX][playerPosY].getHasBat()) {
      superbatCave();
    } else if (rooms[playerPosX][playerPosY].getIsPit()) {
      fallsInPit();
    }
  }

  /** The adventurer is eaten by the wumpus. */
  private void eatenByTheWumpus() {
    lives--;

    currAdventurer.setIsAdventurerDead();

    if (lives == 0) {
      isGameOver = true;
    }

    currAdventurer.setIsAdventurerDead();
    alert = "You are crunchy and taste good with ketchup.  You are dead. "
            + "Thanks for feeding the Wumpus!";
    System.out.println(alert);
  }

  /** The adventurer falls in a pit. */
  private void fallsInPit() {
    lives--;

    currAdventurer.setIsAdventurerDead();

    if (lives == 0) {
      isGameOver = true;
    }

    currAdventurer.setIsAdventurerDead();
    alert = "You fell into the bottomless pit and died!";
    System.out.println(alert);
  }

  @Override
  public void move(String direction) throws IllegalArgumentException {
    walkedRooms.clear();
    int playerPosX = currAdventurer.getAdventurerLocation()[0];
    int playerPosY = currAdventurer.getAdventurerLocation()[1];
    Room curr = rooms[playerPosX][playerPosY];
    walkedRooms.add(curr);
    int flag = 0;
    boolean getToWall = false;

    switch (direction) {
      case "N":
        if (curr.getRoomAbove() != null) {
          goUp();
        } else {
          getToWall = true;
          alert = "The adventurer has crossed the dungeon boundary! Please try again.";
          System.out.println(alert);
        }
        break;
      case "S":
        if (curr.getRoomBelow() != null) {
          goDown();
          flag = 1;
        } else {
          getToWall = true;
          alert = "The adventurer has crossed the dungeon boundary! Please re-input direction.";
          System.out.println(alert);
        }
        break;
      case "W":
        if (curr.getRoomOnTheLeft() != null) {
          goLeft();
          flag = 2;
        } else {
          getToWall = true;
          alert = "The adventurer has crossed the dungeon boundary! Please try again.";
          System.out.println(alert);
        }
        break;
      case "E":
        if (curr.getRoomOnTheRight() != null) {
          goRight();
          flag = 3;
        } else {
          getToWall = true;
          alert = "The adventurer has crossed the dungeon boundary! Please try again.";
          System.out.println(alert);
        }
        break;
      default:
        getToWall = true;
        alert = "An invalid direction has been entered";
        System.out.println("The direction string is not valid!");
        break;
    }
    playerPosX = currAdventurer.getAdventurerLocation()[0];
    playerPosY = currAdventurer.getAdventurerLocation()[1];
    curr = rooms[playerPosX][playerPosY];

    if (!getToWall) {
      movementHelper(curr, flag);
    }
  }

  @Override
  public boolean getAdventuresEnd() {
    return isGameOver;
  }

  @Override
  public boolean checkUnwinnable() {
    Queue<Room> queue = new LinkedList<>();
    Set<Room> visited = new HashSet<>();
    queue.offer(wumpus);

    while (!queue.isEmpty()) {
      Room curr = queue.poll();
      curr.setReachToWumpus();
      visited.add(curr);
      if (curr.getRoomAbove() != null
          && !visited.contains(curr.getRoomAbove())
          && (!curr.getRoomAbove().getIsPit()
              || curr.getRoomAbove().getIsPit() && curr.getRoomAbove().getHasBat())) {
        queue.offer(curr.getRoomAbove());
      }
      if (curr.getRoomBelow() != null
          && !visited.contains(curr.getRoomBelow())
          && (!curr.getRoomBelow().getIsPit()
              || curr.getRoomBelow().getIsPit() && curr.getRoomBelow().getHasBat())) {
        queue.offer(curr.getRoomBelow());
      }
      if (curr.getRoomOnTheLeft() != null
          && !visited.contains(curr.getRoomOnTheLeft())
          && (!curr.getRoomOnTheLeft().getIsPit()
              || curr.getRoomOnTheLeft().getIsPit() && curr.getRoomOnTheLeft().getHasBat())) {
        queue.offer(curr.getRoomOnTheLeft());
      }
      if (curr.getRoomOnTheRight() != null
          && !visited.contains(curr.getRoomOnTheRight())
          && (!curr.getRoomOnTheRight().getIsPit()
              || curr.getRoomOnTheRight().getIsPit() && curr.getRoomOnTheRight().getHasBat())) {
        queue.offer(curr.getRoomOnTheRight());
      }
    }

    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < cols; j++) {
        if (rooms[i][j].getIsRoom()
                && !rooms[i][j].getReachToWumpus()
                && i == adventurer1.getAdventurerStartLocation()[0]
                && j == adventurer1.getAdventurerStartLocation()[1]) {
          return true;
        }
      }
    }

    return false;
  }

  @Override
  public boolean checkShotSuccess() {
    return isShootSuccess;
  }

  @Override
  public Room getCurrentRoom() {
    return rooms[currAdventurer.getAdventurerLocation()[0]][
        currAdventurer.getAdventurerLocation()[1]];
  }

  @Override
  public void setAdventurerLocation(int x, int y) {
    currAdventurer.setAdventurerLocation(x, y);
  }

  @Override
  public void setAdventurerStartLocation(int x, int y) {
    currAdventurer.setAdventurerStartLocation(x, y);
  }

  @Override
  public String getShotResult() {
    if (isShootSuccess) {
      return "You shot to the wumpus!";
    }
    return "You missed the wumpus, and you have "
        + currAdventurer.getNumberOfArrows()
        + " arrows remains.";
  }

  /**
   * This method provides output for the user describing and setting values for the cave.
   *
   * @param curr The current cell.
   * @param flag The direction flag.
   */
  private void movementHelper(Room curr, int flag) {
    walkedRooms.add(curr);
    if (curr.getCloseToWumpus()) {
      alert = "You smell a Wumpus!  Nasty!";
      System.out.println(alert);
    } else if (curr.getCloseToPit()) {
      alert = "You smell something ghastly very near.";
      System.out.println(alert);
    } else if (curr.getIsWumpus()) {
      eatenByTheWumpus();
    } else if (curr.getHasBat()) {
      superbatCave();
      getAdventurerLocation();
    } else if (curr.getIsPit()) {
      fallsInPit();
    } else if (curr.getIsTunnel()) {
      moveInTunnel(flag);
    } else {
      alert = "You feel a draft.";
      System.out.println(alert);
      getAdventurerLocation();
    }
  }

  @Override
  public void fireArrow(String direction, int distance) throws IllegalArgumentException {
    int playerPosX = currAdventurer.getAdventurerLocation()[0];
    int playerPosY = currAdventurer.getAdventurerLocation()[1];

    if (!direction.equals("N")
        && !direction.equals("S")
        && !direction.equals("W")
        && !direction.equals("E")) {
      throw new IllegalArgumentException("The direction input is invalid!");
    }

    if (distance == 0) {
      throw new IllegalArgumentException("shooting distance cannot be zero!");
    }

    Room curr = rooms[playerPosX][playerPosY];

    if (direction.equals("N")) {
      curr = fireArrowByCaveNumber(distance, curr, 0);
    }

    if (direction.equals("S")) {
      curr = fireArrowByCaveNumber(distance, curr, 1);
    }

    if (direction.equals("W")) {
      curr = fireArrowByCaveNumber(distance, curr, 2);
    }

    if (direction.equals("E")) {
      curr = fireArrowByCaveNumber(distance, curr, 3);
    }

    if (curr == null) {
      alert = "You missed the wumpus!";
      System.out.println(alert);
    } else if (curr.getIsWumpus()) {
      isGameOver = true;
      alert = "Player " + getAdventurerRound() + "wins!";
      isShootSuccess = true;
      currAdventurer.setNumberOfArrows(currAdventurer.getNumberOfArrows() - 1);
      alert = "You hear a howl and thud. You killed the wumpus!";
      System.out.println(alert);
    } else {
      alert = "You missed the wumpus!";
      System.out.println(alert);
      currAdventurer.setNumberOfArrows(currAdventurer.getNumberOfArrows() - 1);
      if (currAdventurer.getNumberOfArrows() <= 0) {
        lives--;
        currAdventurer.setIsAdventurerDead();
        if (lives == 0) {
          isGameOver = true;
        }
        alert = "You are out of arrows!";
        System.out.println(alert);
      }
    }
  }

  /**
   * Given a distance and direction, fire an arrow.
   *
   * @param distance Number of caves.
   * @param currentRoom The current cell.
   * @param flag The direction flag.
   * @return The target cell to shoot.
   */
  private Room fireArrowByCaveNumber(int distance, Room currentRoom, int flag) {
    for (int i = 0; i < distance; i++) {
      Room prev = currentRoom;

      if (flag == 1) {
        if (currentRoom.getRoomBelow() == null) {
          currAdventurer.setNumberOfArrows(currAdventurer.getNumberOfArrows() - 1);
          return null;
        }

        currentRoom = currentRoom.getRoomBelow();
        flag = getFlag(prev, currentRoom);
      } else if (flag == 0) {
        if (currentRoom.getRoomAbove() == null) {
          currAdventurer.setNumberOfArrows(currAdventurer.getNumberOfArrows() - 1);
          return null;
        }

        currentRoom = currentRoom.getRoomAbove();
        flag = getFlag(prev, currentRoom);
      } else if (flag == 2) {
        if (currentRoom.getRoomOnTheLeft() == null) {
          currAdventurer.setNumberOfArrows(currAdventurer.getNumberOfArrows() - 1);
          return null;
        }

        currentRoom = currentRoom.getRoomOnTheLeft();
        flag = getFlag(prev, currentRoom);
      } else if (flag == 3) {
        if (currentRoom.getRoomOnTheRight() == null) {
          currAdventurer.setNumberOfArrows(currAdventurer.getNumberOfArrows() - 1);
          return null;
        }

        currentRoom = currentRoom.getRoomOnTheRight();
        flag = getFlag(prev, currentRoom);
      }
    }

    return currentRoom;
  }

  /**
   * Get the direction flag by moving from the last cell to the current cell.
   *
   * @param prev The cell in the last step.
   * @param curr The current cell.
   * @return The flag indicates direction.
   */
  private int getFlag(Room prev, Room curr) {
    int flag = 0;
    if (curr.getRoomAbove() == prev) {
      flag = 1;
    }
    if (curr.getRoomBelow() == prev) {
      flag = 0;
    }
    if (curr.getRoomOnTheLeft() == prev) {
      flag = 3;
    }
    if (curr.getRoomOnTheRight() == prev) {
      flag = 2;
    }
    return flag;
  }

  /**
   * The Adventurer move in tunnel and update the Adventurer's location when the Adventurer get
   * out of the tunnel.
   */
  private void moveInTunnel(int flag) {
    int playerPosX = currAdventurer.getAdventurerLocation()[0];
    int playerPosY = currAdventurer.getAdventurerLocation()[1];
    while (rooms[playerPosX][playerPosY].getIsTunnel()) {
      if (rooms[playerPosX][playerPosY].getRoomBelow() != null && flag != 0) {
        goDown();
        flag = 1;
      } else if (rooms[playerPosX][playerPosY].getRoomAbove() != null && flag != 1) {
        goUp();
        flag = 0;
      } else if (rooms[playerPosX][playerPosY].getRoomOnTheLeft() != null && flag != 3) {
        goLeft();
        flag = 2;
      } else if (rooms[playerPosX][playerPosY].getRoomOnTheRight() != null && flag != 2) {
        goRight();
        flag = 3;
      } else {
        break;
      }
      playerPosX = currAdventurer.getAdventurerLocation()[0];
      playerPosY = currAdventurer.getAdventurerLocation()[1];
      walkedRooms.add(rooms[playerPosX][playerPosY]);
    }
    movementHelper(rooms[playerPosX][playerPosY], flag);
  }
}
