package ck4.nvb.rsmanagement.core.module.forecasting.domain.engine;

import ck4.nvb.rsmanagement.core.module.forecasting.domain.model.EngineType;

public interface ForecastEngineFactory {
  ForecastEngine getEngine(EngineType type);
}