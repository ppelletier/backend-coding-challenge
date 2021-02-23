package com.coveo.challenge;

import com.opencsv.bean.CsvBindByName;

/**
 * Class representing a city and it's attributes.
 */
public class City
{
    /**
     * Constructor required by OpenCSV bean creation.
     */
    public City()
    {}

    /**
     * Constructor used for testing
     */
    public City(Integer pId, String pName, String pAscii, float pLat, float pLong, String pCountry, String pStateOrProvince)
    {
        this.id = pId;
        this.name = pName;
        this.ascii = pAscii;
        this.latitude = pLat;
        this.longitude = pLong;
        this.country = pCountry;
        this.admin1 = pStateOrProvince;
    }

    public City(City pCity, Double pScore)
    {
        this.id = pCity.id;
        this.name =pCity.name;
        this.ascii = pCity.ascii;
        this.latitude = pCity.latitude;
        this.longitude = pCity.longitude;
        this.country = pCity.country;        
        this.score = pScore;
    }

    
    /**
     * The unique id of the city.
     */
    @CsvBindByName(column = "id")
    public Integer id;

    /**
     * The city name.
     */
    @CsvBindByName(column = "name")
    public String name;

    /**
     * The city name in ascii (for foreign regions).
     */
    @CsvBindByName(column = "ascii")
    public String ascii;
    
    /**
     * The latitude.
     */
    @CsvBindByName(column = "lat")
    public Float latitude;

    /**
     * The longitude.
     */
    @CsvBindByName(column = "long")
    public Float longitude;

    /**
     * The country code.
     */
    @CsvBindByName(column = "country")
    public String country;

    /**
     * The state or province.
     */
    @CsvBindByName(column = "admin1")
    public String admin1;
    
    /**
     * calculate the distance in km between the city and the given lat/long
     */
    public double distance(double lat2, double lon2) 
    { 
  
        // The math module contains a function 
        // named toRadians which converts from 
        // degrees to radians. 
        double lon1 = Math.toRadians(longitude); 
        lon2 = Math.toRadians(lon2); 
        double lat1 = Math.toRadians(latitude); 
        lat2 = Math.toRadians(lat2); 
  
        // Haversine formula  
        double dlon = lon2 - lon1;  
        double dlat = lat2 - lat1; 
        double a = Math.pow(Math.sin(dlat / 2), 2) 
                 + Math.cos(lat1) * Math.cos(lat2) 
                 * Math.pow(Math.sin(dlon / 2),2); 
              
        double c = 2 * Math.asin(Math.sqrt(a)); 
  
        // Radius of earth in kilometers. 
        double r = 6371; 
  
        // calculate the result 
        return(c * r); 
    }
}
