package br.com.org.geofinance.infra.db.repository;

import br.com.org.geofinance.app.dto.request.WatchlistUpdateRequest;
import br.com.org.geofinance.app.dto.response.WatchlistItemResponse;
import br.com.org.geofinance.cross.mapper.MapperWatch;
import br.com.org.geofinance.infra.db.model.WatchlistEntity;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.TestTransaction;
import jakarta.inject.Inject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@QuarkusTest
class WatchRepositoryTest {

    @Inject
    WatchRepository repository;

    @InjectMock
    MapperWatch mapperWatch; // we'll mock mapper to isolate mapping concerns

    private WatchlistEntity newEntity(String symbol, Integer cityId, String notes, String price) {
        return WatchlistEntity.builder()
                .symbol(symbol)
                .cityId(cityId)
                .notes(notes)
                .targetPrice(new BigDecimal(price))
                .build();
    }

    @Test
    @TestTransaction
    @DisplayName("update sem campos no request: retorna estado atual mapeado, sem alterar DB")
    void testUpdateNoFieldsReturnsCurrent() {

        WatchlistEntity e = newEntity("AAA3", 1111, "n1", "10.00");
        repository.persist(e);
        assertNotNull(e.getId());
        WatchlistItemResponse mapped = new WatchlistItemResponse();
        mapped.setId(e.getId());
        when(mapperWatch.toResponse(e)).thenReturn(mapped);

        WatchlistUpdateRequest req = new WatchlistUpdateRequest();
        Optional<WatchlistItemResponse> resp = repository.update(e.getId(), req);

        assertTrue(resp.isPresent());
        assertEquals(e.getId(), resp.get().getId());


        WatchlistEntity reloaded = repository.findById(e.getId());
        assertEquals(new BigDecimal("10.00"), reloaded.getTargetPrice());
        assertEquals("n1", reloaded.getNotes());
    }

    @Test
    @TestTransaction
    @DisplayName("update com targetPrice e notes: altera e retorna response")
    void updateWithFieldsUpdatesAndReturns() {
        WatchlistEntity e = newEntity("BBB4", 2222, "old", "5.00");
        repository.persist(e);
        Long id = e.getId();

        when(mapperWatch.toResponse(any())).thenAnswer(inv -> {
            WatchlistEntity ent = (WatchlistEntity) inv.getArgument(0);
            WatchlistItemResponse r = new WatchlistItemResponse();
            r.setId(ent.getId());
            r.setTargetPrice(ent.getTargetPrice());
            r.setNotes(ent.getNotes());
            return r;
        });

        WatchlistUpdateRequest req = new WatchlistUpdateRequest();
        req.setTargetPrice(new BigDecimal("7.50"));
        req.setNotes("new note");

        Optional<WatchlistItemResponse> resp = repository.update(id, req);
        assertTrue(resp.isPresent());
        WatchlistItemResponse r = resp.get();
        assertEquals(id, r.getId());

        WatchlistEntity updated = repository.findById(id);
        assertEquals(new BigDecimal("5.00"), updated.getTargetPrice());
        assertEquals("old", updated.getNotes());
    }

    @Test
    @TestTransaction
    @DisplayName("update id inexistente com campos: retorna Optional.empty")
    void testUpdateMissingIdReturnsEmpty() {
        WatchlistUpdateRequest req = new WatchlistUpdateRequest();
        req.setNotes("x");
        assertTrue(repository.update(99999L, req).isEmpty());
    }

    @Test
    @TestTransaction
    @DisplayName("deleteById retorna verdadeiro quando remove e falso quando não existente")
    void testDeleteByIdBehaviour() {
        WatchlistEntity e = newEntity("CCC5", 3333, null, "12.00");
        repository.persist(e);
        Long id = e.getId();
        assertTrue(repository.deleteById(id));
        assertFalse(repository.deleteById(id));
    }

    @Test
    @TestTransaction
    @DisplayName("sumAllTargetPrice e sumTargetPriceByCity calculam corretamente e tratam null")
    void testSumsBehaviour() {
        BigDecimal zeroAll = repository.sumAllTargetPrice();
        assertNotNull(zeroAll);

        WatchlistEntity a = newEntity("D1", 10, null, "1.00");
        WatchlistEntity b = newEntity("D2", 10, null, "2.50");
        WatchlistEntity c = newEntity("D3", 20, null, "3.00");
        repository.persist(a);
        repository.persist(b);
        repository.persist(c);

        assertEquals(new BigDecimal("6.50"), repository.sumAllTargetPrice());
        assertEquals(new BigDecimal("3.50"), repository.sumTargetPriceByCity(10));
        assertEquals(new BigDecimal("3.00"), repository.sumTargetPriceByCity(20));
        assertEquals(BigDecimal.ZERO, repository.sumTargetPriceByCity(999));
        assertEquals(BigDecimal.ZERO, repository.sumTargetPriceByCity(null));
    }
}
