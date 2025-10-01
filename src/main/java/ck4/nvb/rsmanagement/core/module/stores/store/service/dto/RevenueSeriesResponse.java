package ck4.nvb.rsmanagement.core.module.stores.store.service.dto;

import java.util.List;

public class RevenueSeriesResponse {
  private List<RevenuePoint> series;
  private Long day;
  private Long week;
  private Long month;

  public RevenueSeriesResponse() {}

  public RevenueSeriesResponse(List<RevenuePoint> series, Long day, Long week, Long month) {
    this.series = series;
    this.day = day;
    this.week = week;
    this.month = month;
  }

  public List<RevenuePoint> getSeries() {
    return series;
  }

  public void setSeries(List<RevenuePoint> series) {
    this.series = series;
  }

  public Long getDay() {
    return day;
  }

  public void setDay(Long day) {
    this.day = day;
  }

  public Long getWeek() {
    return week;
  }

  public void setWeek(Long week) {
    this.week = week;
  }

  public Long getMonth() {
    return month;
  }

  public void setMonth(Long month) {
    this.month = month;
  }
}
