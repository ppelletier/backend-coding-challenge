
package com.coveo.challenge;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

/*
 * Encapsulate the work done to filter a list of items, 
 * assign weight to the items, order them and deal with the paging
 * 
 * all this in a generic way
 * 
 * T is the item type to work with (ex. City)
 * Q is the query type used (see SuggestionQuery)
 *  
 */
public class SuggestionHelper<T, Q> {
    Logger logger = LoggerFactory.getLogger(SuggestionHelper.class);    
    private List<T> list;

    public SuggestionHelper(List<T> pList) {
        Assert.notNull(pList, "list cannot be null");
        list = pList;
    }

    /*
     * Simple suggestions return a list of filtered items which can also be "paged"
     * if specified in the configuration
     */
    List<T> suggest(Q pQuery, SuggestionHelperConfig<T, Q> pConfig) {        
        Assert.notNull(pQuery, "query cannot be null");
        Assert.notNull(pConfig, "config cannot be null");
        Assert.notNull(pConfig.matchFunc, "match function cannot be null");
        logger.debug("suggest({})", pQuery);

        List<T> result;
        if (pConfig.comparator != null) {
            result = list.stream().filter(i -> pConfig.matchFunc.match(i, pQuery)).sorted(pConfig.comparator)
                    .collect(Collectors.toList());
        } else {
            result = list.stream().filter(i -> pConfig.matchFunc.match(i, pQuery)).collect(Collectors.toList());
        }

        return getPage(result, pConfig);
    }

    /*
     * Weighted suggestions return a list of filtered, weighted and sorted items
     * which can also be "paged" if specified in the configuration
     */
    List<SuggestionWithWeight<T>> suggestWithWeight(Q pQuery, SuggestionHelperConfig<T, Q> pConfig) {
        Assert.notNull(pQuery, "query cannot be null");
        Assert.notNull(pConfig, "config cannot be null");
        Assert.notNull(pConfig.matchFunc, "match function cannot be null");
        Assert.notNull(pConfig.weightFunc, "weight function cannot be null");
        logger.debug("suggestWithWeight({})", pQuery);

        List<T> result = list.stream().filter(i -> pConfig.matchFunc.match(i, pQuery)).collect(Collectors.toList());

        List<SuggestionWithWeight<T>> resultWithWeight;
        if (pConfig.weightComparator != null) {
            resultWithWeight = result.stream()
                    .map(c -> new SuggestionWithWeight<T>(c, pConfig.weightFunc.weight(c, pQuery)))
                    .sorted(pConfig.weightComparator.reversed()).collect(Collectors.toList());
        } else {
            resultWithWeight = result.stream()
                    .map(c -> new SuggestionWithWeight<T>(c, pConfig.weightFunc.weight(c, pQuery))).sorted()
                    .collect(Collectors.toList());
        }

        return getPage(resultWithWeight, pConfig);
    }

    /*
     * Take a list and return the appropriate elements based on the requested pageNo
     * and pageSize or the complete list depending on what is specified in the
     * configuration
     */
    <T1> List<T1> getPage(List<T1> pList, SuggestionHelperConfig<T, Q> pConfig) {
        Assert.notNull(pList, "list cannot be null");
        Assert.notNull(pConfig, "config cannot be null");
        logger.debug("suggestWithWeight()");
        if (pConfig.usePages) {
            return pList.stream().skip((long) pConfig.pageNo * pConfig.pageSize).limit(pConfig.pageSize)
                    .collect(Collectors.toList());
        } else {
            return pList;
        }
    }
}
