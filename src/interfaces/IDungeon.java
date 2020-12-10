package interfaces;

import model.Room;

public interface IDungeon {
  /**
   * Get the current location of the player.
   *
   * @return The string of player's location.
   */
  String getAdventurerLocation();

  /**
   * Shoot towards to direction with a specified distance by the number of caves.
   *
   * @param direction The direction that player wish to shoot.
   * @param distance  The shooting distance by the number of caves.
   */
  void shoot(String direction, int distance) throws IllegalArgumentException;

  /**
   * The player's move inside the maze.
   *
   * @param direction Move towards to a specific direction.
   */
  void move(String direction);

  /**
   * Return if the game is ended.
   *
   * @return True/False.
   */
  boolean getAdventuresEnd();

  /**
   * Check if the game is unwinnable and it's impossible to kill the wumpus from a safe cave.
   *
   * @return True/False.
   */
  boolean checkUnwinnable();

  /**
   * Get if the player successfully shot the wumpus.
   *
   * @return True/False.
   */
  boolean checkShotSuccess();

  /**
   * Get the current cell of that the player is located.
   *
   * @return The current cell.
   */
  Room getCurrentRoom();

  /**
   * Set the player's location to a specific cell for testing.
   *
   * @param x The player's horizontal location.
   * @param y The player's vertical location.
   */
  void setAdventurerLocation(int x, int y);

  /**
   * Reset the player's starting point for testing.
   *
   * @param x The horizontal location of the starting point.
   * @param y The vertical location of the starting point. //
   */
  void setAdventurerStartLocation(int x, int y);

  /**
   * Get the shot result if the player shot at the wumpus and how many arrows are left.
   *
   * @return The result of shoot is successful or not.
   */
  String getShotResult();

  /**
   * Return the message.
   *
   * @return The message of the dungeon.
   */
  String getMessage();

  /**
   * Get the player indicator of the current round.
   *
   * @return The player indicator.
   */
  int getAdventurerRound();
}
