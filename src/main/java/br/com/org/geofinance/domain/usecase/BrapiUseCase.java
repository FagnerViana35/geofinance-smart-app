package br.com.org.geofinance.domain.usecase;

import br.com.org.geofinance.app.dto.response.BrapiQuoteItem;
import br.com.org.geofinance.app.dto.response.BrapiQuoteListResponse;

import java.util.List;

public interface BrapiUseCase {

    List<BrapiQuoteItem> searchSymbols(String query, int limit);
    List<BrapiQuoteItem> listSymbols(int page, int limit, String sortBy, String sortOrder);

}
