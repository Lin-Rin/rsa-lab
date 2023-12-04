package com.example.rsalab.util.math.rabin;

import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.List;
import java.util.Objects;

@Component
public class MathUtil {

    public List<BigInteger> gcd(BigInteger a, BigInteger b) {
        throw new UnsupportedOperationException();
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
}
