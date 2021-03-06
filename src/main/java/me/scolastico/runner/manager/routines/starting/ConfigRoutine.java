package me.scolastico.runner.manager.routines.starting;

import java.util.HashMap;
import me.scolastico.runner.manager.Application;
import me.scolastico.runner.manager.dataholders.Config;
import me.scolastico.tools.console.ConsoleLoadingAnimation;
import me.scolastico.tools.ebean.DatabaseConfig;
import me.scolastico.tools.handler.ConfigHandler;
import me.scolastico.tools.handler.ErrorHandler;
import me.scolastico.tools.routine.Routine;
import me.scolastico.tools.routine.RoutineAnswer;
import org.fusesource.jansi.Ansi;

public class ConfigRoutine implements Routine {

  @Override
  public RoutineAnswer execute(HashMap<String, Object> hashMap) throws Exception {
    try {
      boolean configMissing = false;

      System.out.print("Loading config... ");
      ConsoleLoadingAnimation.enable();
      ConfigHandler<Config> configHandler = new ConfigHandler<>(new Config(), "config.json");
      if (!configHandler.checkIfExists()) {
        configHandler.saveDefaultConfig();
        configMissing = true;
      }
      Config config = configHandler.loadConfig();
      ConsoleLoadingAnimation.disable();
      if (configMissing) {
        System.out.println(Ansi.ansi().fgRed().a("[FAIL]").reset());
      } else {
        System.out.println(Ansi.ansi().fgGreen().a("[OK]").reset());
      }

      if (config.isDebug()) {
        configHandler.storeConfig(config);
      }

      if (configMissing) {
        System.out.println();
        System.out.println("One or more configs are missing. Generated new ones!");
        System.out.println("Please edit the default config values and restart the application.");
        System.out.println();
        System.out.println("Exiting!");
        return new RoutineAnswer(true, "config missing");
      }
      Application.setConfig(config);
      Application.setConfigHandler(configHandler);
      return new RoutineAnswer(hashMap);
    } catch (Exception e) {
      try {
        ConsoleLoadingAnimation.disable();
      } catch (Exception ignored) {}
      System.out.println(Ansi.ansi().fgRed().a("[FAIL]").reset());
      ErrorHandler.handle(e);
      return new RoutineAnswer(true, "exception");
    }
  }

}
