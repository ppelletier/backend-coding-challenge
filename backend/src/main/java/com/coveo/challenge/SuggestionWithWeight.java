package com.coveo.challenge;

/*
 * Associate an item with its calculated weight.
 * This is used and returned by the SuggestionHelper
 */
public class SuggestionWithWeight<T> {
    public T item;
    public Double weight;

    public SuggestionWithWeight(T pItem, Double pWeight)
    {
        this.item = pItem;
        this.weight = pWeight;
    }    
}
