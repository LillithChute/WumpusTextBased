import org.junit.Before;
import org.junit.Test;

import model.Room;

import static org.junit.Assert.assertEquals;

public class RoomTest {
  private Room room1;
  private Room room2;
  private Room room3;
  private Room room4;
  private Room room5;

  @Before
  public void setup() {
    room1 = new Room();
    room2 = new Room();
    room3 = new Room();
    room4 = new Room();
    room5 = new Room();
    room1.setNextRoom(room2, "right");
    room1.setNextRoom(room3, "left");
    room1.setNextRoom(room4, "up");
    room1.setNextRoom(room5, "down");
  }

  @Test
  public void testSetNextCell() {
    assertEquals(room1.getRoomOnTheRight(), room2);
    assertEquals(room1.getRoomOnTheLeft(), room3);
    assertEquals(room1.getRoomAbove(), room4);
    assertEquals(room1.getRoomBelow(), room5);
  }
}
