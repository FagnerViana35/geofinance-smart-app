package br.com.org.geofinance.domain.usecase;

import br.com.org.geofinance.app.dto.response.BrapiQuoteItem;
import br.com.org.geofinance.app.dto.response.BrapiQuoteListResponse;

import java.util.List;

public interface BrapiUseCase {
    /**
     * Busca por nome/ticker (ex.: "PETR").
     * Retorna uma lista de símbolos (tickers) com metadados.
     */
    List<BrapiQuoteItem> searchSymbols(String query, int limit);

    /**
     * Lista paginada de símbolos.
     * page inicia em 1 (convenção do brapi.dev).
     */
    List<BrapiQuoteItem> listSymbols(int page, int limit, String sortBy, String sortOrder);

}
