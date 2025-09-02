package br.com.org.geofinance.domain.usecase;

import br.com.org.geofinance.app.dto.response.AssetPerformance;

import java.util.List;

public interface AssertPerformersUseCase {

    List<AssetPerformance> rankAssertPerformers(List<String> symbols,
                                                String period,
                                                int size);}
