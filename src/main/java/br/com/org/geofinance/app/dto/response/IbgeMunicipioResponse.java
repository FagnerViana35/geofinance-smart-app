package br.com.org.geofinance.app.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IbgeMunicipioResponse {

    private int id;
    private String nome;
    private Microrregiao microrregiao;

    public static class Microrregiao {
        private Mesorregiao mesorregiao;
        public Mesorregiao getMesorregiao() { return mesorregiao; }
    }
    public static class Mesorregiao {
        @JsonProperty("UF")
        private Uf uf;
        public Uf getUf() { return uf; }
    }
    public static class Uf {
        private String sigla;
        public String getSigla() { return sigla; }
    }

}
