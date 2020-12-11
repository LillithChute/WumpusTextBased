package model;

import interfaces.IRoom;
import java.util.HashMap;
import java.util.Map;

/**
 * This holds all the information pertaining to a dungeon room such as if it contains the Wumpus,
 * superbats, pit, or the adventurer.  It also retains information about it's relationship to
 * other rooms.
 *
 * */
public class Room implements IRoom {
  //string is the direction, cave is the next cave.
  private final Map<String, Room> roomLayout;
  private boolean isTunnel;
  private boolean isRoom;
  private boolean closeToWumpus;
  private boolean isWumpus;
  private boolean isPit;
  private boolean closeToPit;
  private boolean hasBat;
  private boolean couldHitWumpus;

  /**
   * Constructor.
   *
   */
  public Room() {
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

  }

  @Override
  public void setNextRoom(Room nextRoom, String directionSide) {
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

  @Override
  public Room getRoomOnTheLeft() {
    return roomLayout.get("left");
  }

  @Override
  public Room getRoomOnTheRight() {
    return roomLayout.get("right");
  }

  @Override
  public Room getRoomAbove() {
    return roomLayout.get("up");
  }

  @Override
  public Room getRoomBelow() {
    return roomLayout.get("down");
  }

  @Override
  public void setIsCave(boolean isRoom) {
    this.isRoom = isRoom;
  }

  @Override
  public void setIsTunnel(boolean isTunnel) {
    this.isTunnel = isTunnel;
  }

  @Override
  public void setIsWumpus() {
    this.isWumpus = true;
  }

  @Override
  public boolean getIsWumpus() {
    return isWumpus;
  }

  @Override
  public void setCloseToWumpus() {
    this.closeToWumpus = true;
  }

  @Override
  public boolean getCloseToWumpus() {
    return closeToWumpus;
  }

  @Override
  public void setHasBat() {
    hasBat = true;
  }

  @Override
  public boolean getHasBat() {
    return hasBat;
  }

  @Override
  public void setIsPit() {
    isPit = true;
  }

  @Override
  public void setCloseToPit() {
    closeToPit = true;
  }

  @Override
  public boolean getIsPit() {
    return isPit;
  }

  @Override
  public boolean getCloseToPit() {
    return closeToPit;
  }

  @Override
  public boolean getIsRoom() {
    return isRoom;
  }

  @Override
  public boolean getIsTunnel() {
    return isTunnel;
  }

  @Override
  public void setReachToWumpus() {
    couldHitWumpus = true;
  }

  @Override
  public boolean getReachToWumpus() {
    return couldHitWumpus;
  }
}
