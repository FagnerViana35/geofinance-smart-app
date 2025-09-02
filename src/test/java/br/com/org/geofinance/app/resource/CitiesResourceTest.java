package br.com.org.geofinance.app.resource;

import br.com.org.geofinance.app.dto.response.CityInfo;
import br.com.org.geofinance.app.service.CitiesService;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@QuarkusTest
class CitiesResourceTest {

    @InjectMock
    CitiesService citiesService;

    @Test
    @DisplayName("GET /api/cities/{id} retorna CityInfo")
    void testGetCityByIdOk() {
        when(citiesService.validateById(3550308)).thenReturn(new CityInfo(3550308, "Sao Paulo", "SP"));

        given()
                .when().get("/api/cities/3550308")
                .then()
                .statusCode(200)
                .body("id", equalTo(3550308))
                .body("uf", equalTo("SP"))
                .body("name", equalTo("Sao Paulo"));
    }

    @Test
    @DisplayName("GET /api/cities/{id} retorna 404 quando não encontrado")
    void testGetCityByIdNotFound() {
        when(citiesService.validateById(anyInt())).thenThrow(new NotFoundException("nf"));

        given()
                .when().get("/api/cities/9999999")
                .then()
                .statusCode(404);
    }

    @Test
    @DisplayName("GET /api/cities/resolve retorna CityInfo quando parâmetros válidos")
    void testResolveOk() {
        when(citiesService.resolveByUfAndName(eq("SP"), eq("Sao Paulo")))
                .thenReturn(new CityInfo(3550308, "Sao Paulo", "SP"));

        given()
                .queryParam("uf", "SP")
                .queryParam("name", "Sao Paulo")
                .when().get("/api/cities/resolve")
                .then()
                .statusCode(200)
                .body("id", equalTo(3550308))
                .body("uf", equalTo("SP"));
    }

    @Test
    @DisplayName("GET /api/cities/resolve sem parâmetros obrigatórios retorna 400")
    void testResolveMissingParams400() {
        given()
                .when().get("/api/cities/resolve")
                .then()
                .statusCode(400);
    }

    @Test
    @DisplayName("GET /api/cities/resolve propaga 404/erros de negócio do serviço")
    void testResolvePropagatesServiceErrors() {
        when(citiesService.resolveByUfAndName(eq("SP"), eq("Xyz")))
                .thenThrow(new NotFoundException("nf"));

        given()
                .queryParam("uf", "SP")
                .queryParam("name", "Xyz")
                .when().get("/api/cities/resolve")
                .then()
                .statusCode(404);
    }
}
