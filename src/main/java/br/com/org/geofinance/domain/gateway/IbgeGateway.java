package br.com.org.geofinance.domain.gateway;

import br.com.org.geofinance.app.dto.response.CityInfo;

import java.util.List;
import java.util.Optional;

public interface IbgeGateway {
    Optional<CityInfo> findCityById(int cityId);
    List<CityInfo> findCitiesByUf(String uf);

}
