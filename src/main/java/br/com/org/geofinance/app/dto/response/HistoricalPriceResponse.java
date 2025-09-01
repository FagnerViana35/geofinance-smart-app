package br.com.org.geofinance.app.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HistoricalPriceResponse {
    private String symbol;
    private String date;
    private double price;
}
