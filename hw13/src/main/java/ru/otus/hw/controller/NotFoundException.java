package ru.otus.hw.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
class NotFoundException extends RuntimeException {

    NotFoundException() {
    }
}
