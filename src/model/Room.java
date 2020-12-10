package model;

import java.util.HashMap;
import java.util.Map;

/**
 * This holds all the information pertaining to a dungeon room such as if it contains the Wumpus,
 * superbats, pit, or the adventurer.  It also retains information about it's relationship to
 * other rooms.
 *
 * */
public class Room {
  //string is the direction, cell is the next cell.
  private final Map<String, Room> roomLayout;
  private boolean isTunnel;
  private boolean isRoom;
  private boolean closeToWumpus;
  private boolean isWumpus;
  private boolean isPit;
  private boolean closeToPit;
  private boolean hasBat;
  private boolean couldHitWumpus;
  private final int x;
  private final int y;

  /**
   * Constructor.
   *
   * @param x The cell's horizontal position in the maze.
   * @param y The cell's vertical position in the maze.
   */
  public Room(int x, int y) {
    roomLayout = new HashMap<>();
    roomLayout.put("left", null);
    roomLayout.put("right", null);
    roomLayout.put("up", null);
    roomLayout.put("down", null);

    closeToWumpus = false;
    isWumpus = false;
    isPit = false;
    closeToPit = false;
    hasBat = false;
    couldHitWumpus = false;

    this.x = x;
    this.y = y;
  }

  /**
   * Set the pointer to the next cell.
   *
   * @param nextRoom      The cell next to the current cell.
   * @param directionSide The direction pointer to the next cell.
   */
  public void setNextCell(Room nextRoom, String directionSide) {
    switch (directionSide) {
      case "left":
        roomLayout.put("left", nextRoom);
        break;
      case "right":
        roomLayout.put("right", nextRoom);
        break;
      case "up":
        roomLayout.put("up", nextRoom);
        break;
      case "down":
        roomLayout.put("down", nextRoom);
        break;
      default:
    }
  }

  /**
   * Get the cell on the left side of the current cell.
   *
   * @return The cell on the left side of current.
   */
  public Room getLeftCell() {
    return roomLayout.get("left");
  }

  /**
   * Get the cell on the right side of the current cell.
   *
   * @return The cell on the right side of current.
   */
  public Room getRightCell() {
    return roomLayout.get("right");
  }

  /**
   * Get the cell on the up side of the current cell.
   *
   * @return The cell on the up side of current.
   */
  public Room getUpCell() {
    return roomLayout.get("up");
  }

  /**
   * Get the cell on the down side of the current cell.
   *
   * @return The cell on the down side of current.
   */
  public Room getDownCell() {
    return roomLayout.get("down");
  }

  /**
   * Set the room status for the cell.
   *
   * @param isRoom Whether the cell is a room/cave.
   */
  public void setIsRoom(boolean isRoom) {
    this.isRoom = isRoom;
  }

  /**
   * Set the tunnel status for the cell.
   *
   * @param isTunnel Whether the cell is a tunnel or not.
   */
  public void setIsTunnel(boolean isTunnel) {
    this.isTunnel = isTunnel;
  }

  /**
   * Set the wumpus status as true for the cell.
   */
  public void setIsWumpus() {
    this.isWumpus = true;
  }

  /**
   * Get the wumpus status for the cell.
   */
  public boolean getIsWumpus() {
    return isWumpus;
  }

  /**
   * Set the close to wumpus status as true for the cell.
   */
  public void setCloseToWumpus() {
    this.closeToWumpus = true;
  }

  /**
   * Get if the cell is close to wumpus.
   *
   * @return True/False.
   */
  public boolean getCloseToWumpus() {
    return closeToWumpus;
  }

  /**
   * Set the cell has bats as true.
   */
  public void setHasBat() {
    hasBat = true;
  }

  /**
   * Get if the cell has bats.
   */
  public boolean getHasBat() {
    return hasBat;
  }

  /**
   * Set the cell as a pit.
   */
  public void setIsPit() {
    isPit = true;
  }

  /**
   * Set the cell if it is close to a pit.
   */
  public void setCloseToPit() {
    closeToPit = true;
  }

  /**
   * Get if the cell is a pit.
   *
   * @return True/False.
   */
  public boolean getIsPit() {
    return isPit;
  }

  /**
   * Get if the cell is close to a pit.
   *
   * @return True/False.
   */
  public boolean getCloseToPit() {
    return closeToPit;
  }

  /**
   * Get whether the cell is a room.
   *
   * @return True/False.
   */
  public boolean getIsRoom() {
    return isRoom;
  }

  /**
   * Get whether the cell is a tunnel.
   *
   * @return True/False.
   */
  public boolean getIsTunnel() {
    return isTunnel;
  }

  /**
   * Get the number of doors for the cell.
   *
   * @return Number of doors.
   */
  public int getRoomDoors() {
    int count = 0;
    if (getUpCell() != null) {
      count++;
    }
    if (getDownCell() != null) {
      count++;
    }
    if (getLeftCell() != null) {
      count++;
    }
    if (getRightCell() != null) {
      count++;
    }
    return count;
  }

  /**
   * Get the position of the current cell.
   *
   * @return The position array.
   */
  public int[] getCellPos() {
    return new int[]{x, y};
  }

  /**
   * Get the cell type string to mark which image is corresponding to the cell.
   *
   * @return The string of cell indicator.
   */
  public String getCurrCellStatus() {
    if (this.isTunnel) {
      if (getLeftCell() != null && getRightCell() != null) {
        return "is tunnel horizontal";
      }
      if (getLeftCell() != null && getUpCell() != null) {
        return "is tunnel 1";
      }
      if (getLeftCell() != null && getDownCell() != null) {
        return "is tunnel 2";
      }
      if (getUpCell() != null && getDownCell() != null) {
        return "is tunnel vertical";
      }
      if (getDownCell() != null && getRightCell() != null) {
        return "is tunnel 3";
      }
      if (getUpCell() != null && getRightCell() != null) {
        return "is tunnel 4";
      }
    } else if (this.isRoom) {
      int roomNum = getRoomDoors();
      if (roomNum == 1) {
        if (getUpCell() != null) {
          return "is room 1 up";
        }
        if (getDownCell() != null) {
          return "is room 1 down";
        }
        if (getLeftCell() != null) {
          return "is room 1 left";
        }
        if (getRightCell() != null) {
          return "is room 1 right";
        }
      } else if (roomNum == 3) {
        if (getUpCell() != null && getLeftCell() != null && getDownCell() != null) {
          return "is room 3 1";
        }
        if (getUpCell() != null && getLeftCell() != null && getRightCell() != null) {
          return "is room 3 2";
        }
        if (getUpCell() != null && getRightCell() != null && getDownCell() != null) {
          return "is room 3 3";
        }
        if (getDownCell() != null && getLeftCell() != null && getRightCell() != null) {
          return "is room 3 4";
        }
      } else {
        return "is room 4";
      }
    }
    return "";
  }

  /**
   * Set is the cell could reach to wumpus.
   */
  public void setReachToWumpus() {
    couldHitWumpus = true;
  }

  /**
   * Get if the cell could reach to wumpus or not.
   *
   * @return True/False.
   */
  public boolean getReachToWumpus() {
    return couldHitWumpus;
  }
}
