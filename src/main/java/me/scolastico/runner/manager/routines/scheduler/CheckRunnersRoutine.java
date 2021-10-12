package me.scolastico.runner.manager.routines.scheduler;

import java.util.ArrayList;
import java.util.HashMap;
import me.scolastico.runner.manager.dataholders.RunnerConfiguration;
import me.scolastico.runner.manager.dataholders.RunnerData;
import me.scolastico.runner.manager.etc.Database;
import me.scolastico.runner.manager.etc.GithubAPI;
import me.scolastico.tools.routine.Routine;
import me.scolastico.tools.routine.RoutineAnswer;

public class CheckRunnersRoutine implements Routine {

  @Override
  public RoutineAnswer execute(HashMap<String, Object> objectMap) throws Exception {
    RunnerConfiguration config = (RunnerConfiguration) objectMap.get("config");
    RunnerData[] data = GithubAPI.getRunners(config);
    if (data == null) {
      System.out.println("GitHub response not valid for configuration '" + config.getOrg() + ":" + config.getTag() + "'!");
      return new RoutineAnswer(true, "github response not valid");
    }
    objectMap.put("data", data);
    int runners = 0;
    int busyRunners = 0;
    ArrayList<String> offlineRunners = new ArrayList<>();
    for (RunnerData d : data) {
      if (Database.getLocalRunners().containsKey(d.getName())) {
        runners++;
        if (Database.getFreshRunners().containsKey(d.getName())) {
          if (d.getStatus().equals("online")) {
            Database.delFreshRunner(d.getName());
          }
        } else if (d.getStatus().equals("offline")) {
          runners--;
          offlineRunners.add(d.getName());
        } else if (d.isBusy()) busyRunners++;
      }
    }
    for (String name:Database.getFreshRunners().keySet()) {
      RunnerConfiguration c = Database.getLocalRunners().get(name);
      if ((config.getTag() + ":" + config.getGroup()).equals(c.getTag() + ":" + config.getGroup())) runners++;
    }
    int free = runners - busyRunners;
    int needed = 0;
    if (free < config.getMin()) needed = config.getMin() - free;
    int toMuch =  runners + needed - config.getMax();
    if (toMuch > 0) if (toMuch > needed) {
      needed = 0;
    } else {
      needed -= toMuch;
    }
    if (needed == 0) {
      if (free > config.getMin()) {
        needed = config.getMin() - free;
      }
    }
    if (needed != 0) {
      System.out.println("Runner '" + config.getOrg() + ":" + config.getTag() + "' needs " + needed + " more runner(s)...");
    }
    objectMap.put("needed", needed);
    objectMap.put("offlineRunners", offlineRunners);
    return new RoutineAnswer(objectMap);
  }

}
