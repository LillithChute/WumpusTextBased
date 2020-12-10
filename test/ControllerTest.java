import org.junit.Test;

import java.io.Reader;
import java.io.StringReader;

import controller.Controller;
import interfaces.IController;
import model.Game;

import static org.junit.Assert.assertEquals;

public class ControllerTest {

  @Test
  public void testStartMove() throws Exception {
    StringBuffer out = new StringBuffer();
    Reader in = new StringReader("Move N Move E");
    IController controller = new Controller(in, out);
    controller.start(new Game(3, 4, 6, true, false,
            0.2, 0.3, 3, 1));
    assertEquals("You are in cave (1, 1). Tunnels lead to the E, W, N, S You are in " +
            "cave (0, 3). Tunnels lead to the S ", out.toString());
  }

  @Test(expected = Exception.class)
  public void testStartMoveToWall() throws Exception {
    StringBuffer out = new StringBuffer();
    Reader in = new StringReader("Move E Move E");
    IController controller = new Controller(in, out);
    controller.start(new Game(3, 4, 6, true, false,
            0.2, 0.3, 3, 1));
    assertEquals("You are in cave (1, 1). Tunnels lead to the E, W, N, S You are in " +
            "cave (0, 3). Tunnels lead to the S ", out.toString());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testStartMoveInvalid() throws Exception {
    StringBuffer out = new StringBuffer();
    Reader in = new StringReader("Move abcdefs Move E");
    IController controller = new Controller(in, out);
    controller.start(new Game(3, 4, 6, true, false,
            0.2, 0.3, 3, 1));
  }

  @Test
  public void testStartShootSuccess() throws Exception {
    StringBuffer out = new StringBuffer();
    Reader in = new StringReader("Move N Shoot E 1");
    IController controller = new Controller(in, out);
    controller.start(new Game(3, 4, 6, true, false,
            0.2, 0.3, 3, 1));
    assertEquals("You are in cave (1, 1). Tunnels lead to the E, W, N, S " +
            "You shooted to the wumpus successfully!", out.toString());
  }

  @Test(expected = IllegalStateException.class)
  public void testStartShootIllegal() throws Exception {
    StringBuffer out = new StringBuffer();
    Reader in = new StringReader("Move N Shoot E 10 E 1");
    IController controller = new Controller(in, out);
    controller.start(new Game(3, 4, 6, true, false,
            0.2, 0.3, 3,1));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testStartShootIllegalDirection() throws Exception {
    StringBuffer out = new StringBuffer();
    Reader in = new StringReader("Move N Shoot AAA 10 E 1");
    IController controller = new Controller(in, out);
    controller.start(new Game(3, 4, 6, true, false,
            0.2, 0.3, 3, 1));
  }

  @Test
  public void testStartShootTwice() throws Exception {
    StringBuffer out = new StringBuffer();
    Reader in = new StringReader("Move N Shoot E 10 Shoot E 1");
    IController controller = new Controller(in, out);
    controller.start(new Game(3, 4, 6, true, false,
            0.2, 0.3, 3, 1));
    assertEquals("You are in cave (1, 1). Tunnels lead to the E, W, N, S You didn't " +
            "shooted to the wumpus, and you have 2 arrows remains.You shooted to the wumpus " +
            "successfully!", out.toString());
  }
}
