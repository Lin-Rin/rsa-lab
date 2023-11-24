package com.example.rsalab.service;

import com.example.rsalab.dto.rabin.decrypt.DecryptResponse;
import com.example.rsalab.dto.rabin.encrypt.EncryptResponse;
import com.example.rsalab.dto.rabin.generate.ServerKeyResponse;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

@Service
public class RabinService {
    private BigInteger p;
    private BigInteger q;

    public ServerKeyResponse init() {
        ServerKeyResponse response = new ServerKeyResponse();

        return response;
    }

    // (y, c1, c2)
    public EncryptResponse encrypt(BigInteger n, BigInteger x) {
        EncryptResponse response = new EncryptResponse();

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
