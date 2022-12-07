package aoc.day02;

public class Paper extends Shape {

  public static final int score = 2;

  @Override
  Class<? extends Shape> beats() {
    return Rock.class;
  }

  @Override
  Class<? extends Shape> losesTo() {
    return Scissors.class;
  }

}

