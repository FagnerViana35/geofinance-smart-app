package br.com.org.geofinance.app.dto.response;

import br.com.org.geofinance.domain.enuns.TrendSignal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrendInsightResponse {
    private String symbol;
    private int shortWindow;
    private int longWindow;
    private TrendSignal signal;
    private double shortMA;
    private double longMA;
    private double recentChangePct7d;

}
