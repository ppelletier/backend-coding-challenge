package com.coveo.challenge;

/*
 * Encapsulate the data used to perform a query with the SuggestionHelper
 */
public class SuggestionQuery {
    public static final int SEARCH_STARTSWITH = 1;
    public static final int SEARCH_CONTAINS = 2;

    public String query;
    public Double longitude;
    public Double latitude;
    public int searchType = SEARCH_CONTAINS;
    
    public SuggestionQuery withQuery(String pQuery)
    {
        this.query = pQuery;
        return this;
    }

    public SuggestionQuery withLatitude(Double pLatitude)
    {
        this.latitude = pLatitude;
        return this;
    }

    public SuggestionQuery withLongitude(Double pLongitude)
    {
        this.longitude = pLongitude;
        return this;
    }

    public SuggestionQuery withLatLong(Double pLatitude, Double pLongitude)
    {
        this.latitude = pLatitude;
        this.longitude = pLongitude;
        return this;
    }

    public SuggestionQuery withSearchType(int pSearchType)
    {
        this.searchType = pSearchType;
        return this;
    }
}
