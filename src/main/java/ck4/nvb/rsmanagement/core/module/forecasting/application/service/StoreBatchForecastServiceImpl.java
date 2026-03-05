package ck4.nvb.rsmanagement.core.module.forecasting.application.service;

import ck4.nvb.rsmanagement.core.module.forecasting.api.dto.request.StoreBatchForecastRequest;
import ck4.nvb.rsmanagement.core.module.forecasting.api.dto.response.ForecastPointResponse;
import ck4.nvb.rsmanagement.core.module.forecasting.api.dto.response.StoreBatchForecastResponse;
import ck4.nvb.rsmanagement.core.module.forecasting.api.dto.response.StoreProductForecastResponse;
import ck4.nvb.rsmanagement.core.module.forecasting.application.port.out.ClockPort;
import ck4.nvb.rsmanagement.core.module.forecasting.application.port.out.ForecastPointStorePort;
import ck4.nvb.rsmanagement.core.module.forecasting.application.port.out.ForecastRunStorePort;
import ck4.nvb.rsmanagement.core.module.forecasting.application.port.out.IdGeneratorPort;
import ck4.nvb.rsmanagement.core.module.forecasting.application.port.out.StoreSalesSeriesPort;
import ck4.nvb.rsmanagement.core.module.forecasting.domain.engine.ForecastContext;
import ck4.nvb.rsmanagement.core.module.forecasting.domain.engine.ForecastEngine;
import ck4.nvb.rsmanagement.core.module.forecasting.domain.engine.ForecastEngineFactory;
import ck4.nvb.rsmanagement.core.module.forecasting.domain.model.ForecastPoint;
import ck4.nvb.rsmanagement.core.module.forecasting.domain.model.StoreProductKey;
import ck4.nvb.rsmanagement.core.module.forecasting.domain.preprocess.DailySeriesBuilder;
import ck4.nvb.rsmanagement.core.module.forecasting.domain.preprocess.SeriesValidation;
import java.time.Instant;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StoreBatchForecastServiceImpl implements StoreBatchForecastService {

  private final StoreSalesSeriesPort storeSalesSeriesPort;
  private final ForecastEngineFactory forecastEngineFactory;
  private final DailySeriesBuilder dailySeriesBuilder;
  private final SeriesValidation seriesValidation;

  private final ForecastRunStorePort forecastRunStorePort;
  private final ForecastPointStorePort forecastPointStorePort;

  private final IdGeneratorPort idGeneratorPort;
  private final ClockPort clockPort;

  @Value("${forecasting.historyDays:180}")
  private int historyDays;

  @Value("${forecasting.horizonDays:7}")
  private int horizonDays;

  @Value("${forecasting.seasonLength:7}")
  private int seasonLength;

  @Override
  @Transactional
  public StoreBatchForecastResponse forecastNextWeek(Long storeId, StoreBatchForecastRequest request) {
    Objects.requireNonNull(storeId, "storeId must not be null");
    Objects.requireNonNull(request, "request must not be null");
    if (request.getProductIds() == null || request.getProductIds().isEmpty()) {
      throw new IllegalArgumentException("productIds must not be empty");
    }

    LocalDate today = clockPort.today();
    LocalDate trainFrom = today.minusDays(historyDays - 1L); // gômf today
    LocalDate trainTo = today;

    long runId = idGeneratorPort.nextId();
    Instant generatedAt = clockPort.nowInstant();

    // Load aggregated daily sales for (storeId, productId) in [trainFrom..trainTo]
    Map<StoreProductKey, Map<LocalDate, Integer>> rawDaily =
        storeSalesSeriesPort.loadDailySales(storeId, request.getProductIds(), trainFrom, trainTo);

    ForecastContext ctx = new ForecastContext(horizonDays, seasonLength, trainFrom, trainTo);

    ForecastEngine engine =
        forecastEngineFactory.getEngine(
            switch (request.getEngineType()) {
              case ETS -> ck4.nvb.rsmanagement.core.module.forecasting.domain.model.EngineType.ETS;
              case SARIMA -> ck4.nvb.rsmanagement.core.module.forecasting.domain.model.EngineType.SARIMA;
            });

    // Save run metadata first (status can be inferred or updated later if you add it)
    forecastRunStorePort.saveRun(runId, storeId, request.getEngineType().name(), historyDays, horizonDays, seasonLength, generatedAt, trainFrom, trainTo);

    List<StoreProductForecastResponse> results = new ArrayList<>(request.getProductIds().size());

    for (Long productId : request.getProductIds()) {
      StoreProductKey key = new StoreProductKey(storeId, productId);

      StoreProductForecastResponse r = new StoreProductForecastResponse();
      r.setProductId(productId);

      try {
        // Build dense daily series with missing days filled as 0
        var series = dailySeriesBuilder.build(key, rawDaily.getOrDefault(key, Map.of()), trainFrom, trainTo);

        seriesValidation.validate(series, seasonLength);

        List<ForecastPoint> forecast = engine.forecast(series, ctx);

        // persist forecast points (store double)
        forecastPointStorePort.savePoints(runId, storeId, productId, forecast);

        // map response with rounding
        List<ForecastPointResponse> points =
            forecast.stream()
                .map(p -> new ForecastPointResponse(p.date(), roundNonNegative(p.yhat())))
                .collect(Collectors.toList());

        r.setStatus("SUCCESS");
        r.setMessage(null);
        r.setPoints(points);

      } catch (Exception ex) {
        r.setStatus("FAILED");
        r.setMessage(ex.getMessage());
        r.setPoints(List.of());
      }

      results.add(r);
    }

    StoreBatchForecastResponse response = new StoreBatchForecastResponse();
    response.setRunId(runId);
    response.setStoreId(storeId);
    response.setEngineType(request.getEngineType().name());
    response.setHistoryDays(historyDays);
    response.setHorizonDays(horizonDays);
    response.setGeneratedAt(generatedAt);
    response.setResults(results);
    return response;
  }

  private static int roundNonNegative(double value) {
    long rounded = Math.round(value);
    if (rounded < 0) return 0;
    if (rounded > Integer.MAX_VALUE) return Integer.MAX_VALUE;
    return (int) rounded;
  }
}