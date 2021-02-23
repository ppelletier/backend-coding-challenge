package com.coveo.challenge;

import java.util.Comparator;
 
/*
 * Compares "Weighted" Cities. If the weight present for both items they are compared
 * otherwise their ascii names are used
 */
public class CityWithWeightComparator implements Comparator<SuggestionWithWeight<City>>
{
    @Override
    public int compare(SuggestionWithWeight<City> c1, SuggestionWithWeight<City> c2) {
        return c1.weight != null && c2.weight != null ? c1.weight.compareTo(c2.weight) :
            c1.item.ascii.compareTo(c2.item.ascii);
    }
}