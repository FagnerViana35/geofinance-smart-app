package br.com.org.geofinance.app.service;

import br.com.org.geofinance.app.dto.request.WatchlistCreateRequest;
import br.com.org.geofinance.app.dto.request.WatchlistUpdateRequest;
import br.com.org.geofinance.app.dto.response.WatchlistItemResponse;
import br.com.org.geofinance.domain.usecase.WatchUseCase;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import jakarta.inject.Inject;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@QuarkusTest
class WatchlistServiceImplTest {

    @Inject
    WatchlistServiceImpl service;

    @InjectMock
    WatchUseCase watchUseCase;

    @Test
    @DisplayName("create deve delegar para useCase.create e retornar response")
    void testCreateOk() {
        var req = new WatchlistCreateRequest();
        var resp = new WatchlistItemResponse();
        resp.setId(1L);

        when(watchUseCase.create(req)).thenReturn(resp);

        var result = service.create(req);

        assertEquals(1L, result.getId());
        verify(watchUseCase).create(req);
    }

    @Test
    @DisplayName("list deve delegar para useCase.list e retornar lista")
    void testListOk() {
        var resp = new WatchlistItemResponse();
        resp.setId(2L);

        when(watchUseCase.list(0, 10)).thenReturn(List.of(resp));

        var result = service.list(0, 10);

        assertEquals(1, result.size());
        assertEquals(2L, result.get(0).getId());
        verify(watchUseCase).list(0, 10);
    }

    @Test
    @DisplayName("getById deve delegar para useCase.getById e retornar item")
    void testGetByIdOk() {
        var resp = new WatchlistItemResponse();
        resp.setId(3L);

        when(watchUseCase.getById(3L)).thenReturn(resp);

        var result = service.getById(3L);

        assertEquals(3L, result.getId());
        verify(watchUseCase).getById(3L);
    }

    @Test
    @DisplayName("update deve delegar para useCase.update e retornar item atualizado")
    void testUpdateOk() {
        var req = new WatchlistUpdateRequest();
        var resp = new WatchlistItemResponse();
        resp.setId(4L);

        when(watchUseCase.update(4L, req)).thenReturn(resp);

        var result = service.update(4L, req);

        assertEquals(4L, result.getId());
        verify(watchUseCase).update(4L, req);
    }

    @Test
    @DisplayName("delete deve delegar para useCase.delete")
    void testDeleteOk() {
        doNothing().when(watchUseCase).delete(5L);

        service.delete(5L);

        verify(watchUseCase).delete(5L);
    }

    @Test
    @DisplayName("sumAllTargetPrice deve delegar para useCase.sumAllTargetPrice")
    void testSumAllTargetPriceOk() {
        when(watchUseCase.sumAllTargetPrice()).thenReturn(new BigDecimal("123.45"));

        var result = service.sumAllTargetPrice();

        assertEquals(new BigDecimal("123.45"), result);
        verify(watchUseCase).sumAllTargetPrice();
    }

    @Test
    @DisplayName("sumTargetPriceByCity deve delegar para useCase.sumTargetPriceByCity")
    void sumTargetPriceByCityOk() {
        when(watchUseCase.sumTargetPriceByCity(3550308)).thenReturn(new BigDecimal("999.99"));

        var result = service.sumTargetPriceByCity(3550308);

        assertEquals(new BigDecimal("999.99"), result);
        verify(watchUseCase).sumTargetPriceByCity(3550308);
    }
}
