package ru.otus.spring.hw03.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.Map;

@Data
@Component
@ConfigurationProperties(prefix = "test")
public class AppProperties implements TestFileNameProvider, TestConfig, LocaleConfig, Formatter {

    private int minAcceptCount;

    private Locale locale;

    private Map<Locale, String> fileNameByLocaleTag;

    private FormatterRecord formatter;

    @Override
    public String getTestFileName() {
        return fileNameByLocaleTag.get(locale);
    }

    @Override
    public int getRightAnswersCountToPass() {
        return minAcceptCount;
    }

    @Override
    public FormatterRecord getFormatter() {
        return formatter;
    }
}
