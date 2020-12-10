import org.junit.Before;
import org.junit.Test;

import interfaces.IDungeon;
import model.Game;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class GameTest {
  private IDungeon game1;
  private IDungeon game2;
  private IDungeon game3;
  private IDungeon game4;
  private IDungeon game5;

  /**
   * Create objects for the following tests.
   */
  @Before
  public void setup() {
    game1 = new Game(3, 4, 6, true, false,
            0.2, 0.3, 3, 1);
    game2 = new Game(3, 4, 13, true, true,
            0.2, 0.2, 3, 1);
    game3 = new Game(3, 4, 3, false, false,
            0.2, 0.3, 3, 1);
    game4 = new Game(3, 4, 6, false, true,
            0.2, 0.2, 3, 1);
    game5 = new Game(3, 4, 5, false, false,
            0.1, 0.1, 3, 2);
  }

  @Test
  public void testMazeConstructorValid() {
    assertEquals("The maze is 3 * 4, and it is a non-wrapping perfect maze. " +
                    "The start point of player is (2, 1). The saved walls are numbered by: " +
                    "9 2 11 13 15 16 ",
            game1.toString());
    assertEquals("The maze is 3 * 4, and it is a wrapping perfect maze. The start point " +
                    "of player is (2, 0). The saved walls are numbered by: 0 19 2 3 4 5 11 13 16 " +
                    "17 18 21 23 ",
            game2.toString());
    assertEquals("The maze is 3 * 4, and it is a non-wrapping room maze. The start " +
                    "point of player is (2, 2). The saved walls are numbered by: 11 13 16 ",
            game3.toString());
    assertEquals("The maze is 3 * 4, and it is a wrapping room maze. The start point " +
                    "of player is (0, 2). The saved walls are numbered by: 19 2 11 13 21 23 ",
            game4.toString());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMazeConstructorInvalid1() {
    game1 = new Game(-3, 4, 11, true, false,
            0.2, 0.2, 3, 1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMazeConstructorInvalid2() {
    game1 = new Game(3, -4, 11, true, false,
            0.2, 0.2, 3, 1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMazeConstructorInvalid3() {
    game3 = new Game(3, 4, -3, false, false,
            0.2, 0.2, 3, 1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMazeConstructorInvalid4() {
    game3 = new Game(3, 4, 7, false, false,
            0.2, 0.2, 3, 1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMazeConstructorInvalid5() {
    game3 = new Game(3, 4, 4, false, false,
            -0.2, 0.2, 3, 1);
    game3 = new Game(3, 4, 4, false, false,
            0.2, -0.2, 3, 1);
    game3 = new Game(3, 4, 4, false, false,
            0.2, 0.2, -3, 1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGoDownInvalid() {
    game1.move("down");
    game1.move("down");
    game1.move("down");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMoveEmptyString() {
    game1.move("");
    game4.move("");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMoveInvalidString() {
    game1.move("North");
    game4.move("abc");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testEmptyShoot() {
    game1.shoot("", 1);
    game4.shoot("", 1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidShoot() {
    game1.shoot("North", 1);
    game4.shoot("abc", 1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testShootZero() {
    game1.shoot("S", 0);
    game4.shoot("N", 0);
  }

  @Test
  public void testMoveNorthAndSouth() {
    game1.move("N");
    assertEquals("You are in cave (1, 1). Tunnels lead to the E, W, N, S",
            game1.getAdventurerLocation());
    game1.move("S");
    assertEquals("You are in cave (2, 1). Tunnels lead to the E, W, N",
            game1.getAdventurerLocation());
  }

  @Test
  public void testMoveEastAndWest() {
    game1.move("E");
    assertEquals("You are in cave (2, 3). Tunnels lead to the W",
            game1.getAdventurerLocation());
    game1.move("W");
    assertEquals("You are in cave (2, 1). Tunnels lead to the E, W, N",
            game1.getAdventurerLocation());
    game4.move("E");
    assertEquals("You are in cave (0, 3). Tunnels lead to the E, W, N",
            game4.getAdventurerLocation());
    game4.move("W");
    assertEquals("You are in cave (0, 2). Tunnels lead to the E, N, S",
            game4.getAdventurerLocation());
  }

  @Test
  public void testMoveToWall() {
    game1.move("N");
    game1.move("W");
    String beforeMoveToWall = game1.getAdventurerLocation();
    game1.move("S");
    String afterMoveToWall = game1.getAdventurerLocation();
    assertEquals(beforeMoveToWall, afterMoveToWall);

    String beforeMoveToWall4 = game4.getAdventurerLocation();
    game4.move("W");
    String afterMoveToWall4 = game4.getAdventurerLocation();
    assertEquals(beforeMoveToWall4, afterMoveToWall4);
  }

  @Test
  public void testMoveTunnel() {
    game1.move("N");
    game1.move("E");
    assertEquals("You are in cave (0, 3). Tunnels lead to the S",
            game1.getAdventurerLocation());
    game4.move("E");
    game4.move("E");
    game4.move("E");
    assertEquals("You are in cave (1, 1). Tunnels lead to the E, W, N",
            game4.getAdventurerLocation());
  }

  @Test
  public void testMoveTunnel2() {
    game1.move("E");
    assertEquals("You are in cave (2, 3). Tunnels lead to the W",
            game1.getAdventurerLocation());
    game4.move("E");
    game4.move("N");
    assertEquals("You are in cave (2, 0). Tunnels lead to the E, W, N, S",
            game4.getAdventurerLocation());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testShootDistanceZero() {
    game1.shoot("N", 0);
    game2.shoot("W", 0);
    game3.shoot("N", 0);
    game4.shoot("E", 0);
    assertEquals(false, game4.checkShotSuccess());
  }

  @Test
  public void testShootDistanceWrong() {
    game4.shoot("N", 3);
    assertEquals(false, game4.checkShotSuccess());
    game4.setAdventurerLocation(0, 0);
    game4.shoot("S", 1);
    assertEquals(false, game4.checkShotSuccess());
    game4.setAdventurerLocation(0, 0);
    game4.shoot("S", 3);
    assertEquals(false, game4.checkShotSuccess());
  }

  @Test
  public void testShootDistanceOverWumpus() {
    game1.move("N");
    game1.shoot("E", 3);
    assertEquals(false, game1.checkShotSuccess());
    game1.shoot("E", 1);
    assertEquals(true, game1.checkShotSuccess());
  }

  @Test
  public void testShootThroughTunnel() {
    game1.move("N");
    game1.shoot("E", 2);
    assertEquals(false, game1.checkShotSuccess());
    game1.shoot("E", 1);
    assertEquals(true, game1.checkShotSuccess());
  }

  @Test
  public void testShootToWall() {
    game4.shoot("W", 1);
    assertEquals(false, game4.checkShotSuccess());
    game1.shoot("E", 2);
    assertEquals(false, game4.checkShotSuccess());
  }

  @Test
  public void testOutOfArrows() {
    game1.shoot("N", 1);
    assertEquals(false, game1.checkShotSuccess());
    game1.shoot("N", 2);
    assertEquals(false, game1.checkShotSuccess());
    game1.shoot("W", 1);
    assertEquals(false, game1.checkShotSuccess());
    game1.shoot("E", 1);
    assertEquals(false, game1.checkShotSuccess());
  }

  @Test
  public void testShootSuccess() {
    game4.setAdventurerLocation(0, 0);
    game4.shoot("S", 2);
    assertEquals(true, game4.checkShotSuccess());
    assertEquals(true, game4.getAdventuresEnd());
  }

  @Test
  public void testPlayerMoveToBat() {
    game1.setAdventurerStartLocation(0, 2);
    assertNotEquals(game1.getAdventurerLocation(), "You are in cave (0, 2). " +
            "Tunnels lead to the W ");
  }

  @Test
  public void testMoveToPit() {
    game1.move("N");
    game1.move("N");
    assertEquals(true, game1.getCurrentRoom().getIsPit());
    assertEquals(true, game1.getAdventuresEnd());
    assertEquals(true, game4.getCurrentRoom().getDownCell().getIsPit());
  }

  @Test
  public void testMoveCloseToPit() {
    game1.move("N");
    assertEquals(true, game1.getCurrentRoom().getCloseToPit());
    game1.move("S");
    game1.move("W");
    assertEquals(false, game1.getCurrentRoom().getCloseToPit());
    game4.move("N");
    assertEquals(true, game4.getCurrentRoom().getCloseToPit());
    game4.move("W");
    assertEquals(false, game4.getCurrentRoom().getCloseToPit());
  }

  @Test
  public void testMoveCloseToPitByTunnel() {
    game4.setAdventurerStartLocation(1, 0);
    assertEquals(true, game4.getCurrentRoom().getCloseToPit());
  }

  @Test
  public void testMoveCloseToWumpus() {
    //Smell a wumpus through a two cells tunnel.
    game1.move("N");
    assertEquals(true, game1.getCurrentRoom().getCloseToWumpus());
    game1.move("W");
    assertEquals(false, game1.getCurrentRoom().getCloseToWumpus());
    //Smell a wumpus through a one cell tunnel.
    game4.move("N");
    assertEquals(true, game4.getCurrentRoom().getCloseToWumpus());
    game4.move("S");
    assertEquals(false, game4.getCurrentRoom().getCloseToWumpus());
  }

  @Test
  public void testEatenByWumpus() {
    game1.move("N");
    game1.move("E");
    assertEquals(true, game1.getAdventuresEnd());
    game4.move("E");
    game4.move("E");
    game4.move("S");
    game4.move("S");
    assertEquals(true, game4.getAdventuresEnd());
  }

  @Test
  public void testKillWumpusWithTunnel() {
    game1.move("N");
    game1.shoot("E", 1);
    assertEquals(true, game1.checkShotSuccess());
  }

  @Test
  public void testKillWumpusWithoutTunnel() {
    game4.move("N");
    game4.shoot("W", 1);
    assertEquals(true, game4.checkShotSuccess());
  }

  @Test
  public void testUnWinnable() {
    game1.setAdventurerStartLocation(0, 0);
    assertEquals(true, game1.checkUnwinnable());
    assertEquals(false, game4.checkUnwinnable());
  }

  @Test
  public void testMoveTwoPlayer() {
    game5.move("E");
    assertEquals(game5.getAdventurerLocation(), "You are in cave (2, 3). Tunnels lead to the W");
    assertEquals(game5.getMessage(), "You feel a draft.");
    game5.move("S");
    assertEquals(game5.getAdventurerLocation(), "You are in cave (2, 3). Tunnels lead to the W");
    assertEquals(game5.getMessage(), "Player is running out of bound! Please re-input " +
            "direction.");
//    game5.changePlayerFlag();
//    game5.move("N");
//    assertEquals(game5.getPlayerLocation(), "You are in cave (0, 1). Tunnels lead to the " +
//            "E, W, S");
//    game5.move("E");
//    assertEquals(game5.getPlayerLocation(), "You are in cave (1, 1). " +
//            "Tunnels lead to the E, W, N, S");
  }

  @Test
  public void testShootTwoPlayer() {
    game5.shoot("N", 1);
    assertEquals(game5.getMessage(), "You didn't shoot to the wumpus!");
    assertEquals(game5.getAdventuresEnd(), false);
//    game5.changePlayerFlag();
//    game5.shoot("W", 1);
//    assertEquals(game5.getAlert(), "Hee hee hee, you got the wumpus! Next time you won't " +
//            "be so lucky!");
//    assertEquals(game5.getGameEnd(), true);
  }
}
