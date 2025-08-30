package br.com.org.geofinance.domain.usecase;

import br.com.org.geofinance.app.dto.response.TrendInsightResponse;
import br.com.org.geofinance.app.dto.response.VolatilityInsightResponse;

public interface InsightsUseCase {
    TrendInsightResponse calculateTrend(String symbol, int shortWindow, int longWindow);
    VolatilityInsightResponse calculateVolatility(String symbol, int window);

}
