package ck4.nvb.rsmanagement.core.module.forecasting.infrastructure.engine.sarima;

import ck4.nvb.rsmanagement.core.module.forecasting.domain.engine.ForecastContext;
import ck4.nvb.rsmanagement.core.module.forecasting.domain.engine.ForecastEngine;
import ck4.nvb.rsmanagement.core.module.forecasting.domain.model.DailySalesSeries;
import ck4.nvb.rsmanagement.core.module.forecasting.domain.model.ForecastPoint;
import ck4.nvb.rsmanagement.core.module.forecasting.infrastructure.engine.baseline.SeasonalNaiveForecastEngineImpl;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SarimaForecastEngineImpl implements ForecastEngine {

  private final SeasonalNaiveForecastEngineImpl fallback;

  @Override
  public List<ForecastPoint> forecast(DailySalesSeries series, ForecastContext context) {
    // TODO: implement SARIMA later. For now, use stable baseline.
    return fallback.forecast(series, context);
  }
}