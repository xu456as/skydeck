package io.skydeck.gserver.controller;

import io.skydeck.gserver.engine.GameEngine;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/gserver/stat")
public class StatController {

    @Resource
    private GameEngine engine;
    @GetMapping("/test")
    public Integer test() {
//        engine.onActivePhase();
        return 0;
    }
}
