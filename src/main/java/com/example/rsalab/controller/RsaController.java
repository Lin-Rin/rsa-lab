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
import com.example.rsalab.service.RsaService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.math.BigInteger;
import java.util.List;

@RestController
@RequestMapping("/rsa")
@RequiredArgsConstructor
public class RsaController {
    private static final BigInteger EXPONENT = BigInteger.valueOf(2).pow(16).add(BigInteger.ONE);
    private PrivateKey privateKey1;
    private PrivateKey privateKey2;
    private final RsaService rsaService;

    @GetMapping("/serverKey")
    public GenerateKeyRespondDto serverKey() {
        GenerateKeyRespondDto respond = new GenerateKeyRespondDto();
        List<BigInteger> keys = rsaService.generateKeys();
        privateKey1 = new PrivateKey();
        privateKey2 = new PrivateKey();

        BigInteger p = keys.get(0); // private
        BigInteger q = keys.get(1); // private
        BigInteger d = rsaService.getD(p, q, EXPONENT); // private
        System.out.println(d.toString(16));
        privateKey1.setQ(q);
        privateKey1.setP(p);
        privateKey1.setD(d);

        BigInteger p1 = keys.get(2); // private
        BigInteger q1 = keys.get(3); // private
        BigInteger d1 = rsaService.getD(p1, q1, EXPONENT); // private
        privateKey2.setQ(q1);
        privateKey2.setP(p1);
        privateKey2.setD(d1);

        BigInteger n = p.multiply(q); // public + exp
        BigInteger n1 = p1.multiply(q1); // public + exp

        respond.setModulus(n.toString(16));
        respond.setPublicExponent(EXPONENT.toString(16));

        return respond;
    }

    @GetMapping("/encrypt")
    public EncryptRespondDto encrypt(@RequestBody EncryptRequestDto requestDto) {
        EncryptRespondDto respondDto = new EncryptRespondDto();
        BigInteger modulus = new BigInteger(requestDto.getModulus(), 16);
        BigInteger publicExponent = new BigInteger(requestDto.getPublicExponent(), 16);
        BigInteger message = new BigInteger(requestDto.getMessage(), 16);

        BigInteger cipherText = rsaService.encrypt(message, modulus, publicExponent);

        respondDto.setCipherText(cipherText.toString(16));

        return respondDto;
    }

    @GetMapping("/decrypt")
    public DecryptRespondDto decrypt(@RequestBody DecryptRequestDto requestDto) {
        DecryptRespondDto respondDto = new DecryptRespondDto();
        BigInteger cipherText = new BigInteger(requestDto.getCipherText(), 16);

        BigInteger message = rsaService.decrypt(cipherText,
                privateKey1.getD(), privateKey1.getP().multiply(privateKey1.getQ()));

        respondDto.setMessage(message.toString(16));

        return respondDto;
    }

    @GetMapping("/sign")
    public SignRespondDto sign(@RequestBody SignRequestDto requestDto) {
        SignRespondDto respondDto = new SignRespondDto();
        BigInteger message = new BigInteger(requestDto.getMessage(), 16);

        respondDto.setSignature(rsaService.sign(message, privateKey1.getD(),
                privateKey1.getP().multiply(privateKey1.getQ())).toString(16));

        return respondDto;
    }

    @GetMapping("/verify")
    public VerifyRespondDto verify(@RequestBody VerifyRequestDto requestDto) {
        VerifyRespondDto respondDto = new VerifyRespondDto();
        BigInteger message = new BigInteger(requestDto.getMessage(), 16);
        BigInteger signature = new BigInteger(requestDto.getSignature(), 16);
        BigInteger modulus = new BigInteger(requestDto.getModulus(), 16);
        BigInteger publicExponent = new BigInteger(requestDto.getPublicExponent(), 16);

        respondDto.setVerified(rsaService.verify(message, signature, modulus, publicExponent));

        return respondDto;
    }

    @GetMapping("/sendKey")
    public SendKeyRespondDto sendKey(@RequestBody SendKeyRequestDto requestDto) {
        return new SendKeyRespondDto();
    }

    @GetMapping("/receiveKey")
    public RecevieKeyRespondDto receiveKey(@RequestBody RecevieKeyRequestDto requestDto) {
        return new RecevieKeyRespondDto();
    }
}
