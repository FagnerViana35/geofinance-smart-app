package br.com.org.geofinance.infra.restClient;

import br.com.org.geofinance.app.dto.response.IbgeMunicipioResponse;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import java.util.List;

@RegisterRestClient(configKey = "ibge")
@Path("/api/v1/localidades")
public interface IbgeClient {

    @GET
    @Path("/municipios/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    IbgeMunicipioResponse getMunicipioById(@PathParam("id") int id);

    @GET
    @Path("/estados/{uf}/municipios")
    @Produces(MediaType.APPLICATION_JSON)
    List<IbgeMunicipioResponse> listMunicipiosByUf(@PathParam("uf") String uf);


}
