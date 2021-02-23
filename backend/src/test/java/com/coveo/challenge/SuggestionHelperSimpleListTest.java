/**
 * Copyright (c) 2011 - 2019, Coveo Solutions Inc.
 */
package com.coveo.challenge;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

public class SuggestionHelperSimpleListTest {

    List<String> list = List.of("a", "ab", "bc");
    List<City> cities = List.of(new City(6325494, "Québec", "Quebec", 46.81228f, -71.21454f, "CA", ""),
            new City(6325494, "Québec", "Quebec", 38.402136f, -122.823881f, "US", ""),
            new City(6325521, "Lévis", "Levis", 46.80326f, -71.17793f, "CA", ""),
            new City(6325529, "Montmagny", "Montmagny", 46.98043f, -70.55493f, "CA", ""),
            new City(6354895, "Rimouski", "Rimouski", 48.44879f, -68.52396f, "CA", ""),
            new City(6325496, "Québec", "Quebec", 47.81228f, -71.21454f, "CA", ""),
            new City(6325497, "Québec", "Quebec", 48.81228f, -71.21454f, "CA", ""),
            new City(6325498, "Québec", "Quebec", 49.81228f, -71.21454f, "CA", ""),
            new City(6325499, "Québec", "Quebec", 50.81228f, -71.21454f, "CA", ""));

    SuggestionHelper<String, String> suggestor = new SuggestionHelper<>(list);

    @Test
    public void TestContains() {
        MatchFunction<String, String> contains = (a, b) -> a.contains(b);

        SuggestionHelperConfig<String, String> config = new SuggestionHelperConfig<>();

        config.withMatch(contains);

        List<String> r = suggestor.suggest("b", config);

        assertEquals("[ab, bc]", r.toString());
    }

    @Test
    public void TestStartsWith() {
        MatchFunction<String, String> startsWith = (a, b) -> a.startsWith(b);

        SuggestionHelperConfig<String, String> config = new SuggestionHelperConfig<>();
        config.withMatch(startsWith);

        List<String> r = suggestor.suggest("a", config);

        assertEquals("[a, ab]", r.toString());
    }

    @Test
    public void TestQuery() {
        MatchFunction<City, SuggestionQuery> match = (c, sq) -> c.ascii.toLowerCase().contains(sq.query.toLowerCase());

        SuggestionQuery sq = new SuggestionQuery().withQuery("qu").withLatLong(46.813878, -71.207981);
        SuggestionHelperConfig<City, SuggestionQuery> shConfig = new SuggestionHelperConfig<City, SuggestionQuery>()
                .withMatch(match);

        SuggestionHelper<City, SuggestionQuery> sh = new SuggestionHelper<>(cities);
        List<City> result = sh.suggest(sq, shConfig);

        assertEquals(5, result.size());
        assertEquals("Quebec", result.get(0).ascii);
    }

    @Test
    public void TestQueryWithWeight() throws Exception {
        SuggestionQuery sq = new SuggestionQuery().withQuery("quebec").withLatLong(46.813878, -71.207981);

        SuggestionHelperConfig<City, SuggestionQuery> shConfig = new SuggestionHelperConfig<City, SuggestionQuery>()
                .withMatch(AppConfig.contains).withWeight(AppConfig.weight)
                .withWeightComparator(new CityWithWeightComparator());

        SuggestionHelper<City, SuggestionQuery> sh = new SuggestionHelper<>(cities);
        List<SuggestionWithWeight<City>> result = sh.suggestWithWeight(sq, shConfig);

        assertEquals(5, result.size());
        assertEquals("Quebec", result.get(0).item.ascii);
        assertEquals("CA", result.get(0).item.country);
    }

    @Test
    public void TestQueryWithWeightNoPages() throws Exception {
        SuggestionQuery sq = new SuggestionQuery().withQuery("e").withLatLong(46.813878, -71.207981);

        SuggestionHelperConfig<City, SuggestionQuery> shConfig = new SuggestionHelperConfig<City, SuggestionQuery>()
                .withMatch(AppConfig.contains).withWeight(AppConfig.weight)
                .withWeightComparator(new CityWithWeightComparator()).withUsePages(false);

        SuggestionHelper<City, SuggestionQuery> sh = new SuggestionHelper<>(cities);
        List<SuggestionWithWeight<City>> result = sh.suggestWithWeight(sq, shConfig);

        // we should get all the cities and not only a page
        assertEquals(7, result.size());
    }

    @Test
    public void TestQueryWithWeightWithPages() throws Exception {
        SuggestionQuery sq = new SuggestionQuery().withQuery("e").withLatLong(46.813878, -71.207981);

        SuggestionHelperConfig<City, SuggestionQuery> shConfig = new SuggestionHelperConfig<City, SuggestionQuery>()
                .withMatch(AppConfig.contains).withWeight(AppConfig.weight)
                .withWeightComparator(new CityWithWeightComparator()).withUsePages(true);

        SuggestionHelper<City, SuggestionQuery> sh = new SuggestionHelper<>(cities);
        List<SuggestionWithWeight<City>> result = sh.suggestWithWeight(sq, shConfig);

        // We should get only a page of size 5
        assertEquals(5, result.size());
    }

    @Test
    public void TestQueryWithWeightWithPage1() throws Exception {
        SuggestionQuery sq = new SuggestionQuery().withQuery("e").withLatLong(46.813878, -71.207981);

        SuggestionHelperConfig<City, SuggestionQuery> shConfig = new SuggestionHelperConfig<City, SuggestionQuery>()
                .withMatch(AppConfig.contains).withWeight(AppConfig.weight)
                .withWeightComparator(new CityWithWeightComparator()).withUsePages(true).withPageNo(1);

        SuggestionHelper<City, SuggestionQuery> sh = new SuggestionHelper<>(cities);
        List<SuggestionWithWeight<City>> result = sh.suggestWithWeight(sq, shConfig);

        // We should get only a page of size 2 (the second page)
        assertEquals(2, result.size());
    }

    List<SuggestionWithWeight<City>> getSuggestions(String queryText)
    {

        SuggestionQuery sq = new SuggestionQuery()
        .withQuery(queryText)
        .withLatLong(46.813878, -71.207981);

        SuggestionHelperConfig<City, SuggestionQuery> shConfig = new SuggestionHelperConfig<City, SuggestionQuery>()
        .withMatch(AppConfig.contains)
        .withWeight(AppConfig.weight)
        .withWeightComparator(new CityWithWeightComparator());

        SuggestionHelper<City, SuggestionQuery> sh = new SuggestionHelper<>(cities);
        List<SuggestionWithWeight<City>> result = sh.suggestWithWeight(sq, shConfig);

    return result;    
    }

    @Test
    public void TestEmptyQuery() throws Exception {
        List<SuggestionWithWeight<City>> result = getSuggestions("");

        assertEquals(0, result.size());
    }
}
