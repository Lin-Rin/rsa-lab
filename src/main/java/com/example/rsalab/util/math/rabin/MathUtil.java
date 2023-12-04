package com.example.rsalab.util.math.rabin;

import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.List;
import java.util.Objects;

@Component
public class MathUtil {

    public BigInteger gcd(BigInteger a, BigInteger b) {
        BigInteger d = BigInteger.ONE;

        while (isPair(a) && isPair(b)) {
            a = a.divide(BigInteger.TWO);
            b = b.divide(BigInteger.TWO);
            d = d.multiply(BigInteger.TWO);
        }

        while (isPair(a)) {
            a = a.divide(BigInteger.TWO);
        }

        while (!b.equals(BigInteger.ZERO)) {
            while (isPair(b)) {
                b = b.divide(BigInteger.TWO);
            }
            BigInteger temp = a.subtract(b).abs();
            a = a.min(b);
            b = temp;
        }

        d = d.multiply(a);
        return d;
    }

    public List<BigInteger> extendedEuclidean(BigInteger p, BigInteger q) {
        throw new UnsupportedOperationException();
    }

    public List<BigInteger> sqrt(BigInteger y, BigInteger p, BigInteger q) {
        throw new UnsupportedOperationException();
    }

    public BigInteger getJacobiSymbol(BigInteger a, BigInteger n) {
        BigInteger minusOne = BigInteger.ZERO.subtract(BigInteger.ONE);
        BigInteger pred = BigInteger.ONE;

        if (!gcd(a, n).equals(BigInteger.ONE)) return BigInteger.ZERO;

        while (!Objects.equals(a, BigInteger.ONE)) {
            if (a.compareTo(n) > 0) {
                a = a.mod(n);
            }
            pred = pred.multiply(minusOne.pow((a.subtract(BigInteger.ONE).multiply(n.subtract(BigInteger.ONE))).intValue() / 4));
            BigInteger temp = a;
            a = n;
            n = temp;
        }

        return pred;
    }

    private boolean isPair(BigInteger a) {
        return a.mod(BigInteger.TWO).equals(BigInteger.ZERO);
    }
}
