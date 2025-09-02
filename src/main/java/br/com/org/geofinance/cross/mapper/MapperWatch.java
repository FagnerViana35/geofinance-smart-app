package br.com.org.geofinance.cross.mapper;

import br.com.org.geofinance.app.dto.request.WatchlistCreateRequest;
import br.com.org.geofinance.app.dto.response.CityInfo;
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
        if (e == null) return null;
        CityInfo city = null;
        if (e.getCityId() != null) {
            city = CityInfo.builder()
                    .id(e.getCityId())
                    .build();
        }
        return WatchlistItemResponse.builder()
                .id(e.getId())
                .symbol(e.getSymbol())
                .cityId(e.getCityId())
                .city(city)
                .targetPrice(e.getTargetPrice())
                .notes(e.getNotes())
                .createdAt(e.getCreatedAt())
                .updatedAt(e.getUpdatedAt())
                .build();
    }

    public WatchlistItemResponse toEnrichedResponse(WatchlistEntity e, CityInfo city) {
        WatchlistItemResponse r = new WatchlistItemResponse();
        r.setId(e.getId());
        r.setSymbol(e.getSymbol());
        r.setCityId(e.getCityId());
        r.setTargetPrice(e.getTargetPrice());
        r.setNotes(e.getNotes());
        r.setCreatedAt(e.getCreatedAt());
        r.setUpdatedAt(e.getUpdatedAt());
        r.setCity(city);
        return r;
    }



}
