package com.coveo.challenge;

public class SuggestionHelperException extends Exception {
    public SuggestionHelperException(String errorMessage) {
        super(errorMessage);
    }
    public SuggestionHelperException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
}
