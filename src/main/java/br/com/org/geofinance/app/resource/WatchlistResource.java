package br.com.org.geofinance.app.resource;

import br.com.org.geofinance.app.dto.request.WatchlistCreateRequest;
import br.com.org.geofinance.app.dto.request.WatchlistUpdateRequest;
import br.com.org.geofinance.app.dto.response.WatchlistItemEnrichedResponse;
import br.com.org.geofinance.app.dto.response.WatchlistItemResponse;
import br.com.org.geofinance.app.service.WatchlistService;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;

import java.net.URI;
import java.util.List;

@Path("/api/watchlist")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequestScoped
@RunOnVirtualThread
public class WatchlistResource {

    @Inject
    WatchlistService watchlistService;

    @POST
    @Transactional
    @Operation(summary = "Cria um item na watchlist - DADOS DE INVESTIMENTO DE ONDE TEM INVESTIMENTOS")
    @APIResponse(responseCode = "201", description = "Criado",
            content = @Content(schema = @Schema(implementation = WatchlistItemEnrichedResponse.class)))
    public Response create(@Valid WatchlistCreateRequest request, @Context UriInfo uriInfo) {
        WatchlistItemEnrichedResponse created = watchlistService.create(request);
        URI location = uriInfo.getAbsolutePathBuilder().path(String.valueOf(created.getId())).build();
        return Response.created(location).entity(created).build();
    }

    @GET
    @Operation(summary = "Lista itens da watchlist - LISTA TODOS OS INVESTIMENTOS EXISTENTES")
    @APIResponse(responseCode = "200", description = "OK")
    public List<WatchlistItemEnrichedResponse> list(
            @QueryParam("page") @DefaultValue("0") @Min(0) int page,
            @QueryParam("size") @DefaultValue("20") @Min(1) int size) {
        return watchlistService.list(page, size);
    }

    @GET
    @Path("{id}")
    @Operation(summary = "Busca item da watchlist por id - BUSCA INVESTIMENTOS POR ID do INVESTIMENTO")
    @APIResponse(responseCode = "200", description = "OK",
            content = @Content(schema = @Schema(implementation = WatchlistItemEnrichedResponse.class)))
    @APIResponse(responseCode = "404", description = "Não encontrado")
    public WatchlistItemEnrichedResponse getById(@PathParam("id") @Parameter(description = "Identificador do item") Long id) {
        return watchlistService.getById(id);
    }

    @PUT
    @Path("{id}")
    @Transactional
    @Operation(summary = "Atualiza um item da watchlist - ATUALIZA UM INVESTIMENTO - Atualiza o targetprice(preço alvo)")
    @APIResponse(responseCode = "200", description = "Atualizado",
            content = @Content(schema = @Schema(implementation = WatchlistItemEnrichedResponse.class)))
    @APIResponse(responseCode = "404", description = "Não encontrado")
    public WatchlistItemEnrichedResponse update(@PathParam("id") Long id, @Valid WatchlistUpdateRequest request) {
        return watchlistService.update(id, request);
    }

    @DELETE
    @Path("{id}")
    @Transactional
    @Operation(summary = "Remove um item da watchlist - REMOVE UM INVESTIMENTO DA LISTA DE INVESTIMENTO")
    @APIResponse(responseCode = "204", description = "Removido")
    public Response delete(@PathParam("id") Long id) {
        watchlistService.delete(id);
        return Response.noContent().build();
    }

}
