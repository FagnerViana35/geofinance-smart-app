package br.com.org.geofinance.app.resource;

import br.com.org.geofinance.app.dto.request.WatchlistCreateRequest;
import br.com.org.geofinance.app.dto.request.WatchlistUpdateRequest;
import br.com.org.geofinance.app.dto.response.CityInfo;
import br.com.org.geofinance.app.dto.response.WatchlistItemResponse;
import br.com.org.geofinance.app.service.WatchlistService;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.NotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;

@QuarkusTest
class WatchlistResourceTest {

    @InjectMock
    WatchlistService service;

    private WatchlistItemResponse enriched(Long id, String symbol, Integer cityId, String cityUf) {
        WatchlistItemResponse r = new WatchlistItemResponse();
        r.setId(id);
        r.setSymbol(symbol);
        r.setCityId(cityId);
        r.setTargetPrice(new BigDecimal("10.50"));
        r.setNotes("note");
        r.setCreatedAt(OffsetDateTime.now().minusDays(1));
        r.setUpdatedAt(OffsetDateTime.now());
        if (cityId != null) {
            r.setCity(new CityInfo(cityId, "CityName", cityUf));
        }
        return r;
    }

    @Test
    @DisplayName("POST /api/watchlist returns 201 with Location and body")
    void testCreateWatchListItem() {
        var req = WatchlistCreateRequest.builder()
                .symbol("PETR4")
                .cityId(3550308)
                .targetPrice(new BigDecimal("10.50"))
                .notes("n1")
                .build();
        when(service.create(org.mockito.ArgumentMatchers.any(WatchlistCreateRequest.class)))
                .thenReturn(enriched(1L, "PETR4", 3550308, "SP"));

        given()
                .contentType("application/json")
                .body(req)
        .when()
                .post("/api/watchlist")
        .then()
                .statusCode(201)
                .header("Location", endsWith("/api/watchlist/1"))
                .body("id", equalTo(1))
                .body("symbol", equalTo("PETR4"))
                .body("city.uf", equalTo("SP"));
    }

    @Test
    @DisplayName("GET /api/watchlist returns list")
    void testListWatchListItems() {
        when(service.list(0, 20)).thenReturn(List.of(
                enriched(1L, "PETR4", 3550308, "SP"),
                enriched(2L, "VALE3", null, null)
        ));

        given()
                .when().get("/api/watchlist?page=0&size=20")
                .then()
                .statusCode(200)
                .body("size()", equalTo(2))
                .body("[0].symbol", equalTo("PETR4"))
                .body("[1].symbol", equalTo("VALE3"));
    }

    @Test
    @DisplayName("GET /api/watchlist/{id} returns item")
    void testGetByIdOk() {
        when(service.getById(10L)).thenReturn(enriched(10L, "BBAS3", 5300108, "DF"));

        given()
                .when().get("/api/watchlist/10")
                .then()
                .statusCode(200)
                .body("id", equalTo(10))
                .body("symbol", equalTo("BBAS3"))
                .body("city.uf", equalTo("DF"));
    }

    @Test
    @DisplayName("GET /api/watchlist/{id} returns 404 when not found")
    void testGetByIdNotFound() {
        when(service.getById(999L)).thenThrow(new NotFoundException("not found"));

        given()
                .when().get("/api/watchlist/999")
                .then()
                .statusCode(404);
    }

    @Test
    @DisplayName("PUT /api/watchlist/{id} returns updated item")
    void testUpdateOk() {
        var req = WatchlistUpdateRequest.builder()
                .targetPrice(new BigDecimal("20.00"))
                .notes("upd")
                .build();
        when(service.update(org.mockito.ArgumentMatchers.eq(5L), org.mockito.ArgumentMatchers.any(WatchlistUpdateRequest.class)))
                .thenReturn(enriched(5L, "WEGE3", 4205407, "SC"));

        given()
                .contentType("application/json")
                .body(req)
        .when()
                .put("/api/watchlist/5")
        .then()
                .statusCode(200)
                .body("id", equalTo(5))
                .body("symbol", equalTo("WEGE3"));
    }

    @Test
    @DisplayName("PUT /api/watchlist/{id} returns 404 when not found")
    void testUpdateNotFound() {
        var req = WatchlistUpdateRequest.builder().notes("x").build();
        when(service.update(org.mockito.ArgumentMatchers.eq(404L), org.mockito.ArgumentMatchers.any(WatchlistUpdateRequest.class)))
                .thenThrow(new NotFoundException("not found"));

        given()
                .contentType("application/json")
                .body(req)
        .when()
                .put("/api/watchlist/404")
        .then()
                .statusCode(404);
    }

    @Test
    @DisplayName("DELETE /api/watchlist/{id} returns 204")
    void testDeleteOk() {
        given()
                .when().delete("/api/watchlist/7")
                .then()
                .statusCode(204);
    }

    @Test
    @DisplayName("DELETE /api/watchlist/{id} returns 404 when not found")
    void deleteNotFound() {
        // make service.delete throw NotFoundException
        org.mockito.Mockito.doThrow(new NotFoundException("nf"))
                .when(service).delete(777L);

        given()
                .when().delete("/api/watchlist/777")
                .then()
                .statusCode(404);
    }
}