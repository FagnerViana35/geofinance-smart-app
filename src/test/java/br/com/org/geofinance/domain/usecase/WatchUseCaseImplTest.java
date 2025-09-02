package br.com.org.geofinance.domain.usecase;

import br.com.org.geofinance.app.dto.request.WatchlistCreateRequest;
import br.com.org.geofinance.app.dto.request.WatchlistUpdateRequest;
import br.com.org.geofinance.app.dto.response.CityInfo;
import br.com.org.geofinance.app.dto.response.WatchlistItemResponse;
import br.com.org.geofinance.cross.mapper.MapperWatch;
import br.com.org.geofinance.domain.gateway.IbgeGateway;
import br.com.org.geofinance.infra.db.model.WatchlistEntity;
import br.com.org.geofinance.infra.db.repository.WatchRepository;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@QuarkusTest
class WatchUseCaseImplTest {

    @Inject
    WatchUseCaseImpl useCase;

    @InjectMock
    WatchRepository repository;

    @InjectMock
    MapperWatch mapperWatch;

    @InjectMock
    IbgeGateway ibgeGateway;

    private WatchlistCreateRequest req() {
        var r = new WatchlistCreateRequest();
        r.setSymbol("VALE3");
        r.setCityId(3550308);
        r.setTargetPrice(new BigDecimal("100.00"));
        r.setNotes("obs");
        return r;
    }

    @Test
    @DisplayName("create valida payload, valida city opcional e retorna enriched response")
    void testCreateFlow() {
        assertThrows(BadRequestException.class, () -> useCase.create(null));

        var request = req();
        var city = new CityInfo(3550308, "Sao Paulo", "SP");
        when(ibgeGateway.findCityById(3550308)).thenReturn(Optional.of(city));

        var entity = WatchlistEntity.builder().id(1L).symbol("VALE3").cityId(3550308).build();
        when(mapperWatch.toEntity(request)).thenReturn(entity);
        var enriched = new WatchlistItemResponse();
        enriched.setId(1L);
        when(mapperWatch.toEnrichedResponse(entity, city)).thenReturn(enriched);

        var resp = useCase.create(request);
        assertEquals(1L, resp.getId());
        verify(repository).persist(entity);
    }

    @Test
    @DisplayName("create com cityId inválido deve lançar BadRequest")
    void testCreateInvalidCity() {
        var request = req();
        request.setCityId(-1);
        assertThrows(BadRequestException.class, () -> useCase.create(request));

        request.setCityId(9999999);
        when(ibgeGateway.findCityById(9999999)).thenReturn(Optional.empty());
        assertThrows(BadRequestException.class, () -> useCase.create(request));
    }

    @Test
    @DisplayName("getById e update/delete tratam NotFound")
    void testGetUpdateDeleteFlows() {

        when(repository.findById(100L)).thenReturn(null);
        assertThrows(NotFoundException.class, () -> useCase.getById(100L));

        var e = WatchlistEntity.builder().id(3L).symbol("ITUB4").cityId(3550308).build();
        when(repository.findById(3L)).thenReturn(e);
        when(ibgeGateway.findCityById(3550308)).thenReturn(Optional.empty());
        when(mapperWatch.toEnrichedResponse(e, null)).thenReturn(new WatchlistItemResponse());
        assertNotNull(useCase.getById(3L));


        var upd = new WatchlistUpdateRequest();
        when(repository.update(9L, upd)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> useCase.update(9L, upd));

        when(repository.update(4L, upd)).thenReturn(Optional.ofNullable(null));
        when(repository.update(4L, upd)).thenReturn(Optional.of(new WatchlistItemResponse()));
        when(repository.findById(4L)).thenReturn(e);
        when(mapperWatch.toEnrichedResponse(any(), any())).thenReturn(new WatchlistItemResponse());
        assertNotNull(useCase.update(4L, upd));

        when(repository.deleteById(7L)).thenReturn(false);
        assertThrows(NotFoundException.class, () -> useCase.delete(7L));

        when(repository.deleteById(8L)).thenReturn(true);
        assertDoesNotThrow(() -> useCase.delete(8L));
    }

    @Test
    @DisplayName("sumAllTargetPrice deve delegar para o repository e retornar o valor")
    void testSumAllTargetPrice_ok() {
        when(repository.sumAllTargetPrice()).thenReturn(new BigDecimal("123.45"));

        var result = useCase.sumAllTargetPrice();

        assertEquals(new BigDecimal("123.45"), result);
        verify(repository).sumAllTargetPrice();
    }

    @Test
    @DisplayName("sumTargetPriceByCity deve delegar para o repository e retornar o valor")
    void testSumTargetPriceByCity_ok() {
        when(repository.sumTargetPriceByCity(3550308)).thenReturn(new BigDecimal("999.99"));

        var result = useCase.sumTargetPriceByCity(3550308);

        assertEquals(new BigDecimal("999.99"), result);
        verify(repository).sumTargetPriceByCity(3550308);
    }

    @Test
    @DisplayName("sumTargetPriceByCity deve lidar com retorno null do repository")
    void testSumTargetPriceByCity_null() {
        when(repository.sumTargetPriceByCity(123)).thenReturn(null);

        var result = useCase.sumTargetPriceByCity(123);

        assertNull(result);
        verify(repository).sumTargetPriceByCity(123);
    }
}
