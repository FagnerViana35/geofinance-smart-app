package br.com.org.geofinance.domain.usecase;

import br.com.org.geofinance.app.dto.response.CityInfo;

import java.util.List;

public interface CitiesUseCase {

    CityInfo validateById(int id); // 404 se não existir
    CityInfo resolveByUfAndName(String uf, String name); // 404 se não achar, 409 se múltiplos

}
