package ck4.nvb.rsmanagement.core.module.forecasting.domain.preprocess;

import ck4.nvb.rsmanagement.core.module.forecasting.domain.model.DailySalesSeries;
import org.springframework.stereotype.Component;

@Component
public class SeriesValidation {

  public void validate(DailySalesSeries series, int seasonLength) {
    int n = series.points() == null ? 0 : series.points().size();
    // Rule of thumb: >= 2 seasons to learn weekly seasonality
    int min = Math.max(14, seasonLength * 2);
    if (n < min) {
      throw new IllegalArgumentException("Not enough history points: " + n + ", required >= " + min);
    }
  }
}