package aoc.day02;

public class Scissors extends Shape {

  public static final int score = 3;

  @Override
  Class<? extends Shape> beats() {
    return Paper.class;
  }

  @Override
  Class<? extends Shape> losesTo() {
    return Rock.class;
  }

}

