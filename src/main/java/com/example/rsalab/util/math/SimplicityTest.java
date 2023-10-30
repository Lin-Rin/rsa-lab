package com.example.rsalab.util.math;

import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.Random;
import java.util.function.Predicate;

@Component
public class SimplicityTest implements Predicate<BigInteger> {
    @Override
    public boolean test(BigInteger number) {
        BigInteger s = new BigInteger("0", 10);
        BigInteger d = number.subtract(BigInteger.ONE);

        while (d.mod(BigInteger.TWO).equals(BigInteger.ZERO)) {
            s = s.add(BigInteger.ONE);
            d = d.divide(BigInteger.TWO);
        }

        for (int k = 0; k < number.bitLength(); k++) {
            BigInteger a = uniformRandom(number.subtract(BigInteger.ONE));
            BigInteger x = a.modPow(d, number);

            if (x.equals(BigInteger.ONE) || x.equals(number.subtract(BigInteger.ONE))) {
                continue;
            }

            int r = 0;
            for (; s.compareTo(BigInteger.ZERO) > r; r++) {
                x = x.modPow(BigInteger.TWO, number);

                if (x.equals(BigInteger.ONE)) {
                    return false;
                }

                if (x.equals(number.subtract(BigInteger.ONE))) {
                    break;
                }
            }

            if (s.compareTo(BigInteger.ZERO) == r) {
                return false;
            }
        }

        return true;
    }

    private BigInteger uniformRandom(BigInteger top) {
        Random random = new Random();
        BigInteger res;
        do {
            res = new BigInteger(top.bitLength(), random);
        } while (res.compareTo(BigInteger.TWO) < 0 || res.compareTo(top) > 0);
        return res;
    }
}
