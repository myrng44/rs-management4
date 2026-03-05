package ck4.nvb.rsmanagement.core.module.forecasting.infrastructure.engine;

import ck4.nvb.rsmanagement.core.module.forecasting.domain.engine.ForecastEngine;
import ck4.nvb.rsmanagement.core.module.forecasting.domain.engine.ForecastEngineFactory;
import ck4.nvb.rsmanagement.core.module.forecasting.domain.model.EngineType;
import ck4.nvb.rsmanagement.core.module.forecasting.infrastructure.engine.ets.EtsForecastEngineImpl;
import ck4.nvb.rsmanagement.core.module.forecasting.infrastructure.engine.sarima.SarimaForecastEngineImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ForecastEngineFactoryImpl implements ForecastEngineFactory {

  private final EtsForecastEngineImpl ets;
  private final SarimaForecastEngineImpl sarima;

  @Override
  public ForecastEngine getEngine(EngineType type) {
    if (type == null) return ets;
    return switch (type) {
      case ETS -> ets;
      case SARIMA -> sarima;
    };
  }
}