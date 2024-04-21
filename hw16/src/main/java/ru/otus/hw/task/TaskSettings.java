package ru.otus.hw.task;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class TaskSettings {

    private final boolean bookTaskEnable;

    public TaskSettings(@Value("${task.book.auto-create.enable:true}") boolean bookTaskEnable) {
        this.bookTaskEnable = bookTaskEnable;
    }
}
