package br.com.org.geofinance.domain.gateway;

import br.com.org.geofinance.app.dto.response.BrapiQuoteItem;
import br.com.org.geofinance.app.dto.response.BrapiQuoteListResponse;
import br.com.org.geofinance.infra.restClient.BrapiClient;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.log4j.Log4j2;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Log4j2
@ApplicationScoped
public class BrapiGatewayImpl implements BrapiGateway {

    @Inject
    @RestClient
    BrapiClient client;

    final private String TOKEN_MANAGER = "Bearer ct1zSVoMGQHjQxwKj5z6yH";

    @Override
    public List<BrapiQuoteItem> search(String query, int limit) {
        if (query == null || query.isBlank()) {
            return Collections.emptyList();
        }
        try {
            BrapiQuoteListResponse res = client.search(query.trim(), Math.max(1, limit), TOKEN_MANAGER);
            int found = (res != null && res.getStocks() != null) ? res.getStocks().size() : 0;
            log.info("Busca concluída com sucesso para query: {} e limite: {}. Encontrados {} ativos.",
                    query, limit, found);
            return map(res);
        } catch (Exception e) {
            log.error("Não foi encontrado o investimento correspondente ", e);
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
            BrapiQuoteListResponse res = client.list(p, l, sb, so, TOKEN_MANAGER);
            log.info("Busca concluída com sucesso para listagem: page={}, limit={}, sortBy={}, sortOrder={}, encontrados {} itens.",
                    p, l, sb, so, (res != null && res.getStocks() != null ? res.getStocks().size() : 0));
            return map(res);

        } catch (Exception e) {
            log.error("Não foi encontrado o investimento correspondente ", e);
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
