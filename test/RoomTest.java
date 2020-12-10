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
    room1 = new Room(0,0);
    room2 = new Room(1,1);
    room3 = new Room(2,3);
    room4 = new Room(4,5);
    room5 = new Room(4,5);
    room1.setNextCell(room2, "right");
    room1.setNextCell(room3, "left");
    room1.setNextCell(room4, "up");
    room1.setNextCell(room5, "down");
  }

  @Test
  public void testSetNextCell() {
    assertEquals(room1.getRightCell(), room2);
    assertEquals(room1.getLeftCell(), room3);
    assertEquals(room1.getUpCell(), room4);
    assertEquals(room1.getDownCell(), room5);
  }
}
