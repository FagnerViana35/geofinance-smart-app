package br.com.org.geofinance.app.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WatchlistCreateRequest {

    @NotBlank(message = "symbol é obrigatório")
    private String symbol;

    @NotNull(message = "cityId é obrigatório")
    @Positive(message = "cityId deve ser positivo")
    private Integer cityId;

    @NotNull(message = "targetPrice é obrigatório")
    @DecimalMin(value = "0.0", inclusive = false, message = "targetPrice deve ser > 0")
    private BigDecimal targetPrice;

    @Size(max = 500, message = "notes deve ter no máximo 500 caracteres")
    private String notes;

}
