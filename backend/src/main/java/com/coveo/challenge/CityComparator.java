package com.coveo.challenge;

import java.util.Comparator;
 
public class CityComparator implements Comparator<City>
{
    @Override
    public int compare(City c1, City c2) {
        return  c1.ascii.compareTo(c2.ascii);            
    }
}