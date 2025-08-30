package br.com.org.geofinance.app.dto.request;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "VolatilityInsightRequest", description = "Parâmetros para cálculo de volatilidade histórica")
public class VolatilityInsightRequest {

    @Min(2)
    @Schema(description = "Janela (em dias)", example = "30", defaultValue = "30")
    private Integer window = 30;
}
