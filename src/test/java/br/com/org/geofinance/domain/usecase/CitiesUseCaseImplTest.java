package br.com.org.geofinance.domain.usecase;

import br.com.org.geofinance.app.dto.response.CityInfo;
import br.com.org.geofinance.cross.exception.CitiesExceptionError;
import br.com.org.geofinance.domain.gateway.IbgeGateway;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@QuarkusTest
class CitiesUseCaseImplTest {

    @Inject
    CitiesUseCaseImpl useCase;

    @InjectMock
    IbgeGateway ibgeGateway;

    @Test
    @DisplayName("validateById retorna CityInfo ou NotFound")
    void testValidateByIdBehaviour() {
        when(ibgeGateway.findCityById(1)).thenReturn(Optional.of(new CityInfo(1, "Cidade", "SP")));
        when(ibgeGateway.findCityById(2)).thenReturn(Optional.empty());

        assertEquals("Cidade", useCase.validateById(1).getName());
        assertThrows(NotFoundException.class, () -> useCase.validateById(2));
    }

    @Test
    @DisplayName("resolveByUfAndName valida inputs, retorna único match, trata 0 e >1 matches")
    void testResolveByUfAndNameCases() {
        assertThrows(BadRequestException.class, () -> useCase.resolveByUfAndName(null, "X"));
        assertThrows(BadRequestException.class, () -> useCase.resolveByUfAndName("SP", " "));

        var c1 = new CityInfo(10, "Santos", "SP");
        var c2 = new CityInfo(11, "SANTO ANDRE", "SP");
        when(ibgeGateway.findCitiesByUf("SP")).thenReturn(List.of(c1, c2));

        assertEquals(10, useCase.resolveByUfAndName("sp", "santos").getId());

        assertThrows(NotFoundException.class, () -> useCase.resolveByUfAndName("SP", "Guarulhos"));

        var dupA = new CityInfo(20, "Taubaté", "SP");
        var dupB = new CityInfo(21, "Taubaté", "SP");
        when(ibgeGateway.findCitiesByUf("SP")).thenReturn(List.of(dupA, dupB));
        assertThrows(CitiesExceptionError.class, () -> useCase.resolveByUfAndName("SP", "Taubaté"));
    }
}
