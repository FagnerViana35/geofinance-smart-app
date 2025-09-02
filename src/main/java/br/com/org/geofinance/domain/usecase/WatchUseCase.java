package br.com.org.geofinance.domain.usecase;

import br.com.org.geofinance.app.dto.request.WatchlistCreateRequest;
import br.com.org.geofinance.app.dto.request.WatchlistUpdateRequest;
import br.com.org.geofinance.app.dto.response.WatchlistItemResponse;

import java.math.BigDecimal;
import java.util.List;

public interface WatchUseCase {
    WatchlistItemResponse create(WatchlistCreateRequest request);
    List<WatchlistItemResponse> list(int page, int size);
    WatchlistItemResponse getById(Long id);
    WatchlistItemResponse update(Long id, WatchlistUpdateRequest request);
    void delete(Long id);
    BigDecimal sumAllTargetPrice();
    BigDecimal sumTargetPriceByCity(Integer cityId);

}
