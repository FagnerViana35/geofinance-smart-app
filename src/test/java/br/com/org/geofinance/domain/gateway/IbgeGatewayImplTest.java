package br.com.org.geofinance.domain.gateway;

import br.com.org.geofinance.app.dto.response.CityInfo;
import br.com.org.geofinance.app.dto.response.IbgeMunicipioResponse;
import br.com.org.geofinance.app.dto.response.IbgeMunicipioResponse.Microrregiao;
import br.com.org.geofinance.app.dto.response.IbgeMunicipioResponse.Mesorregiao;
import br.com.org.geofinance.app.dto.response.IbgeMunicipioResponse.Uf;
import br.com.org.geofinance.infra.restClient.IbgeClient;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@QuarkusTest
class IbgeGatewayImplTest {

    private final IbgeClient client = mock(IbgeClient.class);
    private final IbgeGatewayImpl gateway = new IbgeGatewayImpl(client);

    private IbgeMunicipioResponse municipio(int id, String nome, String ufSigla) {
        Uf uf = new Uf();
        uf.setSigla(ufSigla);

        Mesorregiao meso = new Mesorregiao();
        meso.setUf(uf);

        Microrregiao micro = new Microrregiao();
        micro.setMesorregiao(meso);

        IbgeMunicipioResponse res = new IbgeMunicipioResponse();
        res.setId(id);
        res.setNome(nome);
        res.setMicrorregiao(micro);
        return res;
    }

    @Test
    @DisplayName("findCityById retorna CityInfo quando encontrado")
    void testFindCityByIdOk() {
        when(client.getMunicipioById(3550308))
                .thenReturn(municipio(3550308, "São Carlos", "SP"));

        Optional<CityInfo> result = gateway.findCityById(3550308);

        assertTrue(result.isPresent());
        assertEquals(3550308, result.get().getId());
        assertEquals("São Carlos", result.get().getName());
        assertEquals("SP", result.get().getUf());
    }

    @Test
    @DisplayName("findCityById retorna vazio quando client retorna null")
    void testFindCityByIdNull() {
        when(client.getMunicipioById(123)).thenReturn(null);

        Optional<CityInfo> result = gateway.findCityById(123);

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("findCityById retorna vazio quando ocorre exceção")
    void testFindCityByIdException() {
        when(client.getMunicipioById(999))
                .thenThrow(new RuntimeException("erro"));

        Optional<CityInfo> result = gateway.findCityById(999);

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("findCitiesByUf retorna lista de cidades quando encontrado")
    void testFindCitiesByUfOk() {
        when(client.listMunicipiosByUf("SP"))
                .thenReturn(List.of(
                        municipio(3550308, "São Carlos", "SP"),
                        municipio(3552205, "Ribeirão Preto", "SP")
                ));

        List<CityInfo> result = gateway.findCitiesByUf("sp");

        assertEquals(2, result.size());
        assertEquals("São Carlos", result.get(0).getName());
        assertEquals("SP", result.get(0).getUf());
    }

    @Test
    @DisplayName("findCitiesByUf retorna lista vazia quando UF é nula ou em branco")
    void testFindCitiesByUfBlank() {
        assertTrue(gateway.findCitiesByUf(null).isEmpty());
        assertTrue(gateway.findCitiesByUf("   ").isEmpty());
    }

    @Test
    @DisplayName("findCitiesByUf retorna vazio quando client retorna lista vazia")
    void findCitiesByUf_empty_list() {
        when(client.listMunicipiosByUf("RJ")).thenReturn(List.of());

        List<CityInfo> result = gateway.findCitiesByUf("RJ");

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("findCitiesByUf retorna vazio quando ocorre exceção")
    void testFindCitiesByUfException() {
        when(client.listMunicipiosByUf("MG"))
                .thenThrow(new RuntimeException("erro"));

        List<CityInfo> result = gateway.findCitiesByUf("MG");

        assertTrue(result.isEmpty());
    }
}
