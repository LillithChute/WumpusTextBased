import org.junit.Test;

import java.io.Reader;
import java.io.StringReader;

import controller.Controller;
import interfaces.IController;
import model.Game;

import static org.junit.Assert.assertEquals;

/** Tests for the controller.*/
public class ControllerTest {
  private final Game game = new Game(3, 4, 6, true, false,
          0.2, 0.3, 3, 1);

  @Test
  public void testMove() throws Exception {
    StringBuffer out = new StringBuffer();
    Reader in = new StringReader("Move N Move E");
    IController controller = new Controller(in, out);
    controller.start(game);
    assertEquals(
        "You are in cave (1, 1). Tunnels lead to the E, W, N, S You are in "
            + "cave (0, 3). Tunnels lead to the S ",
        out.toString());
  }

  @Test(expected = Exception.class)
  public void testMoveToTheWall() throws Exception {
    StringBuffer out = new StringBuffer();
    Reader in = new StringReader("Move E Move E");
    IController controller = new Controller(in, out);
    controller.start(game);
    assertEquals(
        "You are in cave (1, 1). Tunnels lead to the E, W, N, S You are in "
            + "cave (0, 3). Tunnels lead to the S ",
        out.toString());
  }

  @Test
  public void testShootArrowSuccess() throws Exception {
    StringBuffer out = new StringBuffer();
    Reader in = new StringReader("Move N Shoot E 1");
    IController controller = new Controller(in, out);
    controller.start(game);
    assertEquals(
        "You are in cave (1, 1). Tunnels lead to the E, W, N, S You shot to the wumpus!",
        out.toString());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testShootArrowInIllegalDirection() throws Exception {
    StringBuffer out = new StringBuffer();
    Reader in = new StringReader("Move N Shoot AAA 10 E 1");
    IController controller = new Controller(in, out);
    controller.start(game);
  }

  @Test
  public void testShootArrowTwice() throws Exception {
    StringBuffer out = new StringBuffer();
    Reader in = new StringReader("Move N Shoot E 10 Shoot E 1");
    IController controller = new Controller(in, out);
    controller.start(game);
    assertEquals(
        "You are in cave (1, 1). Tunnels lead to the E, W, N, S You missed the wumpus, "
            + "and you have 2 arrows remains.You shot to the wumpus!",
        out.toString());
  }
}
