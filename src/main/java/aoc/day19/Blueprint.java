package aoc.day19;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.stream.IntStream;

import static java.lang.Math.max;

public record Blueprint(int id, Cost oreRobot, Cost clayRobot, Cost obsRobot, Cost geodeRobot) {

  public static Blueprint newInstance(String s) {
    String[] parts = s.split("[:.]");

    String[] tokens = parts[0].split(" ");
    int id = Integer.parseInt(tokens[1]);

    tokens = parts[1].split(" ");
    Cost oreRobot = new Cost(Integer.parseInt(tokens[5]), 0, 0);

    tokens = parts[2].split(" ");
    Cost clayRobot = new Cost(Integer.parseInt(tokens[5]), 0, 0);

    tokens = parts[3].split(" ");
    Cost obsidianRobot = new Cost(Integer.parseInt(tokens[5]), Integer.parseInt(tokens[8]), 0);

    tokens = parts[4].split(" ");
    Cost geodeRobot = new Cost(Integer.parseInt(tokens[5]), 0, Integer.parseInt(tokens[8]));

    return new Blueprint(id, oreRobot, clayRobot, obsidianRobot, geodeRobot);
  }

  public int getMostGeodes(int minutes) {

    int mostGeodes = 0;

    Deque<State> states = new ArrayDeque<>();
    State start = new State();
    states.add(start);

    final int maxOreToSpendInOneMinute =
        max(max(oreRobot.oreCost, clayRobot.oreCost), max(obsRobot.oreCost, geodeRobot.oreCost));
    final int maxClayToSpendInOneMinute = obsRobot.clayCost;
    final int maxObsToSpendInOneMinute = geodeRobot.obsCost;

    while (!states.isEmpty()) {
      start = states.removeFirst();
      int minutesLeft = minutes - start.minutes;

      if (start.numGeodes > mostGeodes) {
        mostGeodes = start.numGeodes;
      }

      if (minutesLeft == 0) {
        continue;
      }

      if (start.numGeodes + start.numGeodeRobots * (minutesLeft) + IntStream.range(0, minutesLeft).sum() < mostGeodes) {
        continue;
      }

      Cost resources = new Cost(start.numOre, start.numClay, start.numObs);

      boolean madeRobot = false;

      if (start.numObsRobots > 0 && resources.isEnoughToBuy(geodeRobot)) {
        states.add(new State(start).buyRobots(0, 0, 0, 1).next());
        madeRobot = true;
      }
      else {
        int maxOreNeeded = minutesLeft * maxOreToSpendInOneMinute;
        if (start.numOreRobots < maxOreToSpendInOneMinute &&
            start.numOre + start.numOreRobots * minutesLeft < maxOreNeeded && resources.isEnoughToBuy(oreRobot)) {
          states.add(new State(start).buyRobots(1, 0, 0, 0).next());
          madeRobot = true;
        }

        int maxClayNeeded = minutesLeft * maxClayToSpendInOneMinute;
        if (start.numClayRobots < maxClayToSpendInOneMinute &&
            start.numClay + start.numClayRobots * minutesLeft < maxClayNeeded && resources.isEnoughToBuy(clayRobot)) {
          states.add(new State(start).buyRobots(0, 1, 0, 0).next());
          madeRobot = true;
        }

        int maxObsNeeded = minutesLeft * maxObsToSpendInOneMinute;
        if (start.numClayRobots > 0 && start.numObsRobots < maxObsToSpendInOneMinute &&
            start.numObs + start.numObsRobots * minutesLeft < maxObsNeeded && resources.isEnoughToBuy(obsRobot)) {
          states.add(new State(start).buyRobots(0, 0, 1, 0).next());
          madeRobot = true;
        }
      }

      if (!madeRobot || start.numOre < maxOreToSpendInOneMinute + 1) {
        states.add(new State(start).next());
      }

    }

    return mostGeodes;
  }

  private record Cost(int oreCost, int clayCost, int obsCost) {

    public boolean isEnoughToBuy(Cost cost) {
      return cost.oreCost <= oreCost && cost.clayCost <= clayCost && cost.obsCost <= obsCost;
    }

  }

  private class State {

    int numOre = 0;
    int numClay = 0;
    int numObs = 0;
    int numGeodes = 0;

    int numOreRobots = 1;
    int numClayRobots = 0;
    int numObsRobots = 0;
    int numGeodeRobots = 0;

    int pendingOreRobots = 0;
    int pendingClayRobots = 0;
    int pendingObsRobots = 0;
    int pendingGeodeRobots = 0;

    int minutes = 0;

    State() {
    }

    State(State o) {
      numOre = o.numOre;
      numClay = o.numClay;
      numObs = o.numObs;
      numGeodes = o.numGeodes;

      numOreRobots = o.numOreRobots;
      numClayRobots = o.numClayRobots;
      numObsRobots = o.numObsRobots;
      numGeodeRobots = o.numGeodeRobots;

      minutes = o.minutes;
    }

    protected State next() {
      numOre += numOreRobots;
      numClay += numClayRobots;
      numObs += numObsRobots;
      numGeodes += numGeodeRobots;

      numOreRobots += pendingOreRobots;
      numClayRobots += pendingClayRobots;
      numObsRobots += pendingObsRobots;
      numGeodeRobots += pendingGeodeRobots;

      pendingOreRobots = 0;
      pendingClayRobots = 0;
      pendingObsRobots = 0;
      pendingGeodeRobots = 0;

      minutes++;

      return this;
    }

    protected State buyRobots(int numOreRobots, int numClayRobots, int numObsRobots, int numGeodeRobots) {
      if (numOreRobots > 0) {
        numOre -= numOreRobots * oreRobot.oreCost;
        pendingOreRobots += numOreRobots;
      }
      if (numClayRobots > 0) {
        numOre -= numClayRobots * clayRobot.oreCost;
        pendingClayRobots += numClayRobots;
      }
      if (numObsRobots > 0) {
        numOre -= numObsRobots * obsRobot.oreCost;
        numClay -= numObsRobots * obsRobot.clayCost;
        pendingObsRobots += numObsRobots;
      }
      if (numGeodeRobots > 0) {
        numOre -= numGeodeRobots * geodeRobot.oreCost;
        numObs -= numGeodeRobots * geodeRobot.obsCost;
        pendingGeodeRobots += numGeodeRobots;
      }

      return this;
    }

  }

}

