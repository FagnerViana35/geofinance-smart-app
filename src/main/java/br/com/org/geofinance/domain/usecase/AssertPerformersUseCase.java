package br.com.org.geofinance.domain.usecase;

import br.com.org.geofinance.app.dto.response.AssetPerformance;

import java.util.List;

public interface AssertPerformersUseCase {

    /**
     * Ranqueia os símbolos por performance.
     * - Se symbols vier vazio/nulo, usa uma lista do brapi (paginada).
     * - period/riskAdjusted/includeDividends podem ser ignorados se não houver histórico.
     */
    List<AssetPerformance> rankAssertPerformers(List<String> symbols,
                                                String period,
                                                int size,
                                                boolean riskAdjusted,
                                                boolean includeDividends);}
