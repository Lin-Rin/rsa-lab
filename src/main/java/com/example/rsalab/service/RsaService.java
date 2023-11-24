package com.example.rsalab.service;

import com.example.rsalab.model.PrivateKey;
import com.example.rsalab.util.math.rsa.PrimeNumberGenerator;
import org.springframework.stereotype.Component;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class RsaService {
    private final PrimeNumberGenerator supplier = new PrimeNumberGenerator();

    public List<BigInteger> generateKeys(int length) {
        List<BigInteger> list = new ArrayList<>();
        supplier.setLength(length);

        list.add(supplier.get());
        list.add(supplier.get());
        list.add(supplier.get());
        list.add(supplier.get());

        // -> 3, 2, 1
        return list.stream()
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());
    }

    public BigInteger getD(BigInteger p, BigInteger q, BigInteger exponent) {
        BigInteger phi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));

        return exponent.modInverse(phi);
    }

    public BigInteger getK(BigInteger n, int length) {
        supplier.setLength(length);
        BigInteger k = supplier.get();

        while (k.compareTo(n) > 0) {
            k = supplier.get();
        }

        return k;
    }

    public BigInteger encrypt(BigInteger message, BigInteger modulus, BigInteger exponent) {
        return message.modPow(exponent, modulus);
    }

    public BigInteger decrypt(BigInteger cipherText, BigInteger d, BigInteger innerModulus) {
        return cipherText.modPow(d, innerModulus);
    }

    public BigInteger sign(BigInteger message, BigInteger d, BigInteger innerModulus) {
        return message.modPow(d, innerModulus);
    }

    public Boolean verify(BigInteger message, BigInteger signature, BigInteger modulus, BigInteger exponent) {
        return signature.modPow(exponent, modulus).equals(message);
    }

    public BigInteger sendKey(BigInteger number, BigInteger modulus, BigInteger exponent) {
        return number.modPow(exponent, modulus);
    }

    public BigInteger sendSignature(BigInteger k, PrivateKey privateKey) {
        return k.modPow(privateKey.getD(), privateKey.getN());
    }

    public BigInteger receiveConfiguration(BigInteger number, BigInteger d, BigInteger innerModulus) {
        return number.modPow(d, innerModulus);
    }

    public BigInteger receiveAuthentication(BigInteger s, BigInteger modulus, BigInteger exponent) {
        return s.modPow(exponent, modulus);
    }
}
