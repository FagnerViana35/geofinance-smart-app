package br.com.org.geofinance.domain.gateway;

import java.util.List;

/**
 * Fornece séries históricas de preços (fechamento) de um símbolo.
 * Convenciona-se retornar em ordem cronológica ASC (do mais antigo para o mais recente).
 */

public interface FinancialDataGateway {
    /**
     * @param symbol símbolo do ativo (ex.: "PETR4.SA")
     * @param maxPoints quantidade máxima de pontos desejados (ex.: 100)
     * @return lista de fechamentos em ordem ASC (antigo -> recente)
     */
    List<Double> getDailyCloses(String symbol, int maxPoints);

}
