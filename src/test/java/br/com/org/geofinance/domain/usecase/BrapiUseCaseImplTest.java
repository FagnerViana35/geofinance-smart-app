package br.com.org.geofinance.domain.usecase;

import br.com.org.geofinance.app.dto.response.BrapiQuoteItem;
import br.com.org.geofinance.domain.gateway.BrapiGateway;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.ws.rs.BadRequestException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@QuarkusTest
class BrapiUseCaseImplTest {

    @Inject
    BrapiUseCaseImpl useCase;

    @InjectMock
    BrapiGateway gateway;

    private BrapiQuoteItem item(String stock) {
        return BrapiQuoteItem.builder().stock(stock).build();
    }

    @Test
    @DisplayName("searchSymbols deve validar parâmetros e delegar ao gateway")
    void testSearchSymbolsValidatesAndDelegates() {
        when(gateway.search("PETR", 10)).thenReturn(List.of(item("PETR4")));

        var result = useCase.searchSymbols(" PETR ", 10);
        assertEquals(1, result.size());
        assertEquals("PETR4", result.get(0).getStock());

        assertThrows(BadRequestException.class, () -> useCase.searchSymbols(null, 10));
        assertThrows(BadRequestException.class, () -> useCase.searchSymbols(" ", 10));
        assertThrows(BadRequestException.class, () -> useCase.searchSymbols("PETR", 0));
        assertThrows(BadRequestException.class, () -> useCase.searchSymbols("PETR", 201));

        verify(gateway).search("PETR", 10);
    }

    @Test
    @DisplayName("listSymbols deve normalizar parâmetros e delegar ao gateway")
    void testListSymbolsNormalizesAndDelegates() {
        when(gateway.list(1, 50, "close", "desc")).thenReturn(List.of(item("VALE3")));

        var result = useCase.listSymbols(0, 999, null, null);
        assertEquals(1, result.size());
        assertEquals("VALE3", result.get(0).getStock());

        verify(gateway).list(1, 50, "close", "desc");
    }
}
