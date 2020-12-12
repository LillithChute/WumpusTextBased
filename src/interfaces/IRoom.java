package interfaces;

import model.Room;

/** The operations allowable on a cave.*/
public interface IRoom {
  /**
   * Set the pointer to the next room.
   *
   * @param nextRoom      The cave next to the current cave.
   * @param directionSide The direction pointer to the next cave.
   */
  void setNextRoom(Room nextRoom, String directionSide);

  /**
   * Get the cave on the left side of the current cave.
   *
   * @return The cave on the left side of current.
   */
  Room getRoomOnTheLeft();

  /**
   * Get the cave on the right side of the current cave.
   *
   * @return The cave on the right side of current.
   */
  Room getRoomOnTheRight();

  /**
   * Get the cave on the up side of the current cave.
   *
   * @return The cave on the up side of current.
   */
  Room getRoomAbove();

  /**
   * Get the cave on the down side of the current cave.
   *
   * @return The cave on the down side of current.
   */
  Room getRoomBelow();

  /**
   * Set the room status for the cave.
   *
   * @param isRoom Whether the cave is a room/cave.
   */
  void setIsCave(boolean isRoom);

  /**
   * Set the tunnel status for the cave.
   *
   * @param isTunnel Whether the cave is a tunnel or not.
   */
  void setIsTunnel(boolean isTunnel);

  /**
   * Set the wumpus status as true for the cave.
   */
  void setIsWumpus();

  /**
   * Get the wumpus status for the cave.
   */
  boolean getIsWumpus();

  /**
   * Set the close to wumpus status as true for the cave.
   */
  void setCloseToWumpus();

  /**
   * Get if the cave is close to wumpus.
   *
   * @return True/False.
   */
  boolean getCloseToWumpus();

  /**
   * Set the cave has bats as true.
   */
  void setHasBat();

  /**
   * Get if the cave has bats.
   */
  boolean getHasBat();

  /**
   * Set the cave as a pit.
   */
  void setIsPit();

  /**
   * Set the cave if it is close to a pit.
   */
  void setCloseToPit();

  /**
   * Get if the cave is a pit.
   *
   * @return True/False.
   */
  boolean getIsPit();

  /**
   * Get if the cave is close to a pit.
   *
   * @return True/False.
   */
  boolean getCloseToPit();

  /**
   * Get whether the cave is a room.
   *
   * @return True/False.
   */
  boolean getIsRoom();

  /**
   * Get whether the cave is a tunnel.
   *
   * @return True/False.
   */
  boolean getIsTunnel();

  /**
   * Set if the cave could reach the wumpus.
   */
  void setCouldReachWumpus();

  /**
   * Get if the cave could reach the wumpus.
   *
   * @return True/False.
   */
  boolean getCouldReachWumpus();
}
