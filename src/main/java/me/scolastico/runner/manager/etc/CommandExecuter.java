package me.scolastico.runner.manager.etc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import me.scolastico.runner.manager.Application;
import org.apache.commons.codec.digest.DigestUtils;
import org.jetbrains.annotations.NotNull;

public class CommandExecuter {

  public static String[] run(String command) throws InterruptedException, IOException {
    if (Application.getConfig().isDebug()) System.out.println("[debug] Executing command: " + command);
    if (!Application.getConfig().isUseBash()) {
      Runtime run = Runtime.getRuntime();
      Process pr = run.exec(command);
      return getStrings(pr);
    } else {
      String hash = DigestUtils.md5Hex(command).toUpperCase();
      File tempScript = File.createTempFile(hash, null);
      Writer streamWriter = new OutputStreamWriter(new FileOutputStream(tempScript));
      PrintWriter printWriter = new PrintWriter(streamWriter);
      printWriter.println("#!/bin/bash");
      printWriter.println(command);
      printWriter.close();
      try {
        ProcessBuilder pb = new ProcessBuilder("bash", tempScript.toString());
        Process process = pb.start();
        return getStrings(process);
      } finally {
        tempScript.delete();
      }
    }
  }

  @NotNull
  private static String[] getStrings(Process process) throws InterruptedException, IOException {
    process.waitFor();
    BufferedReader buf = new BufferedReader(new InputStreamReader(process.getInputStream()));
    ArrayList<String> ret = new ArrayList<>();
    String line = "";
    while ((line=buf.readLine())!=null) {
      ret.add(line);
    }
    return ret.toArray(new String[0]);
  }

}
