package br.com.org.geofinance.cross.mapper;

import br.com.org.geofinance.app.dto.response.AssetPerformance;
import br.com.org.geofinance.app.dto.response.BrapiQuoteItem;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MapperAssetPerformance {

    public AssetPerformance toAssetPerformance(BrapiQuoteItem it) {
        if (it == null) {
            return null;
        }
        // Mapeia informações básicas do brapi para o DTO de performance. Muitos campos
        // (ex.: period, dividendsPct, volatility, score, dataPoints) não estão disponíveis
        // na resposta do brapi.list(), então mantemos como null para possível enriquecimento futuro.
        return AssetPerformance.builder()
                .symbol(it.getStock())
                .changePct(it.getChangeValue())
                .changePercent(it.getChangePercent())
                .close(it.getClose())
                .sector(it.getSector())
                .name(it.getName())
                .build();
    }

//                 it.getStock(),
//                         it.getName(),
//                         it.getClose(),
//                         it.getChange(),   // variação percentual recente
//                         it.getSector(),
//                         it.getSegment()
}
