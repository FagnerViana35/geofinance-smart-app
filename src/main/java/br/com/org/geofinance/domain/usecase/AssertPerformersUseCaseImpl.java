package br.com.org.geofinance.domain.usecase;

import br.com.org.geofinance.app.dto.response.AssetPerformance;
import br.com.org.geofinance.app.dto.response.BrapiQuoteItem;
import br.com.org.geofinance.cross.mapper.MapperAssetPerformance;
import br.com.org.geofinance.domain.gateway.BrapiGateway;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@ApplicationScoped
public class AssertPerformersUseCaseImpl implements AssertPerformersUseCase{

    @Inject
    BrapiGateway gateway;

    @Inject
    MapperAssetPerformance mapper;

    @Override
    public List<AssetPerformance> rankAssertPerformers(List<String> symbols,
                                                       String period,
                                                       int size,
                                                       boolean riskAdjusted,
                                                       boolean includeDividends) {
        int topN = Math.max(1, size);

        // 1) Obter universo de análise
        List<BrapiQuoteItem> universe;
        if (symbols == null || symbols.isEmpty()) {
            // Sem lista de símbolos: carrega uma página “grande” do brapi e ranqueia pelo change
            universe = safeList( gateway.list(1, 200, "close", "desc") );
        } else {
            // Com lista: para reduzir chamadas, usa list do brapi e filtra os desejados
            Set<String> wanted = symbols.stream()
                    .filter(Objects::nonNull)
                    .map(s -> s.trim().toUpperCase())
                    .collect(Collectors.toSet());
            List<BrapiQuoteItem> all = safeList( gateway.list(1, 500, "close", "desc") );
            universe = all.stream()
                    .filter(it -> it.getStock() != null && wanted.contains(it.getStock().toUpperCase()))
                    .toList();
        }

        // 2) Montar métricas: aqui usamos change do brapi como “performance recente”
        List<AssetPerformance> ranked = universe.stream()
                .filter(Objects::nonNull)
                .map(mapper::toAssetPerformance)
                // 3) Ordenar por “quem rendeu mais”: changePct desc
                .sorted((a, b) -> Double.compare(
                        nullableToZero(b.getChangePct()), nullableToZero(a.getChangePct())))
                .limit(topN)
                .toList();

        // Observação: riskAdjusted/includeDividends ignorados aqui por falta de histórico/dividendos.
        // Para evoluir: trocar para um gateway de histórico e calcular totalReturn/volatilidade.

        return ranked;
    }

    private List<BrapiQuoteItem> safeList(List<BrapiQuoteItem> l) {
        return (l == null) ? Collections.emptyList() : l;
    }

    private double nullableToZero(Double v) {
        return v == null ? 0.0 : v;
    }

}
