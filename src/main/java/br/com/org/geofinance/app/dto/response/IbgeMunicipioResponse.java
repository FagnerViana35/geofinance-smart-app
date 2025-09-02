package br.com.org.geofinance.app.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IbgeMunicipioResponse {

    private int id;
    private String nome;
    private Microrregiao microrregiao;

    @Getter
    public static class Microrregiao {
        private Mesorregiao mesorregiao;
    }
    @Getter
    public static class Mesorregiao {
        @JsonProperty("UF")
        private Uf uf;
    }
    @Getter
    public static class Uf {
        private String sigla;
    }
}
