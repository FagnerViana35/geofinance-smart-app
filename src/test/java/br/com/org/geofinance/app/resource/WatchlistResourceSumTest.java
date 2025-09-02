package br.com.org.geofinance.app.resource;

import br.com.org.geofinance.app.service.WatchlistService;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.when;

@QuarkusTest
class WatchlistResourceSumTest {

    @InjectMock
    WatchlistService service;

    @Test
    @DisplayName("GET /api/watchlist/sum/targetprice returns total with BRL formatted string")
    void testSumAllTargetPrice() {
        when(service.sumAllTargetPrice()).thenReturn(new BigDecimal("1234.56"));

        given()
                .when().get("/api/watchlist/sum/targetprice")
                .then()
                .statusCode(200)
                .body("value", equalTo(1234.56f))
                .body("formatted", equalTo("R$ 1.234,56"));
    }

    @Test
    @DisplayName("GET /api/watchlist/sum/targetprice/by-city returns city total with BRL formatted string")
    void testSumTargetPriceByCity() {
        when(service.sumTargetPriceByCity(3550308)).thenReturn(new BigDecimal("99.90"));

        given()
                .queryParam("cityId", 3550308)
                .when().get("/api/watchlist/sum/targetprice/by-city")
                .then()
                .statusCode(200)
                .body("value", equalTo(99.90f))
                .body("formatted", equalTo("R$ 99,90"));
    }

    @Test
    @DisplayName("GET /api/watchlist/sum/targetprice/by-city with null/absent cityId returns zero")
    void testSumTargetPriceByCityNull() {
        when(service.sumTargetPriceByCity(null)).thenReturn(BigDecimal.ZERO);

        given()
                .when().get("/api/watchlist/sum/targetprice/by-city")
                .then()
                .statusCode(200)
                .body("value", equalTo(0))
                .body("formatted", equalTo("R$ 0,00"));
    }
}
