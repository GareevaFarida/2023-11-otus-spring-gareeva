package ru.otus.hw.producer.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
public class RandomIntController {

    @GetMapping(value = "/random")
    public int getRandomInt(@RequestParam(name = "upper-bound") int upperBound) {
        Random random = new Random();
        return random.nextInt(upperBound);
    }
}
