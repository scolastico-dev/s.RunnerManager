package me.scolastico.runner.manager.routines.starting;

import java.util.ArrayList;
import java.util.HashMap;
import me.scolastico.runner.manager.Application;
import me.scolastico.runner.manager.dataholders.RunnerConfiguration;
import me.scolastico.runner.manager.etc.Database;
import me.scolastico.runner.manager.routines.scheduler.CheckRunnersRoutine;
import me.scolastico.runner.manager.routines.scheduler.StartRunnersRoutine;
import me.scolastico.runner.manager.routines.scheduler.StopRunnersRoutine;
import me.scolastico.runner.manager.routines.setup.CheckConfigNameTagRoutine;
import me.scolastico.runner.manager.routines.setup.DeleteOfflineRunnersRoutine;
import me.scolastico.runner.manager.routines.setup.GetRunnersRoutine;
import me.scolastico.runner.manager.routines.setup.PullDockerTag;
import me.scolastico.tools.console.ConsoleLoadingAnimation;
import me.scolastico.tools.dataholder.SchedulerConfiguration;
import me.scolastico.tools.handler.ErrorHandler;
import me.scolastico.tools.handler.SchedulerHandler;
import me.scolastico.tools.routine.Routine;
import me.scolastico.tools.routine.RoutineAnswer;
import me.scolastico.tools.routine.RoutineManager;
import org.fusesource.jansi.Ansi;

public class SchedulerRoutine implements Routine {

  @Override
  public RoutineAnswer execute(HashMap<String, Object> objectMap) throws Exception {
    try {
      SchedulerHandler.enable();
      for (final RunnerConfiguration config:Application.getConfig().getRunnerConfiguration()) {
        System.out.print("Starting runner scheduler '" + config.getOrg() + ":" + config.getTag() + "'... ");
        ConsoleLoadingAnimation.enable();
        Long id = SchedulerHandler.registerTask(new SchedulerConfiguration(Application.getConfig().getCheckEverySeconds()*20L, new Runnable() {
          @Override
          public void run() {
            ArrayList<Routine> routines = new ArrayList<>();
            routines.add(new CheckRunnersRoutine());
            routines.add(new StartRunnersRoutine());
            routines.add(new StopRunnersRoutine());
            RoutineManager manager = new RoutineManager(routines);
            HashMap<String, Object> o = new HashMap<>();
            o.put("config", config);
            manager.startNotAsynchronously(o);
          }
        }));
        Database.addSchedulerId(id);
        ConsoleLoadingAnimation.disable();
        System.out.println(Ansi.ansi().fgGreen().a("[OK]").reset());
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
