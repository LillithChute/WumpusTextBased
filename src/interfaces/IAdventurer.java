package interfaces;

public interface IAdventurer {
  /**
   * Set the starting location for Adventurer.
   *
   * @param x Horizontal position.
   * @param y Vertical position.
   */
  void setAdventurerStartLocation(int x, int y);

  /**
   * Get the Adventurer's start location.
   *
   * @return The Adventurer start position.
   */
  int[] getAdventurerStartLocation();

  /**
   * Place the adventurer in a specific location.
   *
   * @param x Horizontal location.
   * @param y Vertical location.
   */
  void setAdventurerLocation(int x, int y);

  /**
   * Get the Adventurer's current location.
   *
   * @return The array of Adventurer's current location.
   */
  int[] getAdventurerLocation();

  /**
   * Set the adventurer's status to dead.
   */
  void setIsAdventurerDead();

  /**
   * Assign the number of remaining arrows for the Adventurer.
   *
   * @param arrows Number of remaining arrows.
   */
  void setNumberOfArrows(int arrows);

  /**
   * Get the number of remaining arrows for the Adventurer.
   *
   * @return The number of remaining arrows.
   */
  int getNumberOfArrows();
}
