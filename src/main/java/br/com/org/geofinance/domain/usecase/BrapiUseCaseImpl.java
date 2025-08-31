package br.com.org.geofinance.domain.usecase;

import br.com.org.geofinance.app.dto.response.BrapiQuoteItem;
import br.com.org.geofinance.app.dto.response.BrapiQuoteListResponse;
import br.com.org.geofinance.domain.gateway.BrapiGateway;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.BadRequestException;

import java.util.List;

@ApplicationScoped
public class BrapiUseCaseImpl implements BrapiUseCase {

    @Inject
    BrapiGateway gateway;


    @Override
    public List<BrapiQuoteItem> searchSymbols(String query, int limit) {
        if (query == null || query.isBlank()) {
            throw new BadRequestException("query é obrigatória");
        }
        if (limit < 1 || limit > 200) {
            throw new BadRequestException("limit deve estar entre 1 e 200");
        }
        return gateway.search(query.trim(), limit);
    }

    @Override
    public List<BrapiQuoteItem> listSymbols(int page, int limit, String sortBy, String sortOrder) {
        int p = page <= 0 ? 1 : page;
        int l = (limit < 1 || limit > 200) ? 50 : limit;
        String sb = (sortBy == null || sortBy.isBlank()) ? "close" : sortBy;
        String so = (sortOrder == null || sortOrder.isBlank()) ? "desc" : sortOrder;
        return gateway.list(p, l, sb, so);
    }


}
