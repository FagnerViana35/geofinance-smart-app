package br.com.org.geofinance.domain.usecase;

import br.com.org.geofinance.app.dto.response.AssetPerformance;
import br.com.org.geofinance.app.dto.response.BrapiQuoteItem;
import br.com.org.geofinance.cross.mapper.MapperAssetPerformance;
import br.com.org.geofinance.domain.gateway.BrapiGateway;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@QuarkusTest
class AssertPerformersUseCaseImplTest {

    @Inject
    AssertPerformersUseCaseImpl useCase;

    @InjectMock
    BrapiGateway gateway;

    @Inject
    MapperAssetPerformance mapper;

    private BrapiQuoteItem item(String stock, double close, double change) {
        return BrapiQuoteItem.builder()
                .symbol(stock)
                .close(close)
                .change(change)
                .name(stock+" SA")
                .sector("Tech")
                .build();
    }

    @Test
    @DisplayName("Quando symbols é nulo, deve buscar universo padrão e retornar pelo menos 1 item ordenado por changePct")
    void testrankDefaultUniverse() {
        when(gateway.list(1, 200, "close", "desc")).thenReturn(List.of(
                item("AAA3", 10, 0.1),
                item("BBB4", 20, 0.5),
                item("CCC5", 15, -0.2)
        ));

        List<AssetPerformance> result = useCase.rankAssertPerformers(null, "", 2);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("BBB4", result.get(0).getSymbol());
        assertEquals("AAA3", result.get(1).getSymbol());
    }

    @Test
    @DisplayName("Quando symbols são informados, deve filtrar pelo conjunto e respeitar topN >= 1")
    void testRankWithSymbolsAndSizeGuard() {
        when(gateway.list(1, 500, "close", "desc")).thenReturn(List.of(
                item("AAA3", 10, 0.1),
                item("BBB4", 20, 0.5),
                item("CCC5", 15, -0.2)
        ));

        List<AssetPerformance> result = useCase.rankAssertPerformers(List.of("ccc5"), null, 0);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("CCC5", result.get(0).getSymbol());
    }
}
