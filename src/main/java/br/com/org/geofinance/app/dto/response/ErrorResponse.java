package br.com.org.geofinance.app.dto.response;

import java.time.OffsetDateTime;

public class ErrorResponse {
    private String error;
    private String message;
    private int status;
    private String path;
    private OffsetDateTime timestamp;

}
