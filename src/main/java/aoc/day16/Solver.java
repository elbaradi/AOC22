package aoc.day16;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

class Solver {

  private static final String INPUT_PATHNAME_TEMPLATE = "src/resources/input_day_##.txt";
  private static final String DAY = "16";
  private static final String START = "AA";

  public static void main(String[] args) {

    List<String> lines = getRawInput();
    List<Valve> valves = getValves(lines);
    Map<String, Valve> valveMap = getIdentifierToValveMap(valves);
    Valve start = valveMap.get(START);

    // Map of <source, <destination, distance>>.
    // Destinations with 0 flow rate have been excluded.
    Map<Valve, Map<Valve, Integer>> tunnels = getTunnels(valves, valveMap);

    // Problem #1
    // Generates a map of all possible 30-minute paths, and their pressures.
    // A path is represented as a String
    // Would've been better to represent paths as a List of Valve-s.
    Map<String, Integer> pressureByPathMap = findPressureByPaths(start, tunnels, 30);

    int maxPressure = pressureByPathMap.values().stream().reduce(Integer.MIN_VALUE, Integer::max);

    System.out.println("Solution Problem #1: " + maxPressure);

    // Problem #2
    // Takes 25 seconds to compute.
    // For every set of valves originally opened,
    // generates new paths for the Elephant to travel
    // and updates the score.
    // The Elephant never opens valves already opened in the original path.
    pressureByPathMap = findPressureByPaths(start, tunnels, 26);

    // Performance optimization:
    // Once we have the pressures for every path 'you' could take,
    // we no longer care about 'your' exact path,
    // we only care about the set of valves you have opened.
    // Here we filter to leave only the highest pressure per set of closed valves.
    pressureByPathMap = getFilteredPressureByPathMap(pressureByPathMap);

    // Paths with below average released pressure can be safely ignored
    long averagePressure = Math.round(pressureByPathMap.values().stream().collect(Collectors.averagingInt(i -> i)));
    pressureByPathMap = pressureByPathMap.entrySet()
            .stream()
            .filter(entry -> entry.getValue() >= averagePressure)
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

    int maxPressureWithElephantHelping = pressureByPathMap.entrySet()
        .stream()
        .map(entry -> getMostElephantPressure(start, tunnels, entry))
        .reduce(Integer.MIN_VALUE, Integer::max);

    System.out.println("Solution Problem #2: " + maxPressureWithElephantHelping);

  }

  private static Map<String, Integer> getFilteredPressureByPathMap(Map<String, Integer> pressureByPathMap) {
    Map<Set<String>, Integer> filteredPressureByPathMap = new HashMap<>();

    pressureByPathMap.forEach(
        (s, i) -> {
          String[] valves = s.split("->");
          Set<String> path = new HashSet<>();
          for (String valve : valves)
            if (!valve.startsWith("W"))
              path.add(valve);
          Integer pressure = filteredPressureByPathMap.get(path);
          if (pressure == null || i > pressure )
            filteredPressureByPathMap.put(path, i);
        }
    );

    return filteredPressureByPathMap.entrySet().stream().collect(Collectors.toMap(
                                                                     entry ->
                                                                       entry.getKey().toString(), Map.Entry::getValue
    ));

  }

  private static int getMostElephantPressure(
      Valve start,
      Map<Valve, Map<Valve, Integer>> tunnels,
      Map.Entry<String, Integer> entry
  ) {
    int pressureAlreadyReleased = entry.getValue();
    String closedValves = entry.getKey();
    Map<Valve, Map<Valve, Integer>> filteredTunnels = getFilteredTunnels(tunnels, closedValves);

    Map<String, Integer> pressureByElephantPathsMap = findPressureByPaths(start, filteredTunnels, 26);

    Integer max = pressureByElephantPathsMap.values().stream().max(Comparator.naturalOrder()).orElse(0);

    return max + pressureAlreadyReleased;
  }

  // Takes the relevant tunnels, and filters out the tunnels 'you' have already visited.
  // Return tunnels to visit for the Elephant
  // Filtering this way only guarantees the correct outcome
  // if there are too many tunnels for both you and the elephant to visit in x minutes.
  private static Map<Valve, Map<Valve, Integer>> getFilteredTunnels(
      Map<Valve, Map<Valve, Integer>> tunnels, String excludeValves
  ) {
    Map<Valve, Map<Valve, Integer>> filteredTunnels = new HashMap<>();

    if (tunnels != null) {
      for (Map.Entry<Valve, Map<Valve, Integer>> entry : tunnels.entrySet()) {
        if (entry.getKey().id().equals(START) || !excludeValves.contains(entry.getKey().id())) {
          Map<Valve, Integer> filteredValues = new HashMap<>();
          for (Map.Entry<Valve, Integer> sub : entry.getValue().entrySet()) {
            if (!excludeValves.contains(sub.getKey().id())) {
              filteredValues.put(sub.getKey(), sub.getValue());
            }
          }
          filteredTunnels.put(entry.getKey(), filteredValues);
        }
      }
    }

    return filteredTunnels;
  }

