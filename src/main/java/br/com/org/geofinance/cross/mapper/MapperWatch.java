package br.com.org.geofinance.cross.mapper;

import br.com.org.geofinance.app.dto.request.WatchlistCreateRequest;
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
}
