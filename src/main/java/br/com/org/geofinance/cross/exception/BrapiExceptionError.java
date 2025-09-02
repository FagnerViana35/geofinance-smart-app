package br.com.org.geofinance.cross.exception;

import jakarta.enterprise.context.ApplicationScoped;

public class BrapiExceptionError extends Exception{
    public BrapiExceptionError(String message) {
        super(message);
    }

}
