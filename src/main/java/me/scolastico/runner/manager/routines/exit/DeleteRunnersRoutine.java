package me.scolastico.runner.manager.routines.exit;

import java.util.HashMap;
import me.scolastico.runner.manager.dataholders.RunnerConfiguration;
import me.scolastico.runner.manager.dataholders.RunnerData;
import me.scolastico.runner.manager.etc.Database;
import me.scolastico.runner.manager.etc.GithubAPI;
import me.scolastico.tools.routine.Routine;
import me.scolastico.tools.routine.RoutineAnswer;

public class DeleteRunnersRoutine implements Routine {

  @Override
  public RoutineAnswer execute(HashMap<String, Object> objectMap) throws Exception {
    RunnerConfiguration config = (RunnerConfiguration) objectMap.get("config");
    RunnerData[] data = GithubAPI.getRunners(config);
    if (data == null) {
      System.out.println("Github response not valid!");
      return new RoutineAnswer(true, "github response not valid");
    }
    for (RunnerData d : data) {
      if (Database.getLocalRunners().containsKey(d.getName())) {
        GithubAPI.deleteRunner(config, d.getId());
      }
    }
    return new RoutineAnswer(objectMap);
  }

}
