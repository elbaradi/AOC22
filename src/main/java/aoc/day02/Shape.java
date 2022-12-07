package aoc.day02;

public abstract class Shape {

  public static final int score = 0;

  abstract Class<? extends Shape> beats();
  Class<? extends Shape> draws() {
    return this.getClass();
  }
  abstract Class<? extends Shape> losesTo();

  int score() {
    try {
      return this.getClass().getField("score").getInt(null);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      System.err.println("Error retrieving class score: " + e.getMessage());
      return 0;
    }
  }

  boolean beats(Shape other) {
    return this.beats() == other.getClass();
  }

  boolean draws(Shape other) {
    return this.getClass() == other.getClass();
  }

  public static Shape of(char character) {
    // check value of character and return appropriate subclass instance
    return switch (character) {
      case 'A', 'X' -> new Rock();
      case 'B', 'Y' -> new Paper();
      case 'C', 'Z' -> new Scissors();
      default -> throw new IllegalArgumentException("Invalid character: " + character);
    };
  }

}
