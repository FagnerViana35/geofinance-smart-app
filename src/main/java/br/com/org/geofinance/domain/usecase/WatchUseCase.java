package br.com.org.geofinance.domain.usecase;

import br.com.org.geofinance.app.dto.request.WatchlistCreateRequest;
import br.com.org.geofinance.app.dto.request.WatchlistUpdateRequest;
import br.com.org.geofinance.app.dto.response.WatchlistItemEnrichedResponse;

import java.util.List;

public interface WatchUseCase {
    WatchlistItemEnrichedResponse create(WatchlistCreateRequest request);
    List<WatchlistItemEnrichedResponse> list(int page, int size);
    WatchlistItemEnrichedResponse getById(Long id);
    WatchlistItemEnrichedResponse update(Long id, WatchlistUpdateRequest request);
    void delete(Long id);

}
