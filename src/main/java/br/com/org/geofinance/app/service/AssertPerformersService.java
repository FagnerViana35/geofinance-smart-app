package br.com.org.geofinance.app.service;

import br.com.org.geofinance.app.dto.response.AssetPerformance;
import br.com.org.geofinance.domain.usecase.AssertPerformersUseCase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class AssertPerformersService {

    @Inject
    AssertPerformersUseCase useCase;

    public List<AssetPerformance> rankTopPerformers(List<String> symbols, String period, int size,
                                                    boolean riskAdjusted, boolean includeDividends) {
        return useCase.rankAssertPerformers(symbols, period, size, riskAdjusted, includeDividends);
    }

}
