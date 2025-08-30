package br.com.org.geofinance.app.service;

import br.com.org.geofinance.app.dto.request.WatchlistCreateRequest;
import br.com.org.geofinance.app.dto.request.WatchlistUpdateRequest;
import br.com.org.geofinance.app.dto.response.WatchlistItemResponse;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class WatchlistServiceImpl implements WatchlistService {

    @Override
    @Transactional
    public WatchlistItemResponse create(WatchlistCreateRequest request) {
//        return createUseCase.execute(request);
        return null;
    }

    @Override
    public List<WatchlistItemResponse> list(int page, int size) {
//        return listUseCase.execute(page, size);
        return null;
    }

    @Override
    public WatchlistItemResponse getById(Long id) {
//        return getUseCase.execute(id);
        return null;
    }

    @Override
    @Transactional
    public WatchlistItemResponse update(Long id, WatchlistUpdateRequest request) {
//        return updateUseCase.execute(id, request);
        return null;
    }

    @Override
    @Transactional
    public void delete(Long id) {
//        deleteUseCase.execute(id);
        System.out.println("Teste DELETE");
    }

}
