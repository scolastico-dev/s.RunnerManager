package me.scolastico.runner.manager.routines.setup;

import java.util.HashMap;
import me.scolastico.runner.manager.Application;
import me.scolastico.runner.manager.dataholders.CommandConfiguration;
import me.scolastico.runner.manager.dataholders.RunnerConfiguration;
import me.scolastico.runner.manager.etc.Database;
import me.scolastico.tools.routine.Routine;
import me.scolastico.tools.routine.RoutineAnswer;

public class CheckConfigNameTagRoutine implements Routine {

  @Override
  public RoutineAnswer execute(HashMap<String, Object> objectMap) throws Exception {
    RunnerConfiguration config = (RunnerConfiguration) objectMap.get("config");
    if (!Database.addAndCheckConfigName(config.getOrg() + ":" + config.getTag())) {
      return new RoutineAnswer(true, "runner configuration exists twice");
    }
    if (!Application.getConfig().getCommandConfiguration().containsKey(config.getDockerConfiguration())) {
      return new RoutineAnswer(true, "missing docker command configuration");
    }
    CommandConfiguration cmdConfig = Application.getConfig().getCommandConfiguration().get(config.getDockerConfiguration());
    objectMap.put("cmd", cmdConfig);
    return new RoutineAnswer(objectMap);
  }

}
