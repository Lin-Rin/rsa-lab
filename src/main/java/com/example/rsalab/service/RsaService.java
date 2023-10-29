package com.example.rsalab.service;

import com.example.rsalab.util.math.PrimeNumberGenerator;
import com.example.rsalab.util.math.SimplicityTest;
import org.springframework.stereotype.Component;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Component
public class RsaService {
    private final Supplier<BigInteger> supplier = new PrimeNumberGenerator(256);
    private final Predicate<BigInteger> predicate = new SimplicityTest();

    public List<BigInteger> generateKeys() {
        List<BigInteger> list = new ArrayList<>();

        for (;;) {
            list.clear();

            list.add(supplier.get());
            list.add(supplier.get());
            list.add(supplier.get());
            list.add(supplier.get());

            list = list.stream()
                    .filter(predicate)
                    .sorted(Comparator.reverseOrder())
                    .collect(Collectors.toList());

            if (list.size() == 4) {
                BigInteger p = new BigInteger(list.get(0).toString());
                BigInteger q = new BigInteger(list.get(1).toString());
                BigInteger p1 = new BigInteger(list.get(2).toString());
                BigInteger q1 = new BigInteger(list.get(3).toString());

                if (p.multiply(q).compareTo(p1.multiply(q1)) <= 0) {
                    break;
                }
            }

        }

        // -> 3, 2, 1
        return list;
    }

    public BigInteger getD(BigInteger p, BigInteger q, BigInteger exponent) {
        BigInteger phi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));

        return exponent.modInverse(phi);
    }

    public BigInteger encrypt(BigInteger message, BigInteger n, BigInteger exponent) {
        return message.modPow(exponent, n);
    }

    public BigInteger decrypt(BigInteger cipherText, BigInteger d, BigInteger n) {
        return cipherText.modPow(d, n);
    }

    public BigInteger sign(BigInteger message, BigInteger d, BigInteger n) {
        return message.modPow(d, n);
    }

    public Boolean verify(BigInteger message, BigInteger signature, BigInteger n, BigInteger exponent) {
        return signature.modPow(exponent, n).equals(message);
    }

    public BigInteger sendKey() {
        return null;
    }

    public BigInteger receiveKey() {
        return null;
    }
}
