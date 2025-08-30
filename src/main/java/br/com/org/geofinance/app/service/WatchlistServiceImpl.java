package br.com.org.geofinance.app.service;

import br.com.org.geofinance.app.dto.request.WatchlistCreateRequest;
import br.com.org.geofinance.app.dto.request.WatchlistUpdateRequest;
import br.com.org.geofinance.app.dto.response.WatchlistItemResponse;
import br.com.org.geofinance.domain.usecase.WatchUseCase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class WatchlistServiceImpl implements WatchlistService {

    @Inject
    WatchUseCase watchUseCase;

    @Override
    @Transactional
    public WatchlistItemResponse create(WatchlistCreateRequest request) {
        return watchUseCase.create(request);
    }

    @Override
    public List<WatchlistItemResponse> list(int page, int size) {
      return watchUseCase.list(page, size);
    }

    @Override
    public WatchlistItemResponse getById(Long id) {
        return watchUseCase.getById(id);
    }

    @Override
    @Transactional
    public WatchlistItemResponse update(Long id, WatchlistUpdateRequest request) {
        return watchUseCase.update(id, request);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        watchUseCase.delete(id);
    }

}
