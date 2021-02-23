package com.coveo.challenge;

/**
 * Class representing the city result.
 */
public class CitySearchResult {
    public CitySearchResult(City pCity, String pAdmin1, Double pWeight) {
       
        String countryName = pCity.country.equals("CA") ? "Canada" : (pCity.country.equals("US") ? "USA" : pCity.country);

        this.name = String.format("%s, %s, %s", pCity.name, pAdmin1,  countryName);
        
        this.latitude = pCity.latitude;
        this.longitude = pCity.longitude;        
        this.score = pWeight;
    }

    /**
     * The city name.
     */
    public String name;

    /**
     * The latitude.
     */    
    public Float latitude;

    /**
     * The longitude.
     */

    public Float longitude;
    /**
     * The score.
     */
    public Double score;
}
