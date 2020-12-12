package model;

/**
 * This class holds the information relevant to the adventurer such as location, whether they
 * are alive, number of arrows, location, and so on.
 * */
public class Adventurer implements interfaces.IAdventurer {
  private int adventurerLocX;
  private int adventurerLocY;
  private final int[] start;
  private int arrows;

  /**
   * Constructor.
   */
  public Adventurer() {
    adventurerLocX = 0;
    adventurerLocY = 0;
    start = new int[2];
    arrows = 0;
  }

  @Override
  public void setAdventurerStartLocation(int x, int y) {
    start[0] = x;
    start[1] = y;
    setAdventurerLocation(x, y);
  }

  @Override
  public int[] getAdventurerStartLocation() {
    return start;
  }

  @Override
  public void setAdventurerLocation(int x, int y) {
    adventurerLocX = x;
    adventurerLocY = y;
  }

  @Override
  public int[] getAdventurerLocation() {
    int[] location = new int[2];
    location[0] = adventurerLocX;
    location[1] = adventurerLocY;
    return location;
  }

  @Override
  public void setIsAdventurerDead() {
    // Working through something here.
  }

  @Override
  public void setNumberOfArrows(int arrows) {
    this.arrows = arrows;
  }

  @Override
  public int getNumberOfArrows() {
    return arrows;
  }
}
