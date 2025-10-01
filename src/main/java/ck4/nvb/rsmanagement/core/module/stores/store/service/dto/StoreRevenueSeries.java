package ck4.nvb.rsmanagement.core.module.stores.store.service.dto;

import java.util.List;

public class StoreRevenueSeries {
  private Long storeId;
  private String storeName;
  private List<RevenuePoint> series;

  public StoreRevenueSeries() {}

  public StoreRevenueSeries(Long storeId, String storeName, List<RevenuePoint> series) {
    this.storeId = storeId;
    this.storeName = storeName;
    this.series = series;
  }

  public Long getStoreId() {
    return storeId;
  }

  public void setStoreId(Long storeId) {
    this.storeId = storeId;
  }

  public String getStoreName() {
    return storeName;
  }

  public void setStoreName(String storeName) {
    this.storeName = storeName;
  }

  public List<RevenuePoint> getSeries() {
    return series;
  }

  public void setSeries(List<RevenuePoint> series) {
    this.series = series;
  }
}
