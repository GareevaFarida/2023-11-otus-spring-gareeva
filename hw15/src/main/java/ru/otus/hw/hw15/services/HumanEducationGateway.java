package ru.otus.hw.hw15.services;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import ru.otus.hw.hw15.models.Baby;
import ru.otus.hw.hw15.models.Child;

import java.util.Collection;

@MessagingGateway
public interface HumanEducationGateway {
    @Gateway(requestChannel = "babyChannel", replyChannel = "splitWizardsAndMuggles")
    Collection<Child> humanEducationProcess(Collection<Baby> babies);

}
