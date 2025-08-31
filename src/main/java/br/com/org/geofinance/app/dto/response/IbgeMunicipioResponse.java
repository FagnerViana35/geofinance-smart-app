package br.com.org.geofinance.app.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class IbgeMunicipioResponse {

    private int id;
    private String nome;
    private Microrregiao microrregiao;

    public int getId() { return id; }
    public String getNome() { return nome; }
    public Microrregiao getMicrorregiao() { return microrregiao; }

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
