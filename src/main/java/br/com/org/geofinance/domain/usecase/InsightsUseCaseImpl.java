package br.com.org.geofinance.domain.usecase;

import br.com.org.geofinance.app.dto.response.TrendInsightResponse;
import br.com.org.geofinance.app.dto.response.VolatilityInsightResponse;
import br.com.org.geofinance.domain.enuns.RiskLevel;
import br.com.org.geofinance.domain.enuns.TrendSignal;
import br.com.org.geofinance.domain.gateway.FinancialDataGateway;
import jakarta.inject.Inject;
import jakarta.ws.rs.BadRequestException;

import java.util.ArrayList;
import java.util.List;

public class InsightsUseCaseImpl implements InsightsUseCase {

    @Inject
    FinancialDataGateway financialGateway;

    @Inject
    public InsightsUseCaseImpl(FinancialDataGateway financialGateway) {
        this.financialGateway = financialGateway;
    }

    @Override
    public TrendInsightResponse calculateTrend(String symbol, int shortWindow, int longWindow) {
        if (shortWindow <= 0 || longWindow <= 1 || shortWindow >= longWindow) {
            throw new BadRequestException("Parâmetros de janela inválidos");
        }
        int required = Math.max(longWindow, 21);
        List<Double> closesAsc = financialGateway.getDailyCloses(symbol, required + 7);

        if (closesAsc == null || closesAsc.size() < longWindow) {
            throw new BadRequestException("Dados insuficientes para calcular tendência");
        }

        double shortMA = simpleMA(closesAsc, shortWindow);
        double longMA = simpleMA(closesAsc, longWindow);
        TrendSignal signal = shortMA > longMA ? TrendSignal.BULLISH
                : shortMA < longMA ? TrendSignal.BEARISH
                : TrendSignal.SIDEWAYS;

        double recentChangePct7d = calcChangePct7d(closesAsc);

        return new TrendInsightResponse(symbol, shortWindow, longWindow, signal, shortMA, longMA, recentChangePct7d);
    }

    @Override
    public VolatilityInsightResponse calculateVolatility(String symbol, int window) {
        if (window < 2) throw new BadRequestException("Window deve ser >= 2");

        List<Double> closesAsc = financialGateway.getDailyCloses(symbol, window + 1);
        if (closesAsc == null || closesAsc.size() < window) {
            throw new BadRequestException("Dados insuficientes para calcular volatilidade");
        }

        double vol = computeVolatility(closesAsc, window);
        RiskLevel level = classifyRisk(vol);
        double suggestedStop = Math.round(vol * 30.0 * 100.0) / 100.0;

        return new VolatilityInsightResponse(symbol, window, vol, level, suggestedStop);
    }

    // Helpers

    private double simpleMA(List<Double> closesAsc, int window) {
        int n = closesAsc.size();
        if (n < window) throw new BadRequestException("Janela maior que série disponível");
        double sum = 0.0;
        for (int i = n - window; i < n; i++) {
            sum += closesAsc.get(i);
        }
        return sum / window;
    }

    private double calcChangePct7d(List<Double> closesAsc) {
        int n = closesAsc.size();
        if (n < 8) return 0.0;
        double last = closesAsc.get(n - 1);
        double prev7 = closesAsc.get(n - 8);
        if (prev7 == 0) return 0.0;
        return (last - prev7) / prev7 * 100.0;
    }

    private double computeVolatility(List<Double> closesAsc, int window) {
        int n = closesAsc.size();
        int start = n - window;
        List<Double> returns = new ArrayList<>(window - 1);
        for (int i = start + 1; i < n; i++) {
            double p0 = closesAsc.get(i - 1);
            double p1 = closesAsc.get(i);
            if (p0 <= 0 || p1 <= 0) continue;
            returns.add(Math.log(p1 / p0));
        }
        if (returns.size() < 2) return 0.0;

        double mean = returns.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
        double var = returns.stream().mapToDouble(r -> (r - mean) * (r - mean)).sum() / (returns.size() - 1);
        return Math.sqrt(var);
    }

    private RiskLevel classifyRisk(double vol) {
        if (vol < 0.15) return RiskLevel.BAIXA;
        if (vol < 0.30) return RiskLevel.MEDIA;
        return RiskLevel.ALTA;
    }

}
