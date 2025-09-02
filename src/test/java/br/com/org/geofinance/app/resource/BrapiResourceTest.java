package br.com.org.geofinance.app.resource;

import br.com.org.geofinance.app.dto.response.BrapiQuoteItem;
import br.com.org.geofinance.app.service.BrapiService;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;

@QuarkusTest
class BrapiResourceTest {

    @InjectMock
    BrapiService brapiService;

    private BrapiQuoteItem sample(String symbol, String name, Double close) {
        BrapiQuoteItem item = new BrapiQuoteItem();
        item.setSymbol(symbol);
        item.setName(name);
        item.setClose(close);
        return item;
    }

    @Test
    @DisplayName("GET /api/symbols/search returns search results")
    void testSearchSymbols() {
        when(brapiService.searchSymbols("PETR", 10))
                .thenReturn(List.of(
                        sample("PETR4", "Petrobras PN", 28.50),
                        sample("PETR3", "Petrobras ON", 27.30)
                ));

        given()
                .queryParam("query", "PETR")
                .queryParam("limit", 10)
                .when()
                .get("/api/symbols/search")
                .then()
                .statusCode(200)
                .body("$", hasSize(2))
                .body("[0].symbol", equalTo("PETR4"))
                .body("[1].symbol", equalTo("PETR3"));
    }

    @Test
    @DisplayName("GET /api/symbols/search with default limit")
    void testSearchSymbolsDefaultLimit() {
        when(brapiService.searchSymbols("VALE", 50))
                .thenReturn(List.of(sample("VALE3", "Vale ON", 98.50)));

        given()
                .queryParam("query", "VALE")
                .when()
                .get("/api/symbols/search")
                .then()
                .statusCode(200)
                .body("$", hasSize(1))
                .body("[0].symbol", equalTo("VALE3"));
    }

    @Test
    @DisplayName("GET /api/symbols list returns paginated symbols")
    void testListSymbols() {
        when(brapiService.listSymbols(1, 2, "close", "desc"))
                .thenReturn(List.of(
                        sample("PETR4", "Petrobras PN", 28.50),
                        sample("VALE3", "Vale ON", 98.50)
                ));

        given()
                .queryParam("page", 1)
                .queryParam("limit", 2)
                .queryParam("sortBy", "close")
                .queryParam("sortOrder", "desc")
                .when()
                .get("/api/symbols")
                .then()
                .statusCode(200)
                .body("$", hasSize(2))
                .body("[0].symbol", equalTo("PETR4"))
                .body("[1].symbol", equalTo("VALE3"));
    }

    @Test
    @DisplayName("GET /api/symbols list with default params")
    void TestListSymbolsDefaultParams() {
        when(brapiService.listSymbols(1, 50, "close", "desc"))
                .thenReturn(List.of(sample("ITUB4", "Itaú UN", 28.75)));

        given()
                .when()
                .get("/api/symbols")
                .then()
                .statusCode(200)
                .body("$", hasSize(1))
                .body("[0].symbol", equalTo("ITUB4"));
    }
}
