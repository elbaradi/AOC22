package aoc.day02;

public class Rock extends Shape {

  public static final int score = 1;

  @Override
  Class<? extends Shape> beats() {
    return Scissors.class;
  }

  @Override
  Class<? extends Shape> losesTo() {
    return Paper.class;
  }

}
