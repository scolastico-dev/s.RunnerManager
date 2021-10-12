package me.scolastico.runner.manager.routines.scheduler;

import java.util.HashMap;
import me.scolastico.runner.manager.Application;
import me.scolastico.runner.manager.dataholders.RunnerConfiguration;
import me.scolastico.runner.manager.etc.CommandExecuter;
import me.scolastico.runner.manager.etc.Database;
import me.scolastico.tools.handler.ErrorHandler;
import me.scolastico.tools.routine.Routine;
import me.scolastico.tools.routine.RoutineAnswer;
import org.apache.commons.codec.digest.DigestUtils;

public class StartRunnersRoutine implements Routine {

  @Override
  public RoutineAnswer execute(HashMap<String, Object> objectMap) throws Exception {
    RunnerConfiguration config = (RunnerConfiguration) objectMap.get("config");
    int needed = (int) objectMap.get("needed");
    if (needed > 0) {
      for (int i = 0; i < needed; i++) {
        Thread thread = new Thread(() -> {
          try {
            String runnerName =
                DigestUtils.md5Hex(
                    config.getOrg()
                        + ":"
                        + config.getTag()
                        + ":"
                        + Database.getCount()
                        + ":"
                        + Application.getSalt()
                ).toUpperCase();
            System.out.println("Starting runner '" + runnerName + "'...");
            StringBuilder labels = new StringBuilder();
            for (String label:config.getTags()) {
              labels.append(label.replaceAll(" ", "")).append(",");
            }
            if (labels.length() > 0) labels = new StringBuilder(labels.substring(0, labels.length() - 1));
            String command =
                "docker run --rm --name " + runnerName + " "
                    + "-e RUNNER_NAME=\"" + runnerName + "\" "
                    + "-e ACCESS_TOKEN=\"" + config.getGhToken() + "\" "
                    + "-e RUNNER_WORKDIR=\"/tmp/runner/work\" "
                    + "-e RUNNER_GROUP=\"" + config.getGroup() + "\" "
                    + "-e RUNNER_SCOPE=\"org\" "
                    + "-e ORG_NAME=\"" + config.getOrg() + "\" "
                    + (!labels.toString().equals("") ? "-e LABELS=\"" + labels + "\" " : "")
                    + (Application.getConfig().getContainerCPU() > 0 ? "--cpus=\"" + (Application.getConfig().getContainerCPU()/100D) + "\" " : "")
                    + (Application.getConfig().getContainerRAM() > 0 ? "--memory=\"" + Application.getConfig().getContainerRAM() + "m\" " : "")
                    + (Application.getConfig().getContainerSWAP() > 0 ? "--memory-swap=\"" + Application.getConfig().getContainerSWAP() + "m\" " : "")
                    + "myoung34/github-runner:"
                    + config.getTag()
                    + " /ephemeral-runner.sh";
            Database.addFreshRunner(runnerName);
            Database.addLocalRunner(runnerName, config);
            CommandExecuter.run(command);
          } catch (Exception e) {
            ErrorHandler.handle(e);
          }
        });
        thread.start();
      }
    }
    return new RoutineAnswer(objectMap);
  }

}
