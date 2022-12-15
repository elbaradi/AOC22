package aoc.day15;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static java.lang.Math.abs;

class Solver {

  private static final String INPUT_PATHNAME_TEMPLATE = "src/resources/input_day_##.txt";
  private static final String DAY = "15";

  public static void main(String[] args) {

    List<String> lines = getRawInput();
    Map<Pos, Pos> closestBeacons = getClosestBeacons(lines);

    // Problem #1
    int y = 2_000_000;

    Set<Pos> cannotBeBeacons = getSetOfPositionsAtYWhereForSureThereIsNoBeacon(closestBeacons, y);

    System.out.println("Solution Problem #1: " + cannotBeBeacons.size());

    // Problem #2
    Pos hiddenBeacon = getHiddenBeacon(closestBeacons);

    long tuningFrequency = hiddenBeacon.x() * 4_000_000L + hiddenBeacon.y();

    System.out.println("Solution Problem #2: " + tuningFrequency);

  }

  private static Pos getHiddenBeacon(Map<Pos, Pos> closestBeacons) {
    for (int i = 0; i <= 4_000_000; i++) {

      List<Range> possibleBeacons = new ArrayList<>();
      possibleBeacons.add(new Range(0, 4_000_000));

      int y = i;
      List<Range> noBeaconRanges =
          closestBeacons.entrySet().stream()
              .map(entry -> {
            Pos sensor = entry.getKey();
            Pos beacon = entry.getValue();
            return getNoBeaconRangeAtY(sensor, beacon, y);
          })
              .filter(Optional::isPresent)
              .map(Optional::get)
              .toList();
      for (Range range : noBeaconRanges) {
        possibleBeacons = Range.minus(possibleBeacons, range);
      }
      if (!possibleBeacons.isEmpty()) {
        return new Pos(possibleBeacons.get(0).start, y);
      }
    }
    throw new IllegalStateException("No hidden beacon found!");
  }

  private static Optional<Range> getNoBeaconRangeAtY(Pos sensor, Pos beacon, final int y) {

    int diffY = abs(sensor.y() - y);
    int dist = calculateDistance(sensor, beacon);

    if (diffY <= dist)
      return Optional.of(new Range(sensor.x() - (dist - diffY), sensor.x() + (dist - diffY)));

    return Optional.empty();
  }

  private static Set<Pos> getSetOfPositionsAtYWhereForSureThereIsNoBeacon(Map<Pos, Pos> closestBeacons, int y) {
        Set<Pos> cannotBeBeacons = new HashSet<>();

        closestBeacons.forEach(
            (sensor, closestBeacon) -> {
              int dist = calculateDistance(sensor, closestBeacon);
              Set<Pos> cannotBeBeaconsOfThisSensor = addPoints(sensor, dist, y);
              cannotBeBeaconsOfThisSensor.remove(closestBeacon);

              cannotBeBeacons.addAll(cannotBeBeaconsOfThisSensor);
            }
        );

        return cannotBeBeacons;
  }

    private static Set<Pos> addPoints(Pos sensor, int dist, int y) {
      Set<Pos> cannotBeBeacons = new HashSet<>();

      int diffY = abs(sensor.y() - y);
      for (int i = 0; i <= dist - diffY; i++) {
        cannotBeBeacons.add(new Pos(sensor.x(), y).add(new Pos(i, 0)));
        cannotBeBeacons.add(new Pos(sensor.x(), y).add(new Pos(-i, 0)));
      }

      return cannotBeBeacons;
    }

  private static int calculateDistance(Pos sensor, Pos beacon) {
    return abs(beacon.x()- sensor.x()) + abs(beacon.y() - sensor.y());
  }

  private static Map<Pos, Pos> getClosestBeacons(List<String> lines) {
    Map<Pos, Pos> closestBeacons = new HashMap<>();

    lines.forEach(
        line -> {
          String[] parts = line.split(": ");
          Pos sensor = new Pos(parts[0]);
          Pos beacon = new Pos(parts[1]);
          closestBeacons.put(sensor, beacon);
        }
    );

    return closestBeacons;
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
