package br.com.org.geofinance.app.resource;

import br.com.org.geofinance.app.dto.response.TrendInsightResponse;
import br.com.org.geofinance.app.dto.response.VolatilityInsightResponse;
import br.com.org.geofinance.app.service.InsightsService;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.constraints.Min;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;

@Path("/api/insights")
@Produces(MediaType.APPLICATION_JSON)
@RequestScoped
@RunOnVirtualThread
public class InsightsResource {

    @Inject
    InsightsService insightsService;

    @GET
    @Path("/asset/{symbol}/trend")
    @Operation(summary = "Calcula tendência por médias móveis")
    @APIResponse(responseCode = "200", description = "OK",
            content = @Content(schema = @Schema(implementation = TrendInsightResponse.class)))
    public TrendInsightResponse trend(
            @PathParam("symbol") @Parameter(description = "Símbolo do ativo") String symbol,
            @QueryParam("shortWindow") @DefaultValue("7") @Min(1) int shortWindow,
            @QueryParam("longWindow") @DefaultValue("21") @Min(2) int longWindow) {
        return insightsService.calculateTrend(symbol, shortWindow, longWindow);
    }

    @GET
    @Path("/asset/{symbol}/volatility")
    @Operation(summary = "Calcula volatilidade histórica e nível de risco")
    @APIResponse(responseCode = "200", description = "OK",
            content = @Content(schema = @Schema(implementation = VolatilityInsightResponse.class)))
    public VolatilityInsightResponse volatility(
            @PathParam("symbol") String symbol,
            @QueryParam("window") @DefaultValue("30") @Min(2) int window) {
        return insightsService.calculateVolatility(symbol, window);
    }

}
