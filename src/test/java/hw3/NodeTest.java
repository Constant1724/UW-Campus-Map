package hw3;

import org.junit.Rule;
import org.junit.rules.Timeout;
import static org.junit.Assert.*;

import org.junit.Test;

public class NodeTest {
  @Rule public Timeout globalTimeout = Timeout.seconds(10); // 10 seconds max per method tested

  /**
   * Place holder for constructor.
   *
   * <p>When the fields are decided at implementation time, and constructor parameter is set, modify
   * the following properly.
   *
   * @param content content of a Node.
   * @return a new Node with content as its description.
   */
  public static Node create(String content) {
    return new Node(content);
  }

  /** try to create Node with empty, space and character content. */
  @Test
  public void testConstructor() {
    create("");
    create("Content1");
    create(" ");
  }

  /** Test Nodes with Equals content should have the Equals hashCode. */
  @Test
  public void testHashCode() {
    assertEquals(create("").hashCode(), create("").hashCode());
    assertEquals(create("Content1").hashCode(), create("Content1").hashCode());
    assertEquals(create(" ").hashCode(), create(" ").hashCode());

    assertNotEquals(create("").hashCode(), create(" ").hashCode());
    assertNotEquals(create("").hashCode(), create("Content2").hashCode());
    assertNotEquals(create("Content2").hashCode(), create(" ").hashCode());
  }

  /** Test Nodes with Equals content should be equal. */
  @Test
  public void testEquals() {
    assertEquals(create(""), create(""));
    assertEquals(create("Content1"), create("Content1"));
    assertEquals(create(" ").hashCode(), create(" ").hashCode());

    assertNotEquals(create(""), create(" ").hashCode());
    assertNotEquals(create(""), create("Content2"));
    assertNotEquals(create("Content2"), create(" ").hashCode());
  }

  /** Test get content method is implemented properly. */
  @Test
  public void testGetContent() {
    assertEquals(create("A").getContent(), "A");
    assertEquals(create(" ").getContent(), " ");
    assertEquals(create("1").getContent(), "1");
  }
}
