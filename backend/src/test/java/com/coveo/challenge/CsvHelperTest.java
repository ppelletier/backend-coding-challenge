package com.coveo.challenge;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

public class CsvHelperTest {

    @Test
    public void TestSmallTsvSize() throws Throwable {
        CsvHelper<City> csvHelper = new CsvHelper<City>(City.class).withSeparator('\t');

        List<City> l = csvHelper.readFile("data/cities_canada-usa-small.tsv");

        assertEquals(99, l.size());
        City c = l.get(0);
        assertEquals("Abbotsford", c.ascii);
        assertEquals("CA", c.country);
        assertEquals(Integer.valueOf(5881791), c.id);
        assertEquals(49.05798d, c.latitude,0.00001);
        assertEquals(-122.25257d, c.longitude, 0.00001);
        assertEquals("Abbotsford", c.name);
        assertEquals("02", c.admin1);    
    }

    @Test
    public void TestTsvSize() throws Throwable {
        CsvHelper<City> csvHelper = new CsvHelper<City>(City.class)
            .withSeparator('\t')
            .withIgnoreQuotations(true);

        List<City> l = csvHelper.readFile("data/cities_canada-usa.tsv");
        assertEquals(7237, l.size());
    }
}
