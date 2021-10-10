package me.scolastico.example;

import java.util.ArrayList;
import me.scolastico.example.dataholders.Config;
import me.scolastico.example.routines.starting.ConfigRoutine;
import me.scolastico.example.routines.starting.DatabaseRoutine;
import me.scolastico.example.routines.starting.ErrorRoutine;
import me.scolastico.example.routines.starting.FinishRoutine;
import me.scolastico.example.routines.starting.HeaderRoutine;
import me.scolastico.tools.console.ConsoleLoadingAnimation;
import me.scolastico.tools.handler.ConfigHandler;
import me.scolastico.tools.handler.ErrorHandler;
import me.scolastico.tools.routine.Routine;
import me.scolastico.tools.routine.RoutineManager;
import me.scolastico.tools.simplified.SimplifiedResourceFileReader;

public class Application {

  private static ConfigHandler<Config> configHandler;
  private static Config config;
  private final static String version = SimplifiedResourceFileReader.getInstance().getStringFromResources("staticVars/VERSION");
  private final static String branch = SimplifiedResourceFileReader.getInstance().getStringFromResources("staticVars/BRANCH");
  private final static String commit = SimplifiedResourceFileReader.getInstance().getStringFromResources("staticVars/COMMIT");

  public static void main(String[] args) {
    try {
      ArrayList<Routine> routines = new ArrayList<>();
      routines.add(new ErrorRoutine());
      routines.add(new HeaderRoutine());
      routines.add(new ConfigRoutine());
      routines.add(new DatabaseRoutine());
      routines.add(new FinishRoutine());
      RoutineManager manager = new RoutineManager(routines);
      manager.startNotAsynchronously();
    } catch (Exception e) {
      try {
        ConsoleLoadingAnimation.disable();
      } catch (Exception ignored) {}
      ErrorHandler.handleFatal(e);
    }
  }

  public static String getBranch() {
    return branch;
  }

  public static String getCommit() {
    return commit;
  }

  public static String getVersion() {
    return version;
  }

  public static ConfigHandler<Config> getConfigHandler() {
    return configHandler;
  }

  public static void setConfigHandler(ConfigHandler<Config> configHandler) {
    Application.configHandler = configHandler;
  }

  public static Config getConfig() {
    return config;
  }

  public static void setConfig(Config config) {
    Application.config = config;
  }

}
