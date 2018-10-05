package hw1;

import java.util.Random;

/**
 * This class is used to print out Hello World in 5 languages. Each time it will randomly choose one
 * of five languages and print out 'Hello World' in that language.
 */
public class RandomHello {
  /** Prints a random greeting to the console. */
  public static void main(String[] argv) {
    RandomHello randomHello = new RandomHello();
    System.out.println(randomHello.getGreeting());
  }

  /** @return a greeting, randomly chosen from five possibilities */
  public String getGreeting() {
    // YOUR CODE GOES HERE
    Random randomGenerator = new Random();
    String[] greetings = new String[5];
    greetings[0] = "Hello World";
    greetings[1] = "Hola Mundo";
    greetings[2] = "Bonjour Monde";
    greetings[3] = "Hallo Welt";
    greetings[4] = "Ciao Mondo";
    return greetings[randomGenerator.nextInt(greetings.length)];
  }
}
