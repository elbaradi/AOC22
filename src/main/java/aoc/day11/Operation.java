package aoc.day11;

import java.util.Map;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

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

  private static final Map<String, Operation> stringToEnum = Stream.of(values()).collect(toMap(Object::toString, e -> e));

  public static Operation fromString(String symbol) {
    return stringToEnum.get(symbol);
  }

  public abstract Long apply(Long x, Long y);
}
