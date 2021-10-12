package me.scolastico.runner.manager.routines.exit;

import java.util.HashMap;
import me.scolastico.runner.manager.dataholders.RunnerConfiguration;
import me.scolastico.runner.manager.etc.CommandExecuter;
import me.scolastico.runner.manager.etc.Database;
import me.scolastico.tools.routine.Routine;
import me.scolastico.tools.routine.RoutineAnswer;

public class StopRunnersRoutine implements Routine {

  @Override
  public RoutineAnswer execute(HashMap<String, Object> objectMap) throws Exception {
    RunnerConfiguration config = (RunnerConfiguration) objectMap.get("config");
    for (String name : Database.getLocalRunners().keySet()) {
      if (Database.getLocalRunners().get(name) == config) {
        CommandExecuter.run("docker rm " + name);
      }
    }
    return new RoutineAnswer(objectMap);
  }

}
