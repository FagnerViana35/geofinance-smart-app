package br.com.org.geofinance.domain.gateway;

import br.com.org.geofinance.app.dto.response.BrapiQuoteItem;
import br.com.org.geofinance.app.dto.response.BrapiQuoteListResponse;

import java.util.List;

public interface BrapiGateway {
    // Busca por nome/ticker (ex.: "PETR")
    List<BrapiQuoteItem> search(String query, int limit);

    // Lista completa paginada
    List<BrapiQuoteItem> list(int page, int limit, String sortBy, String sortOrder);

}
