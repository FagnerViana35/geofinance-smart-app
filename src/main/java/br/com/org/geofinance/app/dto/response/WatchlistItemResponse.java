package br.com.org.geofinance.app.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WatchlistItemResponse {

    private Long id;
    private String symbol;
    private Integer cityId;
    private CityInfo city;
    private BigDecimal targetPrice;
    private String notes;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

}
