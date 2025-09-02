package br.com.org.geofinance.domain.gateway;

import br.com.org.geofinance.app.dto.response.BrapiQuoteItem;
import br.com.org.geofinance.app.dto.response.BrapiQuoteListResponse;

import java.util.List;

public interface BrapiGateway {

    List<BrapiQuoteItem> search(String query, int limit);
    List<BrapiQuoteItem> list(int page, int limit, String sortBy, String sortOrder);

}
