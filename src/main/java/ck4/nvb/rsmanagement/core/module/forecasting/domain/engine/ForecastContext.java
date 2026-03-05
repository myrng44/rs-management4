package ck4.nvb.rsmanagement.core.module.forecasting.domain.engine;

import java.time.LocalDate;

public record ForecastContext(int horizonDays, int seasonLength, LocalDate trainFrom, LocalDate trainTo) {}