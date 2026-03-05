package ck4.nvb.rsmanagement.core.module.forecasting.infrastructure.engine.baseline;

import ck4.nvb.rsmanagement.core.module.forecasting.domain.engine.ForecastContext;
import ck4.nvb.rsmanagement.core.module.forecasting.domain.engine.ForecastEngine;
import ck4.nvb.rsmanagement.core.module.forecasting.domain.model.DailySalesSeries;
import ck4.nvb.rsmanagement.core.module.forecasting.domain.model.ForecastPoint;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class SeasonalNaiveForecastEngineImpl implements ForecastEngine {

  @Override
  public List<ForecastPoint> forecast(DailySalesSeries series, ForecastContext context) {
    int horizon = context.horizonDays();
    int season = context.seasonLength();

    var points = series.points();
    int n = points.size();
    if (n < season) {
      throw new IllegalArgumentException("Series too short for seasonal naive: n=" + n + ", season=" + season);
    }

    LocalDate lastDate = points.get(n - 1).date();
    List<ForecastPoint> out = new ArrayList<>(horizon);

    // repeat the last season pattern
    for (int i = 1; i <= horizon; i++) {
      int idx = n - season + ((i - 1) % season);
      double yhat = points.get(idx).qty();
      out.add(new ForecastPoint(lastDate.plusDays(i), yhat));
    }
    return out;
  }
}