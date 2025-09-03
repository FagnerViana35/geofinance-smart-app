package br.com.org.geofinance.domain.usecase;

import br.com.org.geofinance.app.dto.response.CityInfo;
import br.com.org.geofinance.cross.exception.CitiesExceptionError;
import br.com.org.geofinance.domain.gateway.IbgeGateway;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.NotFoundException;
import lombok.extern.log4j.Log4j2;

import java.util.List;

@Log4j2
@ApplicationScoped
public class CitiesUseCaseImpl implements CitiesUseCase {

    @Inject
    IbgeGateway ibgeGateway;

    @Override
    public CityInfo validateById(int id) {
        return ibgeGateway.findCityById(id)
                .orElseThrow(() -> new NotFoundException("cityId " + id + " não encontrado no IBGE"));
    }

    @Override
    public CityInfo resolveByUfAndName(String uf, String name) {
        if (uf == null || uf.isBlank() || name == null || name.isBlank()) {
            log.error("O valor do Estado (UF) está inválido");
            throw new BadRequestException("Parâmetros uf e name são obrigatórios");
        }
        var list = listMunicipiosByUf(uf);
        var matches = list.stream()
                .filter(c -> c.getName() != null && c.getName().equalsIgnoreCase(name))
                .toList();
        if (matches.isEmpty()) {
            throw new NotFoundException("Município não encontrado: " + uf + "/" + name);
        }
        if (matches.size() > 1) {
            throw new CitiesExceptionError("Mais de um município corresponde");
        }
        return matches.get(0);
    }

    private List<CityInfo> listMunicipiosByUf(String uf) {
        String norm = uf.trim().toUpperCase();
        var cities = ibgeGateway.findCitiesByUf(norm);
        return cities == null ? List.of() : cities;
    }

}
