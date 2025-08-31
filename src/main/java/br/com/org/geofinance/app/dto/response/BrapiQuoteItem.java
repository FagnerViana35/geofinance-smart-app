package br.com.org.geofinance.app.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class BrapiQuoteItem {
    // ticker (ex.: PETR4, VALE3)
    private String stock;
    // nome da companhia/fundo/etf
    private String name;
    // Campos comuns úteis (presentes em muitas respostas do brapi)
    private Double close;     // último preço de fechamento
    private Double change;    // variação %
    private String sector;    // setor (quando disponível)
    private String segment;   // segmento (quando disponível)

}
