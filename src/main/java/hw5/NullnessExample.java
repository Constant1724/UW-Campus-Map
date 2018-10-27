package hw5;

/** An unannotated class with a null pointer exception */
public class NullnessExample {
  /**
   * @param args The command-line args (ignored)
   */
  public static void main(String[] args) {
    Object myObject = null;
    System.out.println(myObject.toString());
  }
}
