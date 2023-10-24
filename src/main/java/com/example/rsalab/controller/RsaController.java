package com.example.rsalab.controller;

import com.example.rsalab.dto.rsa.decrypt.DecryptRequestDto;
import com.example.rsalab.dto.rsa.decrypt.DecryptRespondDto;
import com.example.rsalab.dto.rsa.encrypt.EncryptRequestDto;
import com.example.rsalab.dto.rsa.encrypt.EncryptRespondDto;
import com.example.rsalab.dto.rsa.generate.GenerateKeyRespondDto;
import com.example.rsalab.dto.rsa.key.recevie.RecevieKeyRequestDto;
import com.example.rsalab.dto.rsa.key.recevie.RecevieKeyRespondDto;
import com.example.rsalab.dto.rsa.key.send.SendKeyRequestDto;
import com.example.rsalab.dto.rsa.key.send.SendKeyRespondDto;
import com.example.rsalab.dto.rsa.sing.SignRequestDto;
import com.example.rsalab.dto.rsa.sing.SignRespondDto;
import com.example.rsalab.dto.rsa.verify.VerifyRequestDto;
import com.example.rsalab.dto.rsa.verify.VerifyRespondDto;
import com.example.rsalab.model.PrivateKey;
import com.example.rsalab.service.DecryptionService;
import com.example.rsalab.service.EncryptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rsa")
@RequiredArgsConstructor
public class RsaController {
    @GetMapping("/serverKey")
    public GenerateKeyRespondDto serverKey(@RequestBody GenerateKeyRespondDto respondDto) {
        return new GenerateKeyRespondDto();
    }

    @GetMapping("/encrypt")
    public EncryptRespondDto encrypt(@RequestBody EncryptRequestDto requestDto) {
        return new EncryptRespondDto();
    }

    @GetMapping("/decrypt")
    public DecryptRespondDto decrypt(@RequestBody DecryptRequestDto requestDto) {
        return new DecryptRespondDto();
    }

    @GetMapping("/sign")
    public SignRespondDto sign(@RequestBody SignRequestDto requestDto) {
        return new SignRespondDto();
    }

    @GetMapping("/verify")
    public VerifyRespondDto verify(@RequestBody VerifyRequestDto requestDto) {
        return new VerifyRespondDto();
    }

    @GetMapping("/sendKey")
    public SendKeyRespondDto sendKey(@RequestBody SendKeyRequestDto requestDto) {
        return new SendKeyRespondDto();
    }

    @GetMapping("/receiveKey")
    public RecevieKeyRespondDto receiveKey(@RequestBody RecevieKeyRequestDto respondDto) {
        return new RecevieKeyRespondDto();
    }
}
