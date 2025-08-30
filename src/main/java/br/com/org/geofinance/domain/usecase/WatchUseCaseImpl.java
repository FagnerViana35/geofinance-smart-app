package br.com.org.geofinance.domain.usecase;

import br.com.org.geofinance.app.dto.request.WatchlistCreateRequest;
import br.com.org.geofinance.app.dto.request.WatchlistUpdateRequest;
import br.com.org.geofinance.app.dto.response.WatchlistItemResponse;
import br.com.org.geofinance.cross.mapper.MapperWatch;
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


    @Override
    public WatchlistItemResponse create(WatchlistCreateRequest request) {
        if (request == null) throw new BadRequestException("Payload obrigatório");
        var entity = mapperWatch.toEntity(request);
        repository.persist(entity);
        return mapperWatch.toResponse(entity);
    }

    @Override
    public List<WatchlistItemResponse> list(int page, int size) {
        if (page < 0 || size < 1) throw new BadRequestException("Parâmetros de paginação inválidos");
        return repository.findAll()
                .page(Page.of(page, size))
                .list()
                .stream()
                .map(mapperWatch::toResponse)
                .toList();
    }

    @Override
    public WatchlistItemResponse getById(Long id) {
        var entity = repository.findById(id); // pode ser null
        if (entity == null) {
            throw new NotFoundException("Watchlist item " + id + " não encontrado");
        }
        return mapperWatch.toResponse(entity);
    }

    @Override
    public WatchlistItemResponse update(Long id, WatchlistUpdateRequest request) {
        return repository.update(id, request)
                .orElseThrow(() -> new NotFoundException("Watchlist item " + id + " não encontrado"));
    }

    @Override
    public void delete(Long id) {
        boolean removed = repository.deleteById(id);
        if (!removed) throw new NotFoundException("Watchlist item " + id + " não encontrado");
    }

}
