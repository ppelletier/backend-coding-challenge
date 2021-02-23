package com.coveo.challenge;

import java.io.IOException;
import java.io.Reader;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

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

        URL url = ClassLoader.getSystemResource(pFilePath);

        if (url == null) {
            throw new CsvHelperException(String.format("Unable to access file: %s", pFilePath));
        }

        Path path;
        try {
            path = Paths.get(url.toURI());
        } catch (URISyntaxException e) {
            throw new CsvHelperException(String.format("Unable to read file: %s", pFilePath), e);
        }
        try (Reader reader = Files.newBufferedReader(path)) {

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
