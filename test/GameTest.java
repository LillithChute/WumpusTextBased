import org.junit.Before;
import org.junit.Test;

import interfaces.IDungeon;
import model.Game;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class GameTest {
  private IDungeon dungeonCrawlOne;
  private IDungeon dungeonCrawlTwo;
  private IDungeon dungeonCrawlThree;
  private IDungeon dungeonCrawlFour;

  /** Create objects for the following tests. */
  @Before
  public void setup() {
    dungeonCrawlOne = new Game(3, 4, 6, true, false,
            0.2, 0.3, 3, 1);
    dungeonCrawlTwo = new Game(3, 4, 13, true, true,
            0.2, 0.2, 3, 1);
    dungeonCrawlThree = new Game(3, 4, 3, false, false,
            0.2, 0.3, 3, 1);
    dungeonCrawlFour = new Game(3, 4, 6, false, true,
            0.2, 0.2, 3, 1);
  }

  @Test
  public void testMazeConstructorIsValid() {
    assertEquals(
        "The dungeon is 3 * 4, and it is a non-wrapping perfect dungeon. The start "
            + "point of player is (2, 1). The saved walls are numbered by: 9 2 11 13 15 16 ",
        dungeonCrawlOne.toString());
    assertEquals(
        "The dungeon is 3 * 4, and it is a wrapping perfect dungeon. The start point of "
            + "player is (2, 0). The saved walls are numbered by: 0 19 2 3 4 5 11 13 16 17 "
            + "18 21 23 ",
        dungeonCrawlTwo.toString());
    assertEquals(
        "The dungeon is 3 * 4, and it is a non-wrapping room dungeon. The start point of "
            + "player is (2, 2). The saved walls are numbered by: 11 13 16 ",
        dungeonCrawlThree.toString());
    assertEquals(
        "The dungeon is 3 * 4, and it is a wrapping room dungeon. The start point of "
            + "player is (0, 2). The saved walls are numbered by: 19 2 11 13 21 23 ",
        dungeonCrawlFour.toString());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMazeConstructorIsInvalid1() {
    dungeonCrawlOne = new Game(-3, 4, 11, true, false,
            0.2, 0.2, 3, 1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMazeConstructorInvalid2() {
    dungeonCrawlOne = new Game(3, -4, 11, true, false,
            0.2, 0.2, 3, 1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMazeConstructorInvalid3() {
    dungeonCrawlThree = new Game(3, 4, -3, false, false, 0.2,
            0.2, 3, 1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMazeConstructorInvalid4() {
    dungeonCrawlThree = new Game(3, 4, 7, false, false, 0.2,
            0.2, 3, 1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMazeConstructorInvalid5() {
    dungeonCrawlThree = new Game(3, 4, 4, false, false, -0.2,
            0.2, 3, 1);
    dungeonCrawlThree = new Game(3, 4, 4, false, false, 0.2,
            -0.2, 3, 1);
    dungeonCrawlThree = new Game(3, 4, 4, false, false, 0.2,
            0.2, -3, 1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testEmptyShoot() {
    dungeonCrawlOne.fireArrow("", 1);
    dungeonCrawlFour.fireArrow("", 1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidShoot() {
    dungeonCrawlOne.fireArrow("North", 1);
    dungeonCrawlFour.fireArrow("abc", 1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testShootZero() {
    dungeonCrawlOne.fireArrow("S", 0);
    dungeonCrawlFour.fireArrow("N", 0);
  }

  @Test
  public void testMoveNorthAndSouth() {
    dungeonCrawlOne.move("N");
    assertEquals(
        "You are in cave (1, 1). Tunnels lead to the E, W, N, S",
            dungeonCrawlOne.getAdventurerLocation());
    dungeonCrawlOne.move("S");
    assertEquals(
        "You are in cave (2, 1). Tunnels lead to the E, W, N",
            dungeonCrawlOne.getAdventurerLocation());
  }

  @Test
  public void testMoveEastAndWest() {
    dungeonCrawlOne.move("E");
    assertEquals("You are in cave (2, 3). Tunnels lead to the W",
            dungeonCrawlOne.getAdventurerLocation());
    dungeonCrawlOne.move("W");
    assertEquals(
        "You are in cave (2, 1). Tunnels lead to the E, W, N",
            dungeonCrawlOne.getAdventurerLocation());
    dungeonCrawlFour.move("E");
    assertEquals(
        "You are in cave (0, 3). Tunnels lead to the E, W, N",
            dungeonCrawlFour.getAdventurerLocation());
    dungeonCrawlFour.move("W");
    assertEquals(
        "You are in cave (0, 2). Tunnels lead to the E, N, S",
            dungeonCrawlFour.getAdventurerLocation());
  }

  @Test
  public void testMoveToWall() {
    dungeonCrawlOne.move("N");
    dungeonCrawlOne.move("W");
    String beforeMoveToWall = dungeonCrawlOne.getAdventurerLocation();
    dungeonCrawlOne.move("S");
    String afterMoveToWall = dungeonCrawlOne.getAdventurerLocation();
    assertEquals(beforeMoveToWall, afterMoveToWall);

    String beforeMoveToWall4 = dungeonCrawlFour.getAdventurerLocation();
    dungeonCrawlFour.move("W");
    String afterMoveToWall4 = dungeonCrawlFour.getAdventurerLocation();
    assertEquals(beforeMoveToWall4, afterMoveToWall4);
  }

  @Test
  public void testMoveTunnel() {
    dungeonCrawlOne.move("N");
    dungeonCrawlOne.move("E");
    assertEquals("You are in cave (0, 3). Tunnels lead to the S",
            dungeonCrawlOne.getAdventurerLocation());
    dungeonCrawlFour.move("E");
    dungeonCrawlFour.move("E");
    dungeonCrawlFour.move("E");
    assertEquals(
        "You are in cave (1, 1). Tunnels lead to the E, W, N",
            dungeonCrawlFour.getAdventurerLocation());
  }

  @Test
  public void testMoveTunnel2() {
    dungeonCrawlOne.move("E");
    assertEquals("You are in cave (2, 3). Tunnels lead to the W",
            dungeonCrawlOne.getAdventurerLocation());
    dungeonCrawlFour.move("E");
    dungeonCrawlFour.move("N");
    assertEquals(
        "You are in cave (2, 0). Tunnels lead to the E, W, N, S",
            dungeonCrawlFour.getAdventurerLocation());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testShootDistanceZero() {
    dungeonCrawlOne.fireArrow("N", 0);
    dungeonCrawlTwo.fireArrow("W", 0);
    dungeonCrawlThree.fireArrow("N", 0);
    dungeonCrawlFour.fireArrow("E", 0);
    assertFalse(dungeonCrawlFour.checkShotSuccess());
  }

  @Test
  public void testShootDistanceWrong() {
    dungeonCrawlFour.fireArrow("N", 3);
    assertFalse(dungeonCrawlFour.checkShotSuccess());
    dungeonCrawlFour.setAdventurerLocation(0, 0);
    dungeonCrawlFour.fireArrow("S", 1);
    assertFalse(dungeonCrawlFour.checkShotSuccess());
    dungeonCrawlFour.setAdventurerLocation(0, 0);
    dungeonCrawlFour.fireArrow("S", 3);
    assertFalse(dungeonCrawlFour.checkShotSuccess());
  }

  @Test
  public void testShootDistanceOverWumpus() {
    dungeonCrawlOne.move("N");
    dungeonCrawlOne.fireArrow("E", 3);
    assertFalse(dungeonCrawlOne.checkShotSuccess());
    dungeonCrawlOne.fireArrow("E", 1);
    assertTrue(dungeonCrawlOne.checkShotSuccess());
  }

  @Test
  public void testShootThroughTunnel() {
    dungeonCrawlOne.move("N");
    dungeonCrawlOne.fireArrow("E", 2);
    assertFalse(dungeonCrawlOne.checkShotSuccess());
    dungeonCrawlOne.fireArrow("E", 1);
    assertTrue(dungeonCrawlOne.checkShotSuccess());
  }

  @Test
  public void testShootToWall() {
    dungeonCrawlFour.fireArrow("W", 1);
    assertFalse(dungeonCrawlFour.checkShotSuccess());
    dungeonCrawlOne.fireArrow("E", 2);
    assertFalse(dungeonCrawlFour.checkShotSuccess());
  }

  @Test
  public void testOutOfArrows() {
    dungeonCrawlOne.fireArrow("N", 1);
    assertFalse(dungeonCrawlOne.checkShotSuccess());
    dungeonCrawlOne.fireArrow("N", 2);
    assertFalse(dungeonCrawlOne.checkShotSuccess());
    dungeonCrawlOne.fireArrow("W", 1);
    assertFalse(dungeonCrawlOne.checkShotSuccess());
    dungeonCrawlOne.fireArrow("E", 1);
    assertFalse(dungeonCrawlOne.checkShotSuccess());
  }

  @Test
  public void testShootSuccess() {
    dungeonCrawlFour.setAdventurerLocation(0, 0);
    dungeonCrawlFour.fireArrow("S", 2);
    assertTrue(dungeonCrawlFour.checkShotSuccess());
    assertTrue(dungeonCrawlFour.getAdventuresEnd());
  }

  @Test
  public void testPlayerMoveToBat() {
    dungeonCrawlOne.setAdventurerStartLocation(0, 2);
    assertNotEquals(
        dungeonCrawlOne.getAdventurerLocation(), "You are in cave (0, 2). " + "Tunnels lead to the W ");
  }

  @Test
  public void testMoveToPit() {
    dungeonCrawlOne.move("N");
    dungeonCrawlOne.move("N");
    assertTrue(dungeonCrawlOne.getCurrentRoom().getIsPit());
    assertTrue(dungeonCrawlOne.getAdventuresEnd());
    assertTrue(dungeonCrawlFour.getCurrentRoom().getRoomBelow().getIsPit());
  }

  @Test
  public void testMoveCloseToPit() {
    dungeonCrawlOne.move("N");
    assertTrue(dungeonCrawlOne.getCurrentRoom().getCloseToPit());
    dungeonCrawlOne.move("S");
    dungeonCrawlOne.move("W");
    assertFalse(dungeonCrawlOne.getCurrentRoom().getCloseToPit());
    dungeonCrawlFour.move("N");
    assertTrue(dungeonCrawlFour.getCurrentRoom().getCloseToPit());
    dungeonCrawlFour.move("W");
    assertFalse(dungeonCrawlFour.getCurrentRoom().getCloseToPit());
  }

  @Test
  public void testMoveCloseToPitByTunnel() {
    dungeonCrawlFour.setAdventurerStartLocation(1, 0);
    assertTrue(dungeonCrawlFour.getCurrentRoom().getCloseToPit());
  }

  @Test
  public void testMoveCloseToWumpus() {
    // Smell a wumpus through a two cells tunnel.
    dungeonCrawlOne.move("N");
    assertTrue(dungeonCrawlOne.getCurrentRoom().getCloseToWumpus());
    dungeonCrawlOne.move("W");
    assertFalse(dungeonCrawlOne.getCurrentRoom().getCloseToWumpus());
    // Smell a wumpus through a one cell tunnel.
    dungeonCrawlFour.move("N");
    assertTrue(dungeonCrawlFour.getCurrentRoom().getCloseToWumpus());
    dungeonCrawlFour.move("S");
    assertFalse(dungeonCrawlFour.getCurrentRoom().getCloseToWumpus());
  }

  @Test
  public void testEatenByWumpus() {
    dungeonCrawlOne.move("N");
    dungeonCrawlOne.move("E");
    assertTrue(dungeonCrawlOne.getAdventuresEnd());
    dungeonCrawlFour.move("E");
    dungeonCrawlFour.move("E");
    dungeonCrawlFour.move("S");
    dungeonCrawlFour.move("S");
    assertTrue(dungeonCrawlFour.getAdventuresEnd());
  }

  @Test
  public void testKillWumpusWithTunnel() {
    dungeonCrawlOne.move("N");
    dungeonCrawlOne.fireArrow("E", 1);
    assertTrue(dungeonCrawlOne.checkShotSuccess());
  }

  @Test
  public void testKillWumpusWithoutTunnel() {
    dungeonCrawlFour.move("N");
    dungeonCrawlFour.fireArrow("W", 1);
    assertTrue(dungeonCrawlFour.checkShotSuccess());
  }

  @Test
  public void testUnWinnable() {
    dungeonCrawlOne.setAdventurerStartLocation(0, 0);
    assertTrue(dungeonCrawlOne.checkUnwinnable());
    assertFalse(dungeonCrawlFour.checkUnwinnable());
  }
}
