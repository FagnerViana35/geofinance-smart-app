package br.com.org.geofinance.app.resource;

import br.com.org.geofinance.app.dto.response.CityInfo;
import br.com.org.geofinance.app.service.CitiesService;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.Operation;

import java.util.List;

@Path("/api/cities")
@Produces(MediaType.APPLICATION_JSON)
@RequestScoped
@RunOnVirtualThread
public class CitiesResource {

    @Inject
    CitiesService citiesService;

    @GET
    @Path("/{id}")
    @Operation(summary = "Valida cityId no IBGE")
    public CityInfo getById(@PathParam("id") int id) {
        return citiesService.validateById(id);
    }

    @GET
    @Path("/resolve")
    @Operation(summary = "Resolve cityId por UF + nome do município")
    public CityInfo resolve(@QueryParam("uf") String uf, @QueryParam("name") String name) {
        if (uf == null || uf.isBlank() || name == null || name.isBlank()) {
            throw new BadRequestException("Parâmetros uf e name são obrigatórios");
        }
        return citiesService.resolveByUfAndName(uf, name);
    }

}
