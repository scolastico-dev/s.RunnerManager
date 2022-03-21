package me.scolastico.runner.manager.routines.scheduler;

import java.util.ArrayList;
import java.util.HashMap;
import me.scolastico.runner.manager.Application;
import me.scolastico.runner.manager.dataholders.RunnerConfiguration;
import me.scolastico.runner.manager.dataholders.RunnerData;
import me.scolastico.runner.manager.etc.CommandExecuter;
import me.scolastico.runner.manager.etc.Database;
import me.scolastico.runner.manager.etc.GithubAPI;
import me.scolastico.tools.handler.ErrorHandler;
import me.scolastico.tools.routine.Routine;
import me.scolastico.tools.routine.RoutineAnswer;

public class StopRunnersRoutine implements Routine {

  @Override
  public RoutineAnswer execute(HashMap<String, Object> objectMap) throws Exception {
    RunnerConfiguration config = (RunnerConfiguration) objectMap.get("config");
    ArrayList<String> offlineRunners = (ArrayList<String>) objectMap.get("offlineRunners");
    int needed = (int) objectMap.get("needed");
    RunnerData[] data = (RunnerData[]) objectMap.get("data");
    HashMap<String, Long> offlineRunnerIds = new HashMap<>();
    if (needed < 0) {
      for (RunnerData d:data) {
        if (
            Database.getLocalRunners().containsKey(d.getName())
                && d.getStatus().equals("online")
                && !d.isBusy()
                && !offlineRunners.contains(d.getName())
        ) {
          offlineRunners.add(d.getName());
          needed++;
          if (needed >= 0) break;
        }
      }
    }
    for (RunnerData d:data) offlineRunnerIds.put(d.getName(), d.getId());
    for (final String name:offlineRunners) {
      System.out.println("Removing runner '" + name + "'...");
      Database.delLocalRunner(name);
      GithubAPI.deleteRunner(config, offlineRunnerIds.get(name));
      Thread thread = new Thread(() -> {
        try {
          CommandExecuter.run(Application.getConfig().getStopCommand().replaceAll("%runnerName%", name));
        } catch (Exception e) {
          ErrorHandler.handle(e);
        }
      });
      thread.start();
    }
    return new RoutineAnswer(objectMap);
  }

}
