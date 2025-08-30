package br.com.org.geofinance.app.service;

import br.com.org.geofinance.app.dto.response.TrendInsightResponse;
import br.com.org.geofinance.app.dto.response.VolatilityInsightResponse;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class InsightsServiceImpl implements InsightsService
{


    @Override
    public TrendInsightResponse calculateTrend(String symbol, int shortWindow, int longWindow) {
//        return trendUseCase.execute(symbol, shortWindow, longWindow);
        return null;
    }

    @Override
    public VolatilityInsightResponse calculateVolatility(String symbol, int window) {
        //return volatilityUseCase.execute(symbol, window);
        return null;
    }

}
