package ru.otus.hw.health;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "management.endpoint.health.custom-settings")
public class HealthCheckSettings {

    private int createNewBookCheckingInterval;

}
