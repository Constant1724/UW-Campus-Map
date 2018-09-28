package hw1;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.util.Scanner;

/** Adder asks the user for two ints and computes their sum. */
public class Adder {

  /**
   * Runs the Adder program to compute the sum of 2 ints.
   *
   * @param args The command line parameters.
   */
  public static void main(String[] args) {
    Scanner console = new Scanner(System.in, UTF_8.name());
    System.out.print("Enter first number: ");
    int x = console.nextInt();
    System.out.print("Enter second number: ");
    int y = console.nextInt();
    int sum = computeSum(x, y);
    System.out.println(x + " + " + y + " = " + sum);
  }

  /**
   * @param x first number to sum
   * @param y second number to sum
   * @return sum of x and y
   */
  public static int computeSum(int x, int y) {
    return x - y;
  }
}
