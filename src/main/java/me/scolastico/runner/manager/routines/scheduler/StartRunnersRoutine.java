package me.scolastico.runner.manager.routines.scheduler;

import java.util.ArrayList;
import java.util.HashMap;
import me.scolastico.runner.manager.Application;
import me.scolastico.runner.manager.dataholders.CommandConfiguration;
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
            CommandConfiguration cmdConfig = Application.getConfig().getCommandConfiguration().get(config.getDockerConfiguration());
            String label = (!labels.toString().equals("") ? cmdConfig.getStartLabels().replaceAll("%labels%", labels.toString()) : "");
            String cpu = "";
            if (config.getContainerCPU() > 0) {
              cpu = cmdConfig.getStartCPU().replaceAll("%cpu%", Integer.toString(config.getContainerCPU()*100));
            } else if (Application.getConfig().getContainerCPU() > 0) {
              cpu = cmdConfig.getStartCPU().replaceAll("%cpu%", Integer.toString(Application.getConfig().getContainerCPU()*100));
            }
            String ram = "";
            if (config.getContainerRAM() > 0) {
              ram = cmdConfig.getStartCPU().replaceAll("%ram%", Integer.toString(config.getContainerRAM()));
            } else if (Application.getConfig().getContainerCPU() > 0) {
              ram = cmdConfig.getStartCPU().replaceAll("%ram%", Integer.toString(Application.getConfig().getContainerRAM()));
            }
            String swap = "";
            if (config.getContainerSWAP() > 0) {
              swap = cmdConfig.getStartCPU().replaceAll("%swap%", Integer.toString(config.getContainerSWAP()));
            } else if (Application.getConfig().getContainerCPU() > 0) {
              swap = cmdConfig.getStartCPU().replaceAll("%swap%", Integer.toString(Application.getConfig().getContainerSWAP()));
            }
            ArrayList<String> commands = new ArrayList<>();
            for (String c:cmdConfig.getStart()) {
              String newCommand = c
                  .replaceAll("%runnerName%", runnerName)
                  .replaceAll("%ghToken%", config.getGhToken())
                  .replaceAll("%grup%", config.getGroup())
                  .replaceAll("%org%", config.getOrg())
                  .replaceAll("%labels%", label)
                  .replaceAll("%cpu%", cpu)
                  .replaceAll("%ram%", ram)
                  .replaceAll("%swap%", swap)
                  .replaceAll("%tag%", config.getTag());
              if (!newCommand.equals("")) commands.add(newCommand);
            }
            StringBuilder commandBuilder = new StringBuilder();
            for (String c:commands) {
              commandBuilder.append(c).append(" ");
            }
            String command = commandBuilder.toString();
            if (command.endsWith(" ")) command = command.substring(0, command.length()-1);
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
