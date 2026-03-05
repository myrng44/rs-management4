package ck4.nvb.rsmanagement.core.module.forecasting.domain.preprocess;

import ck4.nvb.rsmanagement.core.module.forecasting.domain.model.DailySalesPoint;
import ck4.nvb.rsmanagement.core.module.forecasting.domain.model.DailySalesSeries;
import ck4.nvb.rsmanagement.core.module.forecasting.domain.model.StoreProductKey;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class DailySeriesBuilder {

  public DailySalesSeries build(
      StoreProductKey key, Map<LocalDate, Integer> qtyByDate, LocalDate from, LocalDate to) {

    List<DailySalesPoint> points = new ArrayList<>();
    for (LocalDate d = from; !d.isAfter(to); d = d.plusDays(1)) {
      int qty = qtyByDate.getOrDefault(d, 0);
      points.add(new DailySalesPoint(d, qty));
    }
    return new DailySalesSeries(key, points);
  }
}