package com.coveo.challenge;

import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.hibernate.validator.internal.util.logging.Log_.logger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.MethodNotAllowedException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class SuggestionsResource {
    Logger logger = LoggerFactory.getLogger(SuggestionsResource.class);

    private List<City> cities;
    private static String cityFilePath = "data/cities_canada-usa.tsv";

    private List<Admin1> admin1List;
    private static String admin1FilePath = "data/Admin1CodesASCII.tsv";

    private void loadCities() throws CsvHelperException {
        CsvHelper<City> csvHelper = new CsvHelper<>(City.class).withSeparator('\t').withIgnoreQuotations(true);
        cities = csvHelper.readFile(cityFilePath);
    }

    private void loadAdmin1() throws CsvHelperException {
        CsvHelper<Admin1> csvHelper = new CsvHelper<>(Admin1.class).withSeparator('\t').withIgnoreQuotations(true);
        admin1List = csvHelper.readFile(admin1FilePath);
    }

    /*
     * Load the cities information from the tsv file if not already loaded
     */
    public List<City> getCities() throws CsvHelperException {
        if (cities == null) {
            loadCities();
        }
        return cities;
    }

    public List<Admin1> getAdmin1List() throws CsvHelperException {
        if (admin1List == null) {
            loadAdmin1();
        }
        return admin1List;
    }

    private String findAdmin1Name(City pCity) {
        String code = String.format("%s.%s", pCity.country, pCity.admin1);

        List<Admin1> list = null;
        try {
            list = getAdmin1List().stream().filter(a -> a.code.equals(code)).collect(Collectors.toList());
        } catch (CsvHelperException e) {
            // Last minute change...
            // TODO: fix this later...
            logger.error("Unable to load admin1 file: {}", admin1FilePath);
        }

        if (list != null && !list.isEmpty()) {
            return list.get(0).name;
        } else {
            return "";
        }
    }

    @CrossOrigin(origins = "*")
    @RequestMapping("/suggestions")
    public String suggestions(@RequestParam String q,
            @RequestParam(defaultValue = "", required = false) Double latitude,
            @RequestParam(defaultValue = "", required = false) Double longitude,
            @RequestParam(required = false) Integer page) {
        logger.info(String.format("--- Entering suggestions endpoint parameters are: q=%s , latitude=%f, longitude=%f",
                q, latitude, longitude));

        try {

            SuggestionQuery sq = new SuggestionQuery().withQuery(StringHelper.normalize(q).trim()).withLatLong(latitude,
                    longitude);

            SuggestionHelperConfig<City, SuggestionQuery> shConfig = new SuggestionHelperConfig<City, SuggestionQuery>()
                    .withMatch(sq.searchType == SuggestionQuery.SEARCH_STARTSWITH ? AppConfig.startsWith
                            : AppConfig.contains)
                    .withWeight(AppConfig.weight).withWeightComparator(new CityWithWeightComparator());

            SuggestionHelper<City, SuggestionQuery> sh = new SuggestionHelper<>(getCities());
            List<SuggestionWithWeight<City>> result = sh.suggestWithWeight(sq, shConfig);

            // Copy the list of cities and assign their weight
            List<CitySearchResult> cityList = result.stream()
                    .map(c -> new CitySearchResult(c.item, findAdmin1Name(c.item), c.weight))
                    .collect(Collectors.toList());

            Map<String, Object> results = new HashMap<>();
            results.put("cities", cityList);

            String requestResult = new ObjectMapper().writeValueAsString(results);

            logger.info("Result : {}", requestResult);

            return requestResult;
        }
        // Catches all exceptions, log it and return a dummy city indicating an error
        // occured
        catch (InvalidParameterException | MethodNotAllowedException | JsonProcessingException | CsvHelperException e) {
            logger.error("Unexpected error : {} - {}", e.getMessage(), e.getStackTrace());

            return "{\"cities\":[{\"id\":-1,\"name\":\"an error has occured contact...\",\"latitude\":46.81228,\"longitude\":-71.21454,\"Score\":\"0\"}]}";
        }
    }
}
