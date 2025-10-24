package dev.f5.ideathon.model;

public class TempDay {
  private String date;
  private int tMin;
  private int tMax;

  public TempDay(String date, int tMin, int tMax) {
    this.date = date;
    this.tMin = tMin;
    this.tMax = tMax;
  }

  public String getDate() {
    return date;
  }

  public int gettMin() {
    return tMin;
  }

  public int gettMax() {
    return tMax;
  }
}
