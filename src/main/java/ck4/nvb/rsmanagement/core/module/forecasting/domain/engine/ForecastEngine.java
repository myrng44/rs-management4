package ck4.nvb.rsmanagement.core.module.forecasting.domain.engine;

import ck4.nvb.rsmanagement.core.module.forecasting.domain.model.DailySalesSeries;
import ck4.nvb.rsmanagement.core.module.forecasting.domain.model.ForecastPoint;
import java.util.List;

public interface ForecastEngine {
  List<ForecastPoint> forecast(DailySalesSeries series, ForecastContext context);
}