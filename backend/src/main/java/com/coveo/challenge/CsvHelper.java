package com.coveo.challenge;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.List;
import java.io.InputStreamReader;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CsvHelper<T> {
    Logger logger = LoggerFactory.getLogger(CsvHelper.class);

    private char separator = ',';
    private boolean ignoreQuotations = false;
    private boolean ignoreLeadingWhiteSpace = false;

    final Class<T> typeParameterClass;

    public CsvHelper(Class<T> pTypeParameterClass) {
        typeParameterClass = pTypeParameterClass;
    }

    public CsvHelper<T> withSeparator(char pSeparator) {
        this.separator = pSeparator;
        return this;
    }

    public CsvHelper<T> withIgnoreQuotations(boolean pIgnoreQuotations) {
        this.ignoreQuotations = pIgnoreQuotations;
        return this;
    }

    public CsvHelper<T> withIgnoreLeadingWhiteSpace(boolean pIgnoreLeadingWhiteSpace) {
        this.ignoreLeadingWhiteSpace = pIgnoreLeadingWhiteSpace;
        return this;
    }

    public List<T> readFile(String pFilePath) throws CsvHelperException {
        logger.debug("readFile: {}", pFilePath);
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(pFilePath);

        if (inputStream == null) {
            throw new CsvHelperException(String.format("Unable to open stream: %s", pFilePath));
        }
               
        try (Reader reader = new BufferedReader(new InputStreamReader(inputStream))) {

            HeaderColumnNameMappingStrategy<T> strategy = new HeaderColumnNameMappingStrategy<>();
            strategy.setType(typeParameterClass);

            CsvToBean<T> csvToBean = new CsvToBeanBuilder<T>(reader).withMappingStrategy(strategy)
                    .withSeparator(separator).withIgnoreQuotations(ignoreQuotations)
                    .withIgnoreLeadingWhiteSpace(ignoreLeadingWhiteSpace).build();

            return csvToBean.parse();

        } catch (IOException e) {
            throw new CsvHelperException(String.format("Unable to read file: %s", pFilePath), e);
        }
    }   
}
