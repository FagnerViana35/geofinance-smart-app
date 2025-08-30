package br.com.org.geofinance.app.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WatchlistUpdateRequest {

    @DecimalMin(value = "0.0", inclusive = false, message = "targetPrice deve ser > 0")
    private BigDecimal targetPrice;

    @Size(max = 500, message = "notes deve ter no máximo 500 caracteres")
    private String notes;

}
