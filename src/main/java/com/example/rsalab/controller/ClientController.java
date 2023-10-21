package com.example.rsalab.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ClientController {

    @GetMapping(value = "/client")
    public String sayHello() {
        return "It's aliveee";
    }

}
