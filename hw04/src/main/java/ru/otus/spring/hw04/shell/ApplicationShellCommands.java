package ru.otus.spring.hw04.shell;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import ru.otus.spring.hw04.service.LocalizedMessagesService;
import ru.otus.spring.hw04.service.TestRunnerService;

@ShellComponent
public class ApplicationShellCommands {

    private final TestRunnerService testRunnerService;

    private final LocalizedMessagesService localizedMessagesService;

    private String userName;

    public ApplicationShellCommands(TestRunnerService testRunnerService,
                                    @Qualifier("localizedMessagesServiceImpl") LocalizedMessagesService locService) {
        this.testRunnerService = testRunnerService;
        this.localizedMessagesService = locService;
    }


    @ShellMethod(value = "Login command", key = {"l", "login"})
    public String login(String userName) {
        this.userName = userName;
        return localizedMessagesService.getMessage("ApplicationShellCommands.welcome", userName);
    }

    @ShellMethod(value = "Start testing", key = {"start", "test"})
    @ShellMethodAvailability(value = "isStartTestingCommandAvailable")
    public void startTesting() {
        testRunnerService.run();
    }

    public Availability isStartTestingCommandAvailable() {
        String message = localizedMessagesService.getMessage("ApplicationShellCommands.login.before", userName);
        return userName == null ? Availability.unavailable(message) : Availability.available();
    }
}
