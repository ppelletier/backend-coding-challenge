package com.coveo.challenge;

import java.util.Comparator;

interface WeightFunction<T, Q> {
    double weight(T pItem, Q pQuery);
}

interface MatchFunction<T, Q> {
    boolean match(T pItem, Q pQuery);
}

/*
 * Configuration for the SuggestionHelper
 * 
 * T is the item type the SuggestionHelper is working with (ex. City)
 * Q is the query type used (see SuggestionQuery)
 * 
 * It also contains the functions used to: match, weight and compare items 
 * Also the info used to perform paging or not
 */
public class SuggestionHelperConfig<T, Q> {
    public MatchFunction<T, Q> matchFunc;
    public WeightFunction<T, Q> weightFunc;
    public Comparator<SuggestionWithWeight<T>> weightComparator;
    public Comparator<T> comparator;
    public int pageSize = 5;
    public int pageNo = 0;
    public boolean usePages = true;

    public SuggestionHelperConfig<T, Q> withPageSize(int pPageSize)
    {        
        this.pageSize = pPageSize;
        return this;
    }

    public SuggestionHelperConfig<T, Q> withPageNo(int pPageNo)
    {
        this.pageNo = pPageNo;
        return this;
    }

    public SuggestionHelperConfig<T, Q> withUsePages(boolean pUsedPages)
    {
        this.usePages = pUsedPages;
        return this;
    }

    public SuggestionHelperConfig<T, Q> withMatch(MatchFunction<T, Q> pMatchFunc)
    {
        this.matchFunc = pMatchFunc;
        return this;
    }

    public SuggestionHelperConfig<T, Q> withWeight(WeightFunction<T, Q> pWeightFunc)
    {
        weightFunc = pWeightFunc;
        return this;
    }

    public SuggestionHelperConfig<T, Q> withComparator(Comparator<T> pComparator)
    {
        this.comparator = pComparator;
        return this;
    }

    public SuggestionHelperConfig<T, Q> withWeightComparator(Comparator<SuggestionWithWeight<T>> pComparator)
    {
        this.weightComparator = pComparator;
        return this;
    }
}
