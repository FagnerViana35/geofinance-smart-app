package br.com.org.geofinance.cross.mapper;

import br.com.org.geofinance.app.dto.request.WatchlistCreateRequest;
import br.com.org.geofinance.app.dto.response.CityInfo;
import br.com.org.geofinance.app.dto.response.WatchlistItemResponse;
import br.com.org.geofinance.infra.db.model.WatchlistEntity;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.*;
@QuarkusTest
class MapperWatchTest {

    private MapperWatch mapper;

    @BeforeEach
    void setUp() {
        mapper = new MapperWatch();
    }

    @Test
    void testToEntityWhenRequestIsNullReturnsNull() {
        assertNull(mapper.toEntity(null));
    }

    @Test
    void testToEntityMapsCorrectly() {
        WatchlistCreateRequest request = new WatchlistCreateRequest();
        request.setSymbol("AAPL");
        request.setCityId(10);
        request.setTargetPrice(BigDecimal.valueOf(150.50));
        request.setNotes("Tech stock");

        WatchlistEntity entity = mapper.toEntity(request);

        assertNotNull(entity);
        assertEquals("AAPL", entity.getSymbol());
        assertEquals(10, entity.getCityId());
        assertEquals(BigDecimal.valueOf(150.50), entity.getTargetPrice());
        assertEquals("Tech stock", entity.getNotes());
    }

    @Test
    void testToResponseWhenEntityIsNullReturnsNull() {
        assertNull(mapper.toResponse(null));
    }

    @Test
    void testToResponseWhenEntityWithoutCityId() {
        WatchlistEntity entity = WatchlistEntity.builder()
                .id(1L)
                .symbol("GOOG")
                .targetPrice(BigDecimal.valueOf(2500.00))
                .notes("Alphabet stock")
                .createdAt(OffsetDateTime.now())
                .updatedAt(OffsetDateTime.now())
                .build();

        WatchlistItemResponse response = mapper.toResponse(entity);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("GOOG", response.getSymbol());
        assertNull(response.getCity());
        assertEquals(BigDecimal.valueOf(2500.00), response.getTargetPrice());
        assertEquals("Alphabet stock", response.getNotes());
    }

    @Test
    void testToResponseWhenEntityWithCityId() {
        WatchlistEntity entity = WatchlistEntity.builder()
                .id(2L)
                .symbol("MSFT")
                .cityId(5)
                .targetPrice(BigDecimal.valueOf(300.00))
                .notes("Microsoft stock")
                .createdAt(OffsetDateTime.now())
                .updatedAt(OffsetDateTime.now())
                .build();

        WatchlistItemResponse response = mapper.toResponse(entity);

        assertNotNull(response);
        assertEquals(2L, response.getId());
        assertEquals("MSFT", response.getSymbol());
        assertNotNull(response.getCity());
        assertEquals(5, response.getCity().getId());
        assertEquals(5, response.getCityId());
    }

    @Test
    void testToEnrichedResponseMapsCorrectly() {
        OffsetDateTime createdAt = OffsetDateTime.of(2023, 1, 1, 10, 0, 0, 0, ZoneOffset.UTC);
        OffsetDateTime updatedAt = OffsetDateTime.of(2023, 6, 1, 15, 0, 0, 0, ZoneOffset.UTC);

        WatchlistEntity entity = WatchlistEntity.builder()
                .id(3L)
                .symbol("TSLA")
                .cityId(7)
                .targetPrice(BigDecimal.valueOf(700.00))
                .notes("Tesla stock")
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();

        CityInfo city = CityInfo.builder()
                .id(7)
                .name("Austin")
                .build();

        WatchlistItemResponse response = mapper.toEnrichedResponse(entity, city);

        assertNotNull(response);
        assertEquals(3L, response.getId());
        assertEquals("TSLA", response.getSymbol());
        assertEquals(7, response.getCityId());
        assertEquals(BigDecimal.valueOf(700.00), response.getTargetPrice());
        assertEquals("Tesla stock", response.getNotes());
        assertEquals(createdAt, response.getCreatedAt());
        assertEquals(updatedAt, response.getUpdatedAt());
        assertEquals(city, response.getCity());
    }
}
