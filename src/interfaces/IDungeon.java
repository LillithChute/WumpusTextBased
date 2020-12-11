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
   * @param direction The direction that adventurer wish to shoot.
   * @param distance  The shooting distance by the number of caves.
   */
  void fireArrow(String direction, int distance) throws IllegalArgumentException;

  /**
   * The adventurer's move inside the dungeon.
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
   * Get if the adventurer successfully shot the wumpus.
   *
   * @return True/False.
   */
  boolean checkShotSuccess();

  /**
   * Get the current cell of that the adventurer is located.
   *
   * @return The current cell.
   */
  Room getCurrentRoom();

  /**
   * Set the adventurer's location to a specific cell for testing.
   *
   * @param x The adventurer's horizontal location.
   * @param y The adventurer's vertical location.
   */
  void setAdventurerLocation(int x, int y);

  /**
   * Reset the adventurer's starting point for testing.
   *
   * @param x The horizontal location of the starting point.
   * @param y The vertical location of the starting point. //
   */
  void setAdventurerStartLocation(int x, int y);

  /**
   * Get the shot result if the adventurer shot at the wumpus and how many arrows are left.
   *
   * @return The result of shoot is successful or not.
   */
  String getShotResult();

  /**
   * Get the adventurer indicator of the current round.
   *
   * @return The adventurer indicator.
   */
  int getAdventurerRound();
}
