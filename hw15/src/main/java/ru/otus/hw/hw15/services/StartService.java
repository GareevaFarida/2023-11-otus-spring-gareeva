package ru.otus.hw.hw15.services;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import ru.otus.hw.hw15.models.Baby;

import java.util.LinkedList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StartService implements CommandLineRunner {

    private final HumanEducationGateway gateway;

    @Override
    public void run(String... args) throws Exception {
        List<Baby> babies = new LinkedList<>();
        for (int i = 0; i < 15; ++i) {
            babies.add(new Baby("Name%d".formatted(i)));
        }
        babies.add(Baby.createWizard("Гарри Поттер"));
        babies.add(Baby.createWizard("Драко Малфой"));
        gateway.humanEducationProcess(babies);
    }
}
