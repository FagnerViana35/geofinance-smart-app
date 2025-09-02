package br.com.org.geofinance.app.service;

import br.com.org.geofinance.app.dto.request.WatchlistCreateRequest;
import br.com.org.geofinance.app.dto.request.WatchlistUpdateRequest;
import br.com.org.geofinance.app.dto.response.WatchlistItemResponse;

import java.util.List;

public interface WatchlistService {
    WatchlistItemResponse create(WatchlistCreateRequest request);
    List<WatchlistItemResponse> list(int page, int size);
    WatchlistItemResponse getById(Long id);
    WatchlistItemResponse update(Long id, WatchlistUpdateRequest request);
    void delete(Long id);

    java.math.BigDecimal sumAllTargetPrice();
    java.math.BigDecimal sumTargetPriceByCity(Integer cityId);
}

