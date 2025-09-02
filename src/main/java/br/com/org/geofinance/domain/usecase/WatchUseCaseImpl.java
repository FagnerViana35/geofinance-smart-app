package br.com.org.geofinance.domain.usecase;

import br.com.org.geofinance.app.dto.request.WatchlistCreateRequest;
import br.com.org.geofinance.app.dto.request.WatchlistUpdateRequest;
import br.com.org.geofinance.app.dto.response.CityInfo;
import br.com.org.geofinance.app.dto.response.WatchlistItemResponse;
import br.com.org.geofinance.cross.mapper.MapperWatch;
import br.com.org.geofinance.domain.gateway.IbgeGateway;
import br.com.org.geofinance.infra.db.repository.WatchRepository;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;
import lombok.extern.log4j.Log4j2;

import java.math.BigDecimal;
import java.util.List;

@Log4j2
@ApplicationScoped
public class WatchUseCaseImpl implements WatchUseCase {
    @Inject
    WatchRepository repository;

    @Inject
    MapperWatch mapperWatch;

    @Inject
    IbgeGateway ibgeGateway;


    // ... existing code ...
    @Override
    public WatchlistItemResponse create(WatchlistCreateRequest request) {
        if (request == null) {
            log.error("O payload está nulo");
            throw new BadRequestException("Payload obrigatório");
        }

        CityInfo city = null;
        if (request.getCityId() != null) {
            if (request.getCityId() <= 0) {
                throw new BadRequestException("cityId deve ser positivo");
            }
            var cityInfoOpt = ibgeGateway.findCityById(request.getCityId());
            if (cityInfoOpt.isEmpty()) {
                throw new BadRequestException("cityId inválido (IBGE não encontrado): " + request.getCityId());
            }
            city = cityInfoOpt.get();
        }
        var entity = mapperWatch.toEntity(request);
        repository.persist(entity);
        log.info("Investimento inserido com sucesso na carteira");
        return mapperWatch.toEnrichedResponse(entity, city);
    }


    @Override
    public List<WatchlistItemResponse> list(int page, int size) {
        if (page < 0 || size < 1) {
            throw new BadRequestException("Parâmetros de paginação inválidos");
        }
        var entities = repository.findAll()
                .page(Page.of(page, size))
                .list();
        log.info("Busca efetuada com sucesso");
        return entities.stream()
                .map(e -> {
                    return mapperWatch.toResponse(e);
                })
                .toList();

    }


    @Override
    public WatchlistItemResponse getById(Long id) {
        var entity = repository.findById(id);
        if (entity == null) {
            throw new NotFoundException("Watchlist item " + id + " não encontrado");
        }
        var city = ibgeGateway.findCityById(entity.getCityId()).orElse(null);
        log.info("Busca do ID efetuada com sucesso");
        return mapperWatch.toEnrichedResponse(entity, city);
    }


    @Override
    public WatchlistItemResponse update(Long id, WatchlistUpdateRequest request) {

        var updated = repository.update(id, request);
        log.info("Edição concluída");
        if (updated.isEmpty()) {
            throw new NotFoundException("Watchlist item " + id + " não encontrado");
        }

        var entity = repository.findById(id);
        if (entity == null) {
            throw new NotFoundException("Watchlist item " + id + " não encontrado");
        }
        var city = ibgeGateway.findCityById(entity.getCityId()).orElse(null);
        return mapperWatch.toEnrichedResponse(entity, city);
    }

    public BigDecimal sumAllTargetPrice() {
        return repository.sumAllTargetPrice();
    }

    public BigDecimal sumTargetPriceByCity(Integer cityId) {
        return repository.sumTargetPriceByCity(cityId);
    }


    @Override
    public void delete(Long id) {
        boolean removed = repository.deleteById(id);
        log.info("Objeto foi deletado concluída");
        if (!removed) throw new NotFoundException("Watchlist item " + id + " não encontrado");
    }

}
