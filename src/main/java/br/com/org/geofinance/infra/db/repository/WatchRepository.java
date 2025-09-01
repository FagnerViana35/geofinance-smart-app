package br.com.org.geofinance.infra.db.repository;

import br.com.org.geofinance.app.dto.request.WatchlistUpdateRequest;
import br.com.org.geofinance.app.dto.response.WatchlistItemResponse;
import br.com.org.geofinance.cross.mapper.MapperWatch;
import br.com.org.geofinance.infra.db.model.WatchlistEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.time.OffsetDateTime;
import java.util.Optional;

@ApplicationScoped
public class WatchRepository implements PanacheRepository<WatchlistEntity> {

    @Inject
    MapperWatch mapperWatch;

    public Optional<WatchlistItemResponse> update(Long id, WatchlistUpdateRequest request) {
        StringBuilder jpql = new StringBuilder("updatedAt = :updatedAt");
        Parameters params = Parameters.with("updatedAt", OffsetDateTime.now())
                .and("id", id);

        if (request.getTargetPrice() != null) {
            jpql.append(", targetPrice = :targetPrice");
            params = params.and("targetPrice", request.getTargetPrice());
        }
        if (request.getNotes() != null) {
            jpql.append(", notes = :notes");
            params = params.and("notes", request.getNotes());
        }

        if (jpql.toString().equals("updatedAt = :updatedAt")) {
            // Nada para atualizar
            WatchlistEntity current = findById(id);
            return Optional.ofNullable(current).map(mapperWatch::toResponse);
        }

        long rows = update(jpql.append(" where id = :id").toString(), params);
        if (rows == 0) return Optional.empty();

        WatchlistEntity refreshed = findById(id);
        return Optional.of(mapperWatch.toResponse(refreshed));
    }


    public boolean deleteById(Long id) {
        long rows = delete("id = :id", Parameters.with("id", id));
        return rows > 0;
    }
    public java.math.BigDecimal sumAllTargetPrice() {
        var result = getEntityManager()
                .createQuery("select coalesce(sum(w.targetPrice),0) from WatchlistEntity w", java.math.BigDecimal.class)
                .getSingleResult();
        return result == null ? java.math.BigDecimal.ZERO : result;
    }

    public java.math.BigDecimal sumTargetPriceByCity(Integer cityId) {
        if (cityId == null) return java.math.BigDecimal.ZERO;
        var result = getEntityManager()
                .createQuery("select coalesce(sum(w.targetPrice),0) from WatchlistEntity w where w.cityId = :cityId", java.math.BigDecimal.class)
                .setParameter("cityId", cityId)
                .getSingleResult();
        return result == null ? java.math.BigDecimal.ZERO : result;
    }
}
