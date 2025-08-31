package br.com.org.geofinance.app.service;

import br.com.org.geofinance.app.dto.response.BrapiQuoteItem;
import br.com.org.geofinance.domain.usecase.BrapiUseCase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.BadRequestException;

import java.util.List;

@ApplicationScoped
public class BrapiService {

    @Inject
    BrapiUseCase brapiUseCase;


    public List<BrapiQuoteItem> searchSymbols(String query, int limit) {
        return brapiUseCase.searchSymbols(query, limit);
    }

    public List<BrapiQuoteItem> listSymbols(int page, int limit, String sortBy, String sortOrder) {
        return brapiUseCase.listSymbols(page, limit, sortBy, sortOrder);
    }



}
