package me.scolastico.runner.manager.routines.starting;

import java.util.ArrayList;
import java.util.HashMap;
import me.scolastico.runner.manager.Application;
import me.scolastico.runner.manager.dataholders.RunnerConfiguration;
import me.scolastico.runner.manager.etc.Database;
import me.scolastico.runner.manager.routines.exit.DeleteRunnersRoutine;
import me.scolastico.runner.manager.routines.exit.StopRunnersRoutine;
import me.scolastico.tools.console.ConsoleLoadingAnimation;
import me.scolastico.tools.handler.ErrorHandler;
import me.scolastico.tools.handler.SchedulerHandler;
import me.scolastico.tools.routine.Routine;
import me.scolastico.tools.routine.RoutineAnswer;
import me.scolastico.tools.routine.RoutineManager;
import org.fusesource.jansi.Ansi;

public class ShutdownHookRoutine implements Routine {

  @Override
  public RoutineAnswer execute(HashMap<String, Object> objectMap) throws Exception {
    try {
      System.out.print("Starting shutdown hook... ");
      ConsoleLoadingAnimation.enable();
      Thread shutdownHook = new Thread(() -> {
        System.out.println();
        System.out.println("Got request to shutdown. Will now stop...");
        for (Long id:Database.getSchedulerIds()) {
          System.out.print("Stopping scheduler with id '" + id + "'... ");
          SchedulerHandler.removeConfiguration(id);
          System.out.println("[OK]");
        }
        for (RunnerConfiguration config:Application.getConfig().getRunnerConfiguration()) {
          System.out.print("Running exit routine for '" + config.getOrg() + ":" + config.getTag() + "'... ");
          ArrayList<Routine> routines = new ArrayList<>();
          routines.add(new StopRunnersRoutine());
          routines.add(new DeleteRunnersRoutine());
          RoutineManager manager = new RoutineManager(routines);
          HashMap<String, Object> o = new HashMap<>();
          o.put("config", config);
          manager.startNotAsynchronously(o);
          System.out.println("[OK]");
        }
        System.out.println("Done with soft exit.");
        System.out.println();
        System.out.println("Bye!");
        System.out.println();
      });
      Runtime.getRuntime().addShutdownHook(shutdownHook);
      ConsoleLoadingAnimation.disable();
      System.out.println(Ansi.ansi().fgGreen().a("[OK]").reset());
    } catch (Exception e) {
      try {
        ConsoleLoadingAnimation.disable();
      } catch (Exception ignored) {}
      System.out.println(Ansi.ansi().fgRed().a("[FAIL]").reset());
      ErrorHandler.handle(e);
      return new RoutineAnswer(true, "exception");
    }
    return new RoutineAnswer(objectMap);
  }

}
