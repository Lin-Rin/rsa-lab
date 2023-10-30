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
//    private PrivateKey privateKey2;
    private final RsaService rsaService;
    private final int length = 64;

    @GetMapping("/serverKey")
    public GenerateKeyRespondDto serverKey() {
        GenerateKeyRespondDto respond = new GenerateKeyRespondDto();
        List<BigInteger> keys = rsaService.generateKeys(length);
        privateKey1 = new PrivateKey();
//        privateKey2 = new PrivateKey();

        BigInteger p = keys.get(0);
        BigInteger q = keys.get(1);
        BigInteger d = rsaService.getD(p, q, EXPONENT);
        privateKey1.setQ(q);
        privateKey1.setP(p);
        privateKey1.setD(d);
        privateKey1.setN(p.multiply(q));

//        BigInteger p1 = keys.get(2);
//        BigInteger q1 = keys.get(3);
//        BigInteger d1 = rsaService.getD(p1, q1, EXPONENT);
//        privateKey2.setQ(q1);
//        privateKey2.setP(p1);
//        privateKey2.setD(d1);
//        privateKey2.setN(p1.multiply(q1));

        respond.setModulus(privateKey1.getN().toString(16));
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

        BigInteger message = rsaService.decrypt(cipherText, privateKey1.getD(), privateKey1.getN());

        respondDto.setMessage(message.toString(16));

        return respondDto;
    }

    @GetMapping("/sign")
    public SignRespondDto sign(@RequestBody SignRequestDto requestDto) {
        SignRespondDto respondDto = new SignRespondDto();
        BigInteger message = new BigInteger(requestDto.getMessage(), 16);

        BigInteger sign = rsaService.sign(message, privateKey1.getD(), privateKey1.getN());

        respondDto.setSignature(sign.toString(16));

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
        SendKeyRespondDto respondDto = new SendKeyRespondDto();
        BigInteger modulus = new BigInteger(requestDto.getModulus(), 16);
        BigInteger publicExponent = new BigInteger(requestDto.getPublicExponent(), 16);

        BigInteger k = rsaService.getK(modulus, length);
        BigInteger s = rsaService.sendSignature(k, privateKey1);

        BigInteger k1 = rsaService.sendKey(k, modulus, publicExponent);
        BigInteger s1 = rsaService.sendKey(s, modulus, publicExponent);

        respondDto.setKey(k1.toString(16));
        respondDto.setSignature(s1.toString(16));
        return respondDto;
    }

    @GetMapping("/receiveKey")
    public RecevieKeyRespondDto receiveKey(@RequestBody RecevieKeyRequestDto requestDto) {
        RecevieKeyRespondDto respondDto = new RecevieKeyRespondDto();
        BigInteger key = new BigInteger(requestDto.getKey(), 16);
        BigInteger modulus = new BigInteger(requestDto.getModulus(), 16);
        BigInteger signature = new BigInteger(requestDto.getSignature(), 16);
        BigInteger publicExponent = new BigInteger(requestDto.getPublicExponent(), 16);

        BigInteger k = rsaService.receiveConfiguration(key, privateKey1.getD(), privateKey1.getN());
        BigInteger s = rsaService.receiveConfiguration(signature, privateKey1.getD(), privateKey1.getN());
        BigInteger kAuth = rsaService.receiveAuthentication(s, modulus, publicExponent);

        respondDto.setKey(k.toString(16));
        respondDto.setVerified(kAuth.equals(k));
        return respondDto;
    }

    // send
    // 1. генерую на сайті публ ключ // BD9DD2DE6527D9DEFBC523C209D64C0F
    // 2. генерую в себе   публ ключ // ccdad0288ac0aceac00fb54babb8f031
    // 3. заходжу на sendKey ввожу згенерований публ ключ 1.
        // key -- a88d9a91ae417c1481c6ca8c1af3172a
        //   s -- bcbf170de0c66c1aee0be969108dcb2f
    // 4. перевіряю на сайті
        // key -- a88d9a91ae417c1481c6ca8c1af3172a (3)
        //   s -- bcbf170de0c66c1aee0be969108dcb2f (3)
        // mod -- ccdad0288ac0aceac00fb54babb8f031 (2)
        // exp -- 10001
    // 5. все ок

    // receive
    // 1. генерую на сайті публ ключ // 9981A66F9F379F9BAE205A52949011CF
    // 2. генерую в себе   публ ключ // f58574edc7ce3eb87e96eae18eb089b5
    // 3. заходжу нас Send Key і вводжу згенерований ключ 2.
        // key -- 191590EC8E1D315AE1D1177A38E6CD25
        //   s -- 651550013BE0AC96D1D9ACF69B68DAA6
    // 4. перевіряю в себе
        // key --  191590EC8E1D315AE1D1177A38E6CD25 (3)
        //   s --  651550013BE0AC96D1D9ACF69B68DAA6 (3)
        // mod --  9981A66F9F379F9BAE205A52949011CF (1)
        // exp -- 10001
    // 5.

}
