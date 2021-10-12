package me.scolastico.runner.manager.routines.setup;

import java.util.HashMap;
import me.scolastico.runner.manager.dataholders.RunnerConfiguration;
import me.scolastico.runner.manager.dataholders.RunnerData;
import me.scolastico.runner.manager.etc.GithubAPI;
import me.scolastico.tools.routine.Routine;
import me.scolastico.tools.routine.RoutineAnswer;

public class DeleteOfflineRunnersRoutine implements Routine {

  @Override
  public RoutineAnswer execute(HashMap<String, Object> objectMap) throws Exception {
    RunnerConfiguration config = (RunnerConfiguration) objectMap.get("config");
    RunnerData[] toDelete = (RunnerData[]) objectMap.get("toDelete");
    for (RunnerData data:toDelete) {
      if (!GithubAPI.deleteRunner(config, data.getId())) {
        return new RoutineAnswer(true, "deletion not successful");
      }
    }
    return new RoutineAnswer(objectMap);
  }

}
