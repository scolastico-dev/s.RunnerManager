package me.scolastico.runner.manager.routines.starting;

import java.util.ArrayList;
import java.util.HashMap;
import me.scolastico.runner.manager.Application;
import me.scolastico.runner.manager.dataholders.RunnerConfiguration;
import me.scolastico.runner.manager.routines.setup.CheckConfigNameTagRoutine;
import me.scolastico.runner.manager.routines.setup.DeleteOfflineRunnersRoutine;
import me.scolastico.runner.manager.routines.setup.GetRunnersRoutine;
import me.scolastico.runner.manager.routines.setup.PullDockerTag;
import me.scolastico.tools.console.ConsoleLoadingAnimation;
import me.scolastico.tools.handler.ErrorHandler;
import me.scolastico.tools.routine.Routine;
import me.scolastico.tools.routine.RoutineAnswer;
import me.scolastico.tools.routine.RoutineManager;
import org.fusesource.jansi.Ansi;

public class SetupRoutine implements Routine {

  @Override
  public RoutineAnswer execute(HashMap<String, Object> objectMap) throws Exception {
    try {
      for (RunnerConfiguration config:Application.getConfig().getRunnerConfiguration()) {
        System.out.print("Setup of runner configuration '" + config.getOrg() + ":" + config.getTag() + "'... ");
        ConsoleLoadingAnimation.enable();
        ArrayList<Routine> routines = new ArrayList<>();
        routines.add(new CheckConfigNameTagRoutine());
        routines.add(new PullDockerTag());
        routines.add(new GetRunnersRoutine());
        routines.add(new DeleteOfflineRunnersRoutine());
        RoutineManager manager = new RoutineManager(routines);
        HashMap<String, Object> o = new HashMap<>();
        o.put("config", config);
        manager.startNotAsynchronously(o);
        ConsoleLoadingAnimation.disable();
        if (manager.isCanceled()) {
          System.out.println(Ansi.ansi().fgRed().a("[FAIL]").reset());
          System.out.println("Message of failure: " + manager.getErrorMessage());
          return new RoutineAnswer(true, "setup not successful");
        } else {
          System.out.println(Ansi.ansi().fgGreen().a("[OK]").reset());
        }
      }
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
