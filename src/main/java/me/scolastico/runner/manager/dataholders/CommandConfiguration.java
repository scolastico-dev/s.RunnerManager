package me.scolastico.runner.manager.dataholders;

public class CommandConfiguration {

  private String pull = "docker pull myoung34/github-runner:%tag%";
  private String[] start = {
      "docker run --rm --name %runnerName%",
      "-e RUNNER_NAME=\"%runnerName%\"",
      "-e ACCESS_TOKEN=\"%ghToken%\"",
      "-e RUNNER_WORKDIR=\"/tmp/runner/work\"",
      "-e RUNNER_GROUP=\"%grup%\"",
      "-e RUNNER_SCOPE=\"org\"",
      "-e ORG_NAME=\"%org%\"",
      "%labels%",
      "%cpu%",
      "%ram%",
      "%swap%",
      "myoung34/github-runner:%tag%",
      "/ephemeral-runner.sh"
  };
  private String startLabels = "-e LABELS=\"%labels%\"";
  private String startCPU = "--cpus=\"%cpu%\"";
  private String startRAM = "--memory=\"%ram%m\"";
  private String startSWAP = "--memory-swap=\"%swap%,\"";

  public String getPull() {
    return pull;
  }

  public void setPull(String pull) {
    this.pull = pull;
  }

  public String[] getStart() {
    return start;
  }

  public void setStart(String[] start) {
    this.start = start;
  }

  public String getStartLabels() {
    return startLabels;
  }

  public void setStartLabels(String startLabels) {
    this.startLabels = startLabels;
  }

  public String getStartCPU() {
    return startCPU;
  }

  public void setStartCPU(String startCPU) {
    this.startCPU = startCPU;
  }

  public String getStartRAM() {
    return startRAM;
  }

  public void setStartRAM(String startRAM) {
    this.startRAM = startRAM;
  }

  public String getStartSWAP() {
    return startSWAP;
  }

  public void setStartSWAP(String startSWAP) {
    this.startSWAP = startSWAP;
  }

}
