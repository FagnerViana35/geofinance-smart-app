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
        return AssetPerformance.builder()
                .symbol(it.getStock())
                .changePct(it.getChange())
                .changePercent(it.getChange())
                .close(it.getClose())
                .sector(it.getSector())
                .name(it.getName())
                .build();
    }
}
