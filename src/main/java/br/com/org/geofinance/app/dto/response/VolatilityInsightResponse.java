package br.com.org.geofinance.app.dto.response;

import br.com.org.geofinance.domain.enuns.RiskLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * Resposta: Volatilidade do ativo (GET /insights/asset/{symbol}/volatility)
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VolatilityInsightResponse {
    private String symbol;
    private int window;
    private double volatility;
    private RiskLevel riskLevel;
    private double suggestedStopLossPct;

}
