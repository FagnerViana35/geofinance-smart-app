package br.com.org.geofinance.cross.mapper;

import br.com.org.geofinance.app.dto.request.WatchlistCreateRequest;
import br.com.org.geofinance.app.dto.response.CityInfo;
import br.com.org.geofinance.app.dto.response.WatchlistItemEnrichedResponse;
import br.com.org.geofinance.app.dto.response.WatchlistItemResponse;
import br.com.org.geofinance.infra.db.model.WatchlistEntity;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MapperWatch {

    public WatchlistEntity toEntity(WatchlistCreateRequest r) {
        if (r == null) return null;
        return WatchlistEntity.builder()
                .symbol(r.getSymbol())
                .cityId(r.getCityId())
                .targetPrice(r.getTargetPrice())
                .notes(r.getNotes())
                .build();
    }

    // Mantém o DTO "base" (sem enriquecimento)
    public WatchlistItemResponse toResponse(WatchlistEntity e) {
        return new WatchlistItemResponse(
                e.getId(),
                e.getSymbol(),
                e.getCityId(),
                e.getTargetPrice(),
                e.getNotes(),
                e.getCreatedAt(),
                e.getUpdatedAt()
        );
    }

    // Novo: mapeia uma resposta "enriquecida" com dados do IBGE já resolvidos no serviço
    public WatchlistItemEnrichedResponse toEnrichedResponse(WatchlistEntity e, CityInfo city) {
        WatchlistItemEnrichedResponse r = new WatchlistItemEnrichedResponse();
        r.setId(e.getId());
        r.setSymbol(e.getSymbol());
        r.setCityId(e.getCityId());
        r.setTargetPrice(e.getTargetPrice());
        r.setNotes(e.getNotes());
        r.setCreatedAt(e.getCreatedAt());
        r.setUpdatedAt(e.getUpdatedAt());
        r.setCity(city); // pode ser null se IBGE estiver indisponível no GET
        return r;
    }

}
