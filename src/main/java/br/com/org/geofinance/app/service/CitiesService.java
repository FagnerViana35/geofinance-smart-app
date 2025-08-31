package br.com.org.geofinance.app.service;

import br.com.org.geofinance.app.dto.response.CityInfo;
import br.com.org.geofinance.domain.usecase.CitiesUseCase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class CitiesService {

    @Inject
    CitiesUseCase useCase;

    public CityInfo validateById(int id) { return useCase.validateById(id); }
    public CityInfo resolveByUfAndName(String uf, String name) { return useCase.resolveByUfAndName(uf, name); }

}
