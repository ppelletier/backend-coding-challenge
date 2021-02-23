package com.coveo.challenge;

import java.text.Normalizer;

public class StringHelper {
    /*
     * Replace all accented characters from the string with 
     * their unaccented version and put it in lowercase
     */
    static String normalize(String s)
    {
        return Normalizer
				.normalize(s, Normalizer.Form.NFD)
				.replaceAll("[^\\p{ASCII}]", "").toLowerCase();
    }
    
    private StringHelper() {}
}
