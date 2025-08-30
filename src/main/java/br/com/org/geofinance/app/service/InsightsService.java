package br.com.org.geofinance.app.service;

import br.com.org.geofinance.app.dto.response.TrendInsightResponse;
import br.com.org.geofinance.app.dto.response.VolatilityInsightResponse;

public interface InsightsService {
    TrendInsightResponse calculateTrend(String symbol, int shortWindow, int longWindow);
    VolatilityInsightResponse calculateVolatility(String symbol, int window);

}
