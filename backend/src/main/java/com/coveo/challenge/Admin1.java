package com.coveo.challenge;

import com.opencsv.bean.CsvBindByName;

public class Admin1 {
    @CsvBindByName(column = "code")
    public String code;

    @CsvBindByName(column = "name")
    public String name;    
}
