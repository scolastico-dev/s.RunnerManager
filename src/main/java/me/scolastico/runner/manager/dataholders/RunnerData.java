package me.scolastico.runner.manager.dataholders;

public class RunnerData {

  private final long id;
  private final String name;
  private final String os;
  private final String status;
  private final boolean busy;
  private final String[] tags;

  public RunnerData(long id, String name, String os, String status, boolean busy, String[] tags) {
    this.id = id;
    this.name = name;
    this.os = os;
    this.status = status;
    this.busy = busy;
    this.tags = tags;
  }

  public long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getOs() {
    return os;
  }

  public String getStatus() {
    return status;
  }

  public boolean isBusy() {
    return busy;
  }

  public String[] getTags() {
    return tags;
  }

}
