package br.com.org.geofinance.domain.usecase;

import br.com.org.geofinance.app.dto.request.WatchlistCreateRequest;
import br.com.org.geofinance.app.dto.request.WatchlistUpdateRequest;
import br.com.org.geofinance.app.dto.response.WatchlistItemEnrichedResponse;
import br.com.org.geofinance.app.dto.response.WatchlistItemResponse;
import br.com.org.geofinance.cross.mapper.MapperWatch;
import br.com.org.geofinance.domain.gateway.IbgeGateway;
import br.com.org.geofinance.infra.db.repository.WatchRepository;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;

import java.util.List;
@ApplicationScoped
public class WatchUseCaseImpl implements WatchUseCase {
    @Inject
    WatchRepository repository;

    @Inject
    MapperWatch mapperWatch;

    @Inject
    IbgeGateway ibgeGateway;


    @Override
    public WatchlistItemEnrichedResponse create(WatchlistCreateRequest request) {
        if (request == null) {
            throw new BadRequestException("Payload obrigatório");
        }

        var cityInfoOpt = ibgeGateway.findCityById(request.getCityId());
        if (cityInfoOpt.isEmpty()) {
            throw new BadRequestException("cityId inválido (IBGE não encontrado): " + request.getCityId());
        }

        var entity = mapperWatch.toEntity(request);
        repository.persist(entity);

        return mapperWatch.toEnrichedResponse(entity, cityInfoOpt.get());
    }


    @Override
    public List<WatchlistItemEnrichedResponse> list(int page, int size) {
        if (page < 0 || size < 1) {
            throw new BadRequestException("Parâmetros de paginação inválidos");
        }
        var entities = repository.findAll()
                .page(Page.of(page, size))
                .list();
        return entities.stream()
                .map(e -> {
                    var city = ibgeGateway.findCityById(e.getCityId()).orElse(null);
                    return mapperWatch.toEnrichedResponse(e, city);
                })
                .toList();
    }


    @Override
    public WatchlistItemEnrichedResponse getById(Long id) {
        var entity = repository.findById(id);
        if (entity == null) {
            throw new NotFoundException("Watchlist item " + id + " não encontrado");
        }
        var city = ibgeGateway.findCityById(entity.getCityId()).orElse(null);
        return mapperWatch.toEnrichedResponse(entity, city);
    }


    @Override
    public WatchlistItemEnrichedResponse update(Long id, WatchlistUpdateRequest request) {
        // Se o seu repository.update retorna Optional<WatchlistItemResponse>, apenas use-o para testar existência
        var updated = repository.update(id, request);
        if (updated.isEmpty()) {
            throw new NotFoundException("Watchlist item " + id + " não encontrado");
        }
        // Recarrega a entidade para enriquecer corretamente (createdAt/updatedAt atualizados)
        var entity = repository.findById(id);
        if (entity == null) {
            throw new NotFoundException("Watchlist item " + id + " não encontrado");
        }
        var city = ibgeGateway.findCityById(entity.getCityId()).orElse(null);
        return mapperWatch.toEnrichedResponse(entity, city);
    }


    @Override
    public void delete(Long id) {
        boolean removed = repository.deleteById(id);
        if (!removed) throw new NotFoundException("Watchlist item " + id + " não encontrado");
    }

}
