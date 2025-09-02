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
public class BrapiQuoteItem {
    private String stock;
    private String name;
    private Double close;
    private Double change;
    private String type;
    private Number market_cap;
    private Long volume;
    private String sector;
    private String logo;
}

