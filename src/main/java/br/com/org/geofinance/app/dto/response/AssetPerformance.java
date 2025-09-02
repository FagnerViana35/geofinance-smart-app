package br.com.org.geofinance.app.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class AssetPerformance {
    private  String symbol;
    private  String period;
    private  Double changePercent;
    private  Double changePct;
    private  Integer dataPoints;
    private  Double  close;
    private  String sector;
    private String name;
    private String currency;
}
