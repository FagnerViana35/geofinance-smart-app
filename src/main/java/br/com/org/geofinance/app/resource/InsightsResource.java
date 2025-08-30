package br.com.org.geofinance.app.resource;

import br.com.org.geofinance.app.dto.request.TrendInsightRequest;
import br.com.org.geofinance.app.dto.request.VolatilityInsightRequest;
import br.com.org.geofinance.app.dto.response.TrendInsightResponse;
import br.com.org.geofinance.app.dto.response.VolatilityInsightResponse;
import br.com.org.geofinance.app.service.InsightsService;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;

@Path("/api/insights")
@Produces(MediaType.APPLICATION_JSON)
@RequestScoped
@RunOnVirtualThread
public class InsightsResource {

    @Inject
    InsightsService insightsService;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/asset/{symbol}/trend")
    @Operation(summary = "Calcula tendência por médias móveis")
    @APIResponse(responseCode = "200", description = "OK",
            content = @Content(schema = @Schema(implementation = TrendInsightResponse.class)))
    public TrendInsightResponse trend(
            @PathParam("symbol") String symbol,
            @RequestBody(required = true, content = @Content(schema = @Schema(implementation = TrendInsightRequest.class)))
            @Valid TrendInsightRequest request) {
        int shortWindow = request.getShortWindow() != null ? request.getShortWindow() : 7;
        int longWindow = request.getLongWindow() != null ? request.getLongWindow() : 21;
        return insightsService.calculateTrend(symbol, shortWindow, longWindow);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/asset/{symbol}/volatility")
    @Operation(summary = "Calcula volatilidade histórica e nível de risco")
    @APIResponse(responseCode = "200", description = "OK",
            content = @Content(schema = @Schema(implementation = VolatilityInsightResponse.class)))
    public VolatilityInsightResponse volatility(
            @PathParam("symbol") String symbol,
            @RequestBody(required = true, content = @Content(schema = @Schema(implementation = VolatilityInsightRequest.class)))
            @Valid VolatilityInsightRequest request) {
        int window = request.getWindow() != null ? request.getWindow() : 30;
        return insightsService.calculateVolatility(symbol, window);
    }

}
