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
@Schema(name = "TrendInsightRequest", description = "Parâmetros para cálculo de tendência por médias móveis")
public class TrendInsightRequest {

    @Min(1)
    @Schema(description = "Janela curta (em dias)", example = "7", defaultValue = "7")
    private Integer shortWindow = 7;

    @Min(2)
    @Schema(description = "Janela longa (em dias)", example = "21", defaultValue = "21")
    private Integer longWindow = 21;
}
