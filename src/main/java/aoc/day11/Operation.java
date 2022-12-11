package aoc.day11;

public enum Operation {
  PLUS("+") {
    @Override
    public Long apply(Long x, Long y) {
      return x + y;
    }
  },
  TIMES("*") {
    @Override
    public Long apply(Long x, Long y) {
      return x * y;
    }
  };

  private final String symbol;

  Operation(String symbol) { this.symbol = symbol; }

  @Override
  public String toString() {
    return symbol;
  }

  public abstract Long apply(Long x, Long y);
}
