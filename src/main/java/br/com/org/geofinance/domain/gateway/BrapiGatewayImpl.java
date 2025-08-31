package br.com.org.geofinance.domain.gateway;

import br.com.org.geofinance.app.dto.response.BrapiQuoteItem;
import br.com.org.geofinance.app.dto.response.BrapiQuoteListResponse;
import br.com.org.geofinance.infra.restClient.BrapiClient;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@ApplicationScoped

public class BrapiGatewayImpl implements BrapiGateway {

    @Inject
    @RestClient
    BrapiClient client;

    @Override
    public List<BrapiQuoteItem> search(String query, int limit) {
        if (query == null || query.isBlank()) {
            return Collections.emptyList();
        }
        try {
            BrapiQuoteListResponse res = client.search(query.trim(), Math.max(1, limit));
            return map(res);
        } catch (Exception e) {
            // Em produção: logar, métricas e possivelmente retry/backoff
            return Collections.emptyList();
        }
    }

    @Override
    public List<BrapiQuoteItem> list(int page, int limit, String sortBy, String sortOrder) {
        int p = page <= 0 ? 1 : page;       // brapi pagina a partir de 1
        int l = Math.max(1, limit);
        String sb = (sortBy == null || sortBy.isBlank()) ? "close" : sortBy;
        String so = (sortOrder == null || sortOrder.isBlank()) ? "desc" : sortOrder;

        try {
            BrapiQuoteListResponse res = client.list(p, l, sb, so);
            return map(res);
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    private List<BrapiQuoteItem> map(BrapiQuoteListResponse res) {
        if (res == null || res.getStocks() == null) return Collections.emptyList();
        return res.getStocks().stream()
                .filter(Objects::nonNull)
                .toList();
    }

}
