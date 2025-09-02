package br.com.org.geofinance.app.service;

import br.com.org.geofinance.app.dto.response.AssetPerformance;
import br.com.org.geofinance.app.dto.response.BrapiQuoteItem;
import br.com.org.geofinance.app.dto.response.WatchlistItemResponse;
import br.com.org.geofinance.domain.usecase.AssertPerformersUseCase;
import br.com.org.geofinance.domain.usecase.BrapiUseCase;
import br.com.org.geofinance.domain.usecase.CitiesUseCase;
import br.com.org.geofinance.domain.usecase.WatchUseCase;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@QuarkusTest
class ServicesTest {

    @Inject AssertPerformersService assertPerformersService;
    @Inject BrapiService brapiService;
    @Inject CitiesService citiesService;
    @Inject WatchlistServiceImpl watchlistService;

    @InjectMock AssertPerformersUseCase assertPerformersUseCase;
    @InjectMock BrapiUseCase brapiUseCase;
    @InjectMock CitiesUseCase citiesUseCase;
    @InjectMock WatchUseCase watchUseCase;

    @Test
    @DisplayName("AssertPerformersService delega ao usecase")
    void testAssertPerformersServiceDelegates() {
        when(assertPerformersUseCase.rankAssertPerformers(null, "1m", 5))
                .thenReturn(List.of(new AssetPerformance()));
        var r = assertPerformersService.rankTopPerformers(null, "1m", 5);
        assertEquals(1, r.size());
        verify(assertPerformersUseCase).rankAssertPerformers(null, "1m", 5);
    }

    @Test
    @DisplayName("BrapiService delega ao usecase")
    void testBrapiServiceDelegates() {
        when(brapiUseCase.searchSymbols("PETR", 3)).thenReturn(List.of(BrapiQuoteItem.builder().symbol("PETR4").build()));
        var r1 = brapiService.searchSymbols("PETR", 3);
        assertEquals(1, r1.size());
        verify(brapiUseCase).searchSymbols("PETR", 3);

        when(brapiUseCase.listSymbols(1, 10, "close", "desc")).thenReturn(List.of());
        var r2 = brapiService.listSymbols(1, 10, "close", "desc");
        assertNotNull(r2);
        verify(brapiUseCase).listSymbols(1, 10, "close", "desc");
    }

    @Test
    @DisplayName("CitiesService delega ao usecase")
    void testCitiesServiceDelegates() {
        citiesService.validateById(1);
        verify(citiesUseCase).validateById(1);
        citiesService.resolveByUfAndName("SP", "Santos");
        verify(citiesUseCase).resolveByUfAndName("SP", "Santos");
    }

    @Test
    @DisplayName("WatchlistServiceImpl delega ao usecase")
    void testWatchlistServiceDelegates() {
        var r = new WatchlistItemResponse();
        when(watchUseCase.getById(10L)).thenReturn(r);
        assertSame(r, watchlistService.getById(10L));
        verify(watchUseCase).getById(10L);
    }
}