  private static Map<Valve, Map<Valve, Integer>> getTunnels(List<Valve> valves, Map<String, Valve> valveMap) {
    Map<Valve, Map<Valve, Integer>> tunnels = new HashMap<>();

    for (Valve valve : valves) {
      if (valve.flow() == 0 && !valve.id().equals(START)) {
        continue;
      }
      Map<Valve, Integer> shortestPathFromValve = new HashMap<>();
      shortestPathFromValve.put(valve, 0);

      travelToNeighbors(valve, shortestPathFromValve, 1, valveMap);
      shortestPathFromValve = shortestPathFromValve.entrySet()
          .stream()
          .filter(entry -> entry.getKey().flow() != 0 && !entry.getKey().equals(valve))
          .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
      tunnels.put(valve, shortestPathFromValve);
    }

    return tunnels;
  }

  private static void travelToNeighbors(
      Valve src, Map<Valve, Integer> shortestPathFromValve, int dist, Map<String, Valve> valveMap
  ) {
    for (String dst : src.tunnels()) {
      Valve valve = valveMap.get(dst);
      if (!shortestPathFromValve.containsKey(valve) || shortestPathFromValve.get(valve) > dist) {
        shortestPathFromValve.put(valve, dist);
        travelToNeighbors(valve, shortestPathFromValve, dist + 1, valveMap);
      }
    }
  }

  private static Map<String, Integer> findPressureByPaths(
      Valve start, Map<Valve, Map<Valve, Integer>> tunnels, int minutes
  ) {

    Map<String, Integer> pathScoreMap = new HashMap<>();
    String path = start.id();
    pathScoreMap.put(path, 0);

    scorePaths(start, path, minutes, tunnels, pathScoreMap);

    return pathScoreMap;
  }

  private static void scorePaths(
      Valve start,
      String previous,
      int minutes,
      Map<Valve, Map<Valve, Integer>> tunnels,
      Map<String, Integer> pathScoreMap
  ) {
    if (minutes <= 0) {
      return;
    }

    Map<Valve, Integer> dists = tunnels.get(start);
    int score = pathScoreMap.get(previous);
    pathScoreMap.remove(previous);

    for (Map.Entry<Valve, Integer> destination : dists.entrySet()) {
      Valve valve = destination.getKey();
      String path = previous;

      if (!path.contains(valve.id()) && dists.get(valve) + 1 <= minutes) {
        path = path.concat("->" + destination.getKey().id());
        pathScoreMap.put(path, score + (dists.get(valve) + 1) * (currentFlows(path, tunnels) - valve.flow()));
        scorePaths(valve, path, minutes - dists.get(valve) - 1, tunnels, pathScoreMap);
      }
      else {
        path = path.concat("->W" + minutes);
        pathScoreMap.put(path, score + minutes * currentFlows(path, tunnels));
      }
    }

  }

  private static Integer currentFlows(String path, Map<Valve, Map<Valve, Integer>> tunnels) {
    int pressureIncrease = 0;

    for (Valve valve : tunnels.keySet()) {
      if (path.contains(valve.id())) {
        pressureIncrease += valve.flow();
      }
    }

    return pressureIncrease;
  }

  private static Map<String, Valve> getIdentifierToValveMap(List<Valve> valves) {
    return valves.stream().collect(Collectors.toUnmodifiableMap(Valve::id, Function.identity()));
  }

  private static List<Valve> getValves(List<String> lines) {
    List<Valve> valves = new ArrayList<>();

    for (String line : lines) {
      valves.add(Valve.newInstance(line));
    }

    return valves;
  }

  private static List<String> getRawInput() {
    List<String> lines = new ArrayList<>();
    try (BufferedReader reader = new BufferedReader(new FileReader(getInputPathname()))) {
      reader.lines().forEach(lines::add);
    }
    catch (IOException e) {
      throw new RuntimeException("Retrieving input went wrong!");
    }
    return lines;
  }

  public static String getInputPathname() {
    return INPUT_PATHNAME_TEMPLATE.replace("##", DAY);
  }

}
