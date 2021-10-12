package me.scolastico.runner.manager.etc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class CommandExecuter {

  public static String[] run(String command) throws InterruptedException, IOException {
    Runtime run = Runtime.getRuntime();
    Process pr = run.exec(command);
    pr.waitFor();
    BufferedReader buf = new BufferedReader(new InputStreamReader(pr.getInputStream()));
    ArrayList<String> ret = new ArrayList<>();
    String line = "";
    while ((line=buf.readLine())!=null) {
      ret.add(line);
    }
    return ret.toArray(new String[0]);
  }

}
