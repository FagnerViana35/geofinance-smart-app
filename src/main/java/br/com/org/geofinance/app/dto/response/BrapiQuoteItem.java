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
    private String stock; // symbol
    // nome curto da companhia/fundo/etf
    private String name; // shortName
    // nome completo
    private String longName; // longName
    // moeda de negociação (ex.: BRL, USD)
    private String currency; // currency
    // último preço de negociação
    private Double close; // regularMarketPrice
    // preço máximo do dia
    private Double high; // regularMarketDayHigh
    // preço mínimo do dia
    private Double low; // regularMarketDayLow
    // variação em valor absoluto
    private Double changeValue; // regularMarketChange
    // variação percentual
    private Double changePercent; // regularMarketChangePercent
    // horário da última atualização
    private String marketTime; // regularMarketTime
    // valor de mercado (market cap)
    private Long marketCap; // marketCap
    // volume de negociações
    private Long volume; // regularMarketVolume
    // setor (quando disponível em outra resposta do brapi)
    private String sector;
    // segmento (quando disponível em outra resposta do brapi)
    private String segment;
    // URL do logo
    private String logoUrl; // logourl
}

