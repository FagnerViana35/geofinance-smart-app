package br.com.org.geofinance.domain.gateway;

import br.com.org.geofinance.app.dto.response.CityInfo;
import br.com.org.geofinance.app.dto.response.IbgeMunicipioResponse;
import br.com.org.geofinance.infra.restClient.IbgeClient;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.List;
import java.util.Optional;

@Slf4j
@ApplicationScoped
public class IbgeGatewayImpl implements IbgeGateway {
    @Inject
    @RestClient
    IbgeClient client;

    @Override
    public Optional<CityInfo> findCityById(int cityId) {
        try {
            IbgeMunicipioResponse res = client.getMunicipioById(cityId);
            if (res == null || res.getId() == 0) return Optional.empty();
            String uf = res.getMicrorregiao() != null
                    && res.getMicrorregiao().getMesorregiao() != null
                    && res.getMicrorregiao().getMesorregiao().getUf() != null
                    ? res.getMicrorregiao().getMesorregiao().getUf().getSigla()
                    : null;
            log.info("CitiId foi encontrado");
            return Optional.of(new CityInfo(res.getId(), res.getNome(), uf));
        } catch (Exception e) {
            log.error("CitiId não foi encontrado");
            return Optional.empty();
        }
    }

    @Override
    public List<CityInfo> findCitiesByUf(String uf) {
        if (uf == null || uf.isBlank()) return List.of();
        try {
            String norm = uf.trim().toUpperCase();
            List<IbgeMunicipioResponse> list = client.listMunicipiosByUf(norm);
            if (list == null || list.isEmpty()) return List.of();
            return list.stream()
                    .map(res -> {
                        String sigla = res.getMicrorregiao() != null
                                && res.getMicrorregiao().getMesorregiao() != null
                                && res.getMicrorregiao().getMesorregiao().getUf() != null
                                ? res.getMicrorregiao().getMesorregiao().getUf().getSigla()
                                : norm;
                        log.info("CitiId foi encontrado");
                        return new CityInfo(res.getId(), res.getNome(), sigla);
                    })
                    .toList();
        } catch (Exception e) {
            log.error("Cidade não foi encontrada, verifica o {} correto", uf, e);
            return List.of();
        }
    }


}
