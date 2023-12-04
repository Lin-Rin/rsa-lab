package com.example.rsalab.controller;

import com.example.rsalab.dto.rabin.decrypt.DecryptRequest;
import com.example.rsalab.dto.rabin.decrypt.DecryptResponse;
import com.example.rsalab.dto.rabin.encrypt.EncryptRequest;
import com.example.rsalab.dto.rabin.encrypt.EncryptResponse;
import com.example.rsalab.dto.rabin.generate.ServerKeyResponse;
import com.example.rsalab.dto.rabin.sign.SignRequest;
import com.example.rsalab.dto.rabin.sign.SignResponse;
import com.example.rsalab.dto.rabin.verify.VerifyRequest;
import com.example.rsalab.dto.rabin.verify.VerifyResponse;
import com.example.rsalab.service.RabinService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rabin")
@RequiredArgsConstructor
public class RabinController {
    private final RabinService rabinService;

    @GetMapping("/serverKey")
    public ServerKeyResponse serverKey(
            @RequestParam("keySize") Long keySize
    ) {
        return rabinService.serverKey(keySize);
    }

    @GetMapping("/encrypt")
    public EncryptResponse encrypt(
            @RequestBody EncryptRequest request
    ) {
        return rabinService.encrypt(request);
    }

    @GetMapping("/decrypt")
    public DecryptResponse decrypt(
          @RequestBody DecryptRequest request
    ) {
        return rabinService.decrypt(request);
    }

    @GetMapping("/sign")
    public SignResponse sign(
            @RequestBody SignRequest request
    ) {
       return rabinService.sign(request);
    }

    @GetMapping("/verify")
    public VerifyResponse verify(
            @RequestBody VerifyRequest request
    ) {
        return rabinService.verify(request);
    }
}
