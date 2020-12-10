package model;

/**
 * This class holds the information relevant to the adventurer such as location, whether they
 * are alive, number of arrows, location, and so on.
 * */
public class Adventurer {
  private int adventurerLocX;
  private int adventurerLocY;
  private final int[] start;
  private int arrows;
  private boolean isDead;

  /**
   * Constructor.
   */
  public Adventurer() {
    adventurerLocX = 0;
    adventurerLocY = 0;
    start = new int[2];
    arrows = 0;
    isDead = false;
  }

  /**
   * Set the starting location for Adventurer.
   *
   * @param x Horizontal position.
   * @param y Vertical position.
   */
  public void setAdventurerStartLocation(int x, int y) {
    start[0] = x;
    start[1] = y;
    setAdventurerLocation(x, y);
  }

  /**
   * Get the Adventurer's start location.
   *
   * @return The Adventurer start position.
   */
  public int[] getAdventurerStartLocation() {
    return start;
  }

  /**
   * Place the adventurer in a specific location.
   *
   * @param x Horizontal location.
   * @param y Vertical location.
   */
  public void setAdventurerLocation(int x, int y) {
    adventurerLocX = x;
    adventurerLocY = y;
  }

  /**
   * Get the Adventurer's current location.
   *
   * @return The array of Adventurer's current location.
   */
  public int[] getAdventurerLocation() {
    int[] location = new int[2];
    location[0] = adventurerLocX;
    location[1] = adventurerLocY;
    return location;
  }

  /**
   * Set the adventurer's status to dead.
   */
  public void setIsAdventurerDead() {
    isDead = true;
  }

  /**
   * Get whether the adventurer is dead.
   *
   * @return whether the adventurer is dead.
   */
  public boolean getIsAdventurerDead() {
    return isDead;
  }

  /**
   * Assign the number of remaining arrows for the Adventurer.
   *
   * @param arrows Number of remaining arrows.
   */
  public void setNumberOfArrows(int arrows) {
    this.arrows = arrows;
  }

  /**
   * Get the number of remaining arrows for the Adventurer.
   *
   * @return The number of remaining arrows.
   */
  public int getNumberOfArrows() {
    return arrows;
  }
}
