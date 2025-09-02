package br.com.org.geofinance.app.resource;

import br.com.org.geofinance.app.dto.response.BrapiQuoteItem;
import br.com.org.geofinance.app.service.BrapiService;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.constraints.Min;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;

import java.util.List;

@Path("/api/symbols")
@Produces(MediaType.APPLICATION_JSON)
@RequestScoped
@RunOnVirtualThread
public class BrapiResource {

    @Inject
    BrapiService brapiService;

    @GET
    @Path("/search")
    @Operation(summary = "Busca símbolos - STOCK - por nome/ticker no brapi.dev")
    public List<BrapiQuoteItem> search(
            @QueryParam("query") @Parameter(description = "Texto para busca (ex.: PETR)") String query,
            @QueryParam("limit") @DefaultValue("50") @Min(1) int limit
    ) {
        return brapiService.searchSymbols(query, limit);
    }

    @GET
    @Operation(summary = "Lista de código símbolos - STOCK - (paginado) do brapi.dev")
    public List<BrapiQuoteItem> list(
            @QueryParam("page") @DefaultValue("1") @Min(1) int page,
            @QueryParam("limit") @DefaultValue("50") @Min(1) int limit,
            @QueryParam("sortBy") @DefaultValue("close") String sortBy,
            @QueryParam("sortOrder") @DefaultValue("desc") String sortOrder
    ) {
        return brapiService.listSymbols(page, limit, sortBy, sortOrder);
    }

}
