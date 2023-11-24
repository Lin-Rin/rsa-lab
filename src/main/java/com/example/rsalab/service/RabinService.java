package com.example.rsalab.service;

import com.example.rsalab.dto.rabin.decrypt.DecryptResponse;
import com.example.rsalab.dto.rabin.encrypt.EncryptResponse;
import com.example.rsalab.dto.rabin.generate.ServerKeyResponse;
import com.example.rsalab.model.RabinPrivateKey;
import com.example.rsalab.util.math.rabin.MathUtil;
import com.example.rsalab.util.math.rabin.NumberGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

@RequiredArgsConstructor
@Service
public class RabinService {
    private RabinPrivateKey privateKey;
    private final MathUtil mathUtil;
    private final NumberGenerator generator;

    public ServerKeyResponse serverKey(Long keySize) {
        ServerKeyResponse response = new ServerKeyResponse();
        privateKey = new RabinPrivateKey();

        var p = generator.getPrimeNumber(keySize);
        var q = generator.getPrimeNumber(keySize);
        privateKey.setQ(q);
        privateKey.setP(p);

        var n = p.multiply(q);
        BigInteger b = generator.generateNumber(BigInteger.valueOf(2).pow(Math.toIntExact(keySize)));

        response.setModulus(n.toString(16));
        response.setModulus(b.toString(16));

        return response;
    }

    // (y, c1, c2)
    public EncryptResponse encrypt(BigInteger n, BigInteger x) {
        EncryptResponse response = new EncryptResponse();
        var r = generator.get64BitNumber();
        var newX = r;

        return response;
    }

    // (y, c1, c2)
    public DecryptResponse decrypt(List<BigInteger> cipherText) {
        DecryptResponse response = new DecryptResponse();

        return response;
    }

    public List<BigInteger> sign(BigInteger p, BigInteger q, BigInteger message, BigInteger n, BigInteger x) {
        throw new UnsupportedOperationException();
    }

    public BigInteger verify(BigInteger sign, BigInteger n) {
        throw new UnsupportedOperationException();
    }
}
