package com.example.rsalab.controller;

import com.example.rsalab.dto.rabin.encrypt.EncryptRequest;
import com.example.rsalab.dto.rabin.encrypt.EncryptResponse;
import com.example.rsalab.dto.rabin.generate.ServerKeyResponse;
import com.example.rsalab.service.RabinService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rabin")
@RequiredArgsConstructor
public class RabinController {
    private final RabinService rabinService;

    public ServerKeyResponse serverKey(
            @RequestParam("keySize") Long keySize
    ) {
        return rabinService.serverKey(keySize);
    }

    public EncryptResponse encrypt(
            @RequestBody EncryptRequest request
    ) {
        return rabinService.encrypt(request);
    }

}
