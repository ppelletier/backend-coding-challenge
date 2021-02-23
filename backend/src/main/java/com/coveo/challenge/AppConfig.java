package com.coveo.challenge;

import org.springframework.util.Assert;

public class AppConfig {

    /*
     * Functions used to configure the SuggestionHelper
     */

    /*
    * Function used to filter the cities
    */
    public static MatchFunction<City, SuggestionQuery> contains = (c, sq) -> StringHelper.normalize(sq.query).equals("") ? false : StringHelper.normalize(c.ascii).contains(StringHelper.normalize(sq.query));
    public static MatchFunction<City, SuggestionQuery> startsWith = (c, sq) -> StringHelper.normalize(c.ascii).startsWith(StringHelper.normalize(sq.query));
    

    /*
    * Function used to 'weight' the cities
    */
    public static WeightFunction<City, SuggestionQuery> weight = (c, sq) -> calculateWeight(c, sq);    

    /*
     * calculate the weight of the suggestion as follow
     *   .5 is given if the city name and the query are equals else 0
     * 
     * to this value we add:
     * 
     *   .5 if the city distance is less than 20 km 
     *   .25 if the city distance is less than 500 km
     *   otherwise 1 divided by the distance so the closer the higher the value
     */
    static Double calculateWeight(City c, SuggestionQuery sq)
    {
        Assert.notNull(c, "city cannot be null");
        Assert.notNull(sq, "SuggestionQuery cannot be null");

        if (sq.latitude != null && sq.longitude != null) {
            double distance = c.distance(sq.latitude, sq.longitude);
            
            double d =  distance < 20 ? 0.5 
                : distance < 500 ? 0.25 
                : 1d / distance;
            return (StringHelper.normalize(c.ascii).equals(StringHelper.normalize(sq.query)) ? 0.5 : 0) + d;                 
        } else {
            return null;
        }
    }

    private AppConfig(){}
}
