package ru.otus.spring.hw04.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.Map;

@Data
@Component
@ConfigurationProperties(prefix = "test")
public class AppProperties implements TestFileNameProvider, TestConfig, LocaleConfig, FormatterPropsProvider {

    private int minAcceptCount;

    private Locale locale;

    private Map<Locale, String> fileNameByLocaleTag;

    private FormatterProperties formatterProperties;

    @Override
    public String getTestFileName() {
        return fileNameByLocaleTag.get(locale);
    }

    @Override
    public int getRightAnswersCountToPass() {
        return minAcceptCount;
    }

}
