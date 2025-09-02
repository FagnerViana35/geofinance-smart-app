package br.com.org.geofinance.app.resource;

import br.com.org.geofinance.app.dto.response.AssetPerformance;
import br.com.org.geofinance.app.service.AssertPerformersService;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.Operation;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Path("/api/insights")
@Produces(MediaType.APPLICATION_JSON)
@RequestScoped
@RunOnVirtualThread
public class AssetAnalyticsResource {

    @Inject
    AssertPerformersService service;

    @GET
    @Path("/top-performers")
    @Operation(summary = "Rankeia Investimentos que rende mais")
    public List<AssetPerformance> top(
            @QueryParam("period") @DefaultValue("30d") String period,
            @QueryParam("size") @DefaultValue("10") int size,
            @QueryParam("riskAdjusted") @DefaultValue("false") boolean riskAdjusted,
            @QueryParam("symbols") String symbolsCsv,
            @QueryParam("includeDividends") @DefaultValue("false") boolean includeDividends
    ) {
        List<String> symbols = (symbolsCsv == null || symbolsCsv.isBlank())
                ? List.of()
                : Arrays.stream(symbolsCsv.split(","))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .map(String::toUpperCase)
                    .distinct()
                    .collect(Collectors.toList());

        return service.rankTopPerformers(symbols, period, size, riskAdjusted, includeDividends);
    }

}
