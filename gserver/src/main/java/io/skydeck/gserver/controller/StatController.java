package io.skydeck.gserver.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/gserver/stat")
public class StatController {
    @GetMapping("/test")
    public Integer test() {
        return 0;
    }
}
