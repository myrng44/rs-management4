package ck4.nvb.rsmanagement.core.module.stores.store.service.dto;

import java.util.List;

public class AllStoresRevenueResponse {
  private List<StoreRevenueSeries> stores;
  private Long dayTotal;
  private Long weekTotal;
  private Long monthTotal;
  private String from;
  private String to;

  public AllStoresRevenueResponse() {}

  public AllStoresRevenueResponse(
      List<StoreRevenueSeries> stores,
      Long dayTotal,
      Long weekTotal,
      Long monthTotal,
      String from,
      String to) {
    this.stores = stores;
    this.dayTotal = dayTotal;
    this.weekTotal = weekTotal;
    this.monthTotal = monthTotal;
    this.from = from;
    this.to = to;
  }

  public List<StoreRevenueSeries> getStores() {
    return stores;
  }

  public void setStores(List<StoreRevenueSeries> stores) {
    this.stores = stores;
  }

  public Long getDayTotal() {
    return dayTotal;
  }

  public void setDayTotal(Long dayTotal) {
    this.dayTotal = dayTotal;
  }

  public Long getWeekTotal() {
    return weekTotal;
  }

  public void setWeekTotal(Long weekTotal) {
    this.weekTotal = weekTotal;
  }

  public Long getMonthTotal() {
    return monthTotal;
  }

  public void setMonthTotal(Long monthTotal) {
    this.monthTotal = monthTotal;
  }

  public String getFrom() {
    return from;
  }

  public void setFrom(String from) {
    this.from = from;
  }

  public String getTo() {
    return to;
  }

  public void setTo(String to) {
    this.to = to;
  }
}
