package br.com.org.geofinance.app.resource;

import br.com.org.geofinance.app.dto.response.AssetPerformance;
import br.com.org.geofinance.app.service.AssertPerformersService;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;

@QuarkusTest
class AssetAnalyticsResourceTest {

    @InjectMock
    AssertPerformersService service;

    private AssetPerformance sample(String symbol, BigDecimal performance) {
        AssetPerformance ap = new AssetPerformance();
        ap.setSymbol(symbol);
        return ap;
    }

    @Test
    @DisplayName("GET /api/insights/top-performers returns default list")
    void testTopPerformersDefault() {
        when(service.rankTopPerformers(List.of(), "30d", 10))
                .thenReturn(List.of(
                        sample("PETR4", new BigDecimal("12.5")),
                        sample("VALE3", new BigDecimal("10.0"))
                ));

        given()
                .when().get("/api/insights/top-performers")
                .then()
                .statusCode(200)
                .body("size()", equalTo(2))
                .body("[0].symbol", equalTo("PETR4"))
                .body("[1].symbol", equalTo("VALE3"));
    }

    @Test
    @DisplayName("GET /api/insights/top-performers with parameters")
    void testTopPerformersWithParams() {
        when(service.rankTopPerformers(List.of("BBAS3", "ITUB4"), "60d", 5))
                .thenReturn(List.of(
                        sample("BBAS3", new BigDecimal("8.5")),
                        sample("ITUB4", new BigDecimal("7.2"))
                ));

        given()
                .queryParam("symbols", "bbas3,itub4")
                .queryParam("period", "60d")
                .queryParam("size", 5)
                .queryParam("riskAdjusted", true)
                .queryParam("includeDividends", true)
                .when().get("/api/insights/top-performers")
                .then()
                .statusCode(200)
                .body("size()", equalTo(2))
                .body("[0].symbol", equalTo("BBAS3"))
                .body("[1].symbol", equalTo("ITUB4"));
    }

    @Test
    @DisplayName("GET /api/insights/top-performers with empty symbols")
    void testTopPerformersEmptySymbols() {
        when(service.rankTopPerformers(List.of(), "30d", 10))
                .thenReturn(List.of());

        given()
                .queryParam("symbols", "")
                .when().get("/api/insights/top-performers")
                .then()
                .statusCode(200)
                .body("size()", equalTo(0));
    }

}
