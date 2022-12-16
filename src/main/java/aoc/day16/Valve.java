package aoc.day16;

import java.util.ArrayList;
import java.util.List;

public record Valve(String id, int flow, List<String> tunnels) {

  public static Valve newInstance(String string) { // “Valve AA has flow rate=0; tunnels lead to valves DD, II, BB"
    String[] tokens = string.split("[; ,]");

    String identifier = tokens[1];
    int flowRate = Integer.parseInt(tokens[4].substring(5));
    List<String> tunnels = new ArrayList<>();

    for (int i = 10; i < tokens.length; i += 2) {
      tunnels.add(tokens[i]);
    }

    return new Valve(identifier, flowRate, tunnels);
  }

}
