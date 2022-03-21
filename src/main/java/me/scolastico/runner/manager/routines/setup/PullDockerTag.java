package me.scolastico.runner.manager.routines.setup;

import java.util.HashMap;
import me.scolastico.runner.manager.dataholders.CommandConfiguration;
import me.scolastico.runner.manager.dataholders.RunnerConfiguration;
import me.scolastico.runner.manager.etc.CommandExecuter;
import me.scolastico.tools.routine.Routine;
import me.scolastico.tools.routine.RoutineAnswer;

public class PullDockerTag implements Routine {

  @Override
  public RoutineAnswer execute(HashMap<String, Object> objectMap) throws Exception {
    RunnerConfiguration config = (RunnerConfiguration) objectMap.get("config");
    CommandConfiguration cmd = (CommandConfiguration) objectMap.get("cmd");
    String[] response = CommandExecuter.run(cmd.getPull().replaceAll("%tag%", config.getTag()));
    if (response.length <= 1) {
      return new RoutineAnswer(true, "docker response not valid");
    }
    return new RoutineAnswer(objectMap);
  }

}
