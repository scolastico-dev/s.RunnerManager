package me.scolastico.runner.manager.routines.setup;

import java.util.ArrayList;
import java.util.HashMap;
import me.scolastico.runner.manager.Application;
import me.scolastico.runner.manager.dataholders.RunnerConfiguration;
import me.scolastico.runner.manager.dataholders.RunnerData;
import me.scolastico.runner.manager.etc.GithubAPI;
import me.scolastico.tools.routine.Routine;
import me.scolastico.tools.routine.RoutineAnswer;
import org.apache.commons.lang3.ArrayUtils;

public class GetRunnersRoutine implements Routine {

  @Override
  public RoutineAnswer execute(HashMap<String, Object> objectMap) throws Exception {
    RunnerConfiguration config = (RunnerConfiguration) objectMap.get("config");
    RunnerData[] data = GithubAPI.getRunners(config);
    ArrayList<RunnerData> toDelete = new ArrayList<>();
    if (data == null) {
      return new RoutineAnswer(true, "github api response not valid");
    }
    for (RunnerData d:data) {
      if (d.getStatus().equals("offline")) {
        if (Application.getConfig().isDeleteOfflineRunnersOnlyWithTag()) {
          toDelete.add(d);
        } else if (Application.getConfig().isDeleteOfflineRunners()) {
          if (ArrayUtils.contains(d.getTags(), Application.getConfig().getDeleteOfflineRunnersWithTag())) {
            toDelete.add(d);
          }
        }
      }
    }
    objectMap.put("toDelete", toDelete.toArray(new RunnerData[0]));
    return new RoutineAnswer(objectMap);
  }

}
