package com.coveo.challenge;

public class CsvHelperException extends Exception {
    
    public CsvHelperException(String errorMessage) {
        super(errorMessage);
    }
    public CsvHelperException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
}
