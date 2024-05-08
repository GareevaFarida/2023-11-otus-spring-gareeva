package ru.otus.hw.consumer.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("producer")
public interface RandomIntService {

    @GetMapping(value = "/random")
    int getRandomInt(@RequestParam(name = "upper-bound") int upperBound);
}
