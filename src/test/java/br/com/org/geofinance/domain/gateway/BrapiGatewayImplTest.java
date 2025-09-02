package br.com.org.geofinance.domain.gateway;

import br.com.org.geofinance.app.dto.response.BrapiQuoteItem;
import br.com.org.geofinance.app.dto.response.BrapiQuoteListResponse;
import br.com.org.geofinance.infra.restClient.BrapiClient;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@QuarkusTest
class BrapiGatewayImplTest {

    @Inject
    BrapiGatewayImpl gateway;

    @InjectMock
    @RestClient
    BrapiClient client;

    private BrapiQuoteListResponse listResp(String... stocks) {
        return BrapiQuoteListResponse.builder()
                .stocks(java.util.Arrays.stream(stocks)
                        .map(s -> BrapiQuoteItem.builder().symbol(s).build())
                        .toList())
                .build();
    }

    @Test
    @DisplayName("search: blank retorna vazio, sucesso mapeia itens, exceção retorna vazio")
    void testSearchCases() {
        assertTrue(gateway.search(" ", 10).isEmpty());

        when(client.search(eq("PETR"), eq(2), anyString())).thenReturn(listResp("PETR4", "PETR3"));
        var r = gateway.search("PETR", 2);
        assertEquals(2, r.size());

        when(client.search(anyString(), anyInt(), anyString())).thenThrow(new RuntimeException("boom"));
        assertTrue(gateway.search("VALE", 5).isEmpty());
    }

    @Test
    @DisplayName("list: normaliza params e mapeia, exceção retorna vazio")
    void testListCases() {
        when(client.list(eq(1), eq(1), eq("close"), eq("desc"), anyString())).thenReturn(listResp("VALE3"));
        var r = gateway.list(0, 0, null, null);
        assertEquals(1, r.size());

        when(client.list(anyInt(), anyInt(), anyString(), anyString(), anyString())).thenThrow(new RuntimeException("x"));
        assertTrue(gateway.list(1, 10, "close", "asc").isEmpty());
    }
}
