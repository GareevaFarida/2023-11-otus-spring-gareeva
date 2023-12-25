package ru.otus.spring.hw03.config;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.Map;

@Data
@Component
@ConfigurationProperties(prefix = "test")
public class AppProperties implements TestFileNameProvider, TestConfig, LocaleConfig {

    private int minAcceptCount;

    private Locale locale;

    Map<Locale, String> fileNameByLocaleTag;

    Formatter formatter;

    @Override
    public String getTestFileName() {
        return fileNameByLocaleTag.get(locale);
    }

    @Override
    public int getRightAnswersCountToPass() {
        return minAcceptCount;
    }

}
