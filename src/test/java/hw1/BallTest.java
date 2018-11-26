package hw1.test;

import static org.junit.Assert.*;

import hw1.*;
import org.junit.BeforeClass;
import org.junit.Test;

/** BallTest is a simple clearbox test of the {@link hw1.Ball} class. */
public class BallTest {

  private static Ball b = null;

  private static final double BALL_VOLUME = 20.0;
  private static final double JUNIT_DOUBLE_DELTA = 0.0001;

  @BeforeClass
  public static void setupBeforeTests() throws Exception {
    b = new Ball(BALL_VOLUME);
  }

  /** Test that Ball returns the correct volume when queried. */
  @Test
  public void testVolume() {
    assertEquals("b.getVolume()", BALL_VOLUME, b.getVolume(), JUNIT_DOUBLE_DELTA);
  }
}
