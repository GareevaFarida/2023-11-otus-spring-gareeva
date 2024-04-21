package ru.otus.hw.hw15.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.Router;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.MessageChannelSpec;
import org.springframework.integration.dsl.MessageChannels;
import ru.otus.hw.hw15.models.Child;
import ru.otus.hw.hw15.services.educationservices.HighSchool;
import ru.otus.hw.hw15.services.educationservices.Hogwards;
import ru.otus.hw.hw15.services.educationservices.Kindergarten;
import ru.otus.hw.hw15.services.educationservices.PrimarySchool;

import static org.springframework.integration.dsl.MessageChannels.publishSubscribe;

@Configuration
public class IntegrationConfig {

    @Bean
    public MessageChannelSpec<?, ?> babyChannel() {
        return MessageChannels.queue(100);
    }

    @Bean
    public MessageChannelSpec<?, ?> employeeChannel() {
        return publishSubscribe();
    }

    @Bean
    public MessageChannelSpec<?, ?> hogwardsChannel() {
        return MessageChannels.queue(100);
    }

    @Bean
    public MessageChannelSpec<?, ?> commonSchoolChannel() {
        return MessageChannels.queue(100);
    }

    @Router(inputChannel = "splitWizardsAndMuggles")
    public String resolvePupilsChannel(Child child) {
        return (child.isMagicalAbility()) ? "hogwardsChannel" : "commonSchoolChannel";
    }


    @Bean
    public IntegrationFlow babyFlow(Kindergarten kindergarten, PrimarySchool primarySchool) {
        return IntegrationFlow.from(babyChannel())
                .split()
                .handle(kindergarten, "teach")
                .handle(primarySchool, "teach")
                .route(this, "resolvePupilsChannel")
                .get();
    }

    @Bean
    public IntegrationFlow wizardFlow(Hogwards hogwards) {
        return IntegrationFlow.from(hogwardsChannel())
                .handle(hogwards, "teach")
                .channel(employeeChannel())
                .get();
    }

    @Bean
    public IntegrationFlow muggleFlow(HighSchool school) {
        return IntegrationFlow.from(commonSchoolChannel())
                .handle(school, "teach")
                .channel(employeeChannel())
                .get();
    }
}
