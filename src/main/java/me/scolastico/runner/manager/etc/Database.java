package me.scolastico.runner.manager.etc;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import me.scolastico.runner.manager.dataholders.RunnerConfiguration;

public class Database {

  private static long counter = 0;
  private static ArrayList<String> availableConfigNames = new ArrayList<>();
  private static HashMap<String, RunnerConfiguration> localRunners = new HashMap<>();
  private static ArrayList<Long> schedulerIds = new ArrayList<>();
  private static HashMap<String, Instant> freshRunners = new HashMap<>();

  public static void delFreshRunner(String name) {
    freshRunners.remove(name);
  }

  public static void addFreshRunner(String name) {
    freshRunners.put(name, Instant.now().plusSeconds(60));
  }

  public static Instant getFreshRunner(String name) {
    ArrayList<String> toDelete = new ArrayList<>();
    for (String id : freshRunners.keySet()) {
      if (freshRunners.get(id).isBefore(Instant.now())) toDelete.add(name);
    }
    for (String id : toDelete) {
      freshRunners.remove(name);
    }
    return freshRunners.get(name);
  }

  public static HashMap<String, Instant> getFreshRunners() {
    return freshRunners;
  }

  public static void setFreshRunners(HashMap<String, Instant> freshRunners) {
    Database.freshRunners = freshRunners;
  }

  public static synchronized long getCount() {
    counter++;
    return counter;
  }

  public static long getCounter() {
    return counter;
  }

  public static void setCounter(long counter) {
    Database.counter = counter;
  }

  public static void addSchedulerId(Long id) {
    schedulerIds.add(id);
  }

  public static ArrayList<Long> getSchedulerIds() {
    return schedulerIds;
  }

  public static void setSchedulerIds(ArrayList<Long> schedulerIds) {
    Database.schedulerIds = schedulerIds;
  }

  public static boolean addAndCheckConfigName(String name) {
    if (availableConfigNames.contains(name)) return false;
    availableConfigNames.add(name);
    return true;
  }

  public static ArrayList<String> getAvailableConfigNames() {
    return availableConfigNames;
  }

  public static void setAvailableConfigNames(ArrayList<String> availableConfigNames) {
    Database.availableConfigNames = availableConfigNames;
  }

  public static void addLocalRunner(String name, RunnerConfiguration config) {
    localRunners.put(name, config);
  }

  public static void delLocalRunner(String name) {
    localRunners.remove(name);
  }

  public static HashMap<String, RunnerConfiguration> getLocalRunners() {
    return localRunners;
  }

  public static void setLocalRunners(HashMap<String, RunnerConfiguration> localRunners) {
    Database.localRunners = localRunners;
  }

}
