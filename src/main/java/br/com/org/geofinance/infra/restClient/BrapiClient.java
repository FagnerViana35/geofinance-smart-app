package br.com.org.geofinance.infra.restClient;

import br.com.org.geofinance.app.dto.response.BrapiQuoteListResponse;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(configKey = "brapi")
@Path("/api/quote")
@Produces(MediaType.APPLICATION_JSON)
public interface BrapiClient {
    // Lista completa (paginada) - aceita filtros/sort opcionais
    @GET
    @Path("/list")
    BrapiQuoteListResponse list(
            @QueryParam("page") @DefaultValue("1") int page,
            @QueryParam("limit") @DefaultValue("50") int limit,
            @QueryParam("sortBy") @DefaultValue("close") String sortBy,
            @QueryParam("sortOrder") @DefaultValue("desc") String sortOrder
    );

    // Busca por nome/ticker
    @GET
    @Path("/list")
    BrapiQuoteListResponse search(
            @QueryParam("search") String search,
            @QueryParam("limit") @DefaultValue("50") int limit
    );

}
